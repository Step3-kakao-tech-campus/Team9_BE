package com.kakao.linknamu.thirdparty.googledocs.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.Document;
import com.google.api.services.docs.v1.model.ParagraphElement;
import com.google.api.services.docs.v1.model.StructuralElement;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkService;
import com.kakao.linknamu.core.config.GoogleDocsConfig;
import com.kakao.linknamu.thirdparty.googledocs.entity.GooglePage;
import com.kakao.linknamu.thirdparty.googledocs.repository.GooglePageJpaRepository;
import com.kakao.linknamu.thirdparty.googledocs.util.InvalidGoogleDocsApiException;
import com.kakao.linknamu.thirdparty.utils.JsoupUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.kakao.linknamu.core.config.GoogleDocsConfig.getCredentials;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleDocsApiBatchService {
	private final GooglePageJpaRepository googlePageJpaRepository;
	private final JsoupUtils jsoupUtils;
	private final BookmarkService bookmarkService;

	@Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
	public void googleDocsApiCronJob() {
		// 활성화된 페이지들을 리스트에 저장
		List<GooglePage> activeGoogleDocsPages = googlePageJpaRepository.findByActivePage();

		// 활성화된 구글 독스 페이지들에 대해 배치를 실행한다.
		activeGoogleDocsPages.forEach((GooglePage gp) -> {
			try {
				List<Bookmark> resultBookmarks = getLinks(gp);
				bookmarkService.batchInsertBookmark(resultBookmarks);
			} catch (InvalidGoogleDocsApiException e) {
				gp.deactivate();
				googlePageJpaRepository.save(gp);
			}
		});
	}

	private List<Bookmark> getLinks(GooglePage googlePage) {
		Set<Bookmark> resultBookmarks = new HashSet<>();
		try {
			// 서비스 생성
			final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			Docs service = new Docs.Builder(
				httpTransport,
				GoogleDocsConfig.getJSON_FACTORY(),
				getCredentials(httpTransport))
				.setApplicationName(GoogleDocsConfig.getAPPLICATION_NAME())
				.build();

			// google docs 객체 생성 및 get API를 사용해서 link 항목 불러오기
			Document response = service.documents().get(googlePage.getDocumentId()).execute();
			List<StructuralElement> contents = response.getBody().getContent();
			for (StructuralElement e : contents) {
				if (e.getParagraph() != null && e.getParagraph().getElements() != null) {
					List<ParagraphElement> elements = e.getParagraph().getElements();
					for (ParagraphElement pe : elements) {
						if (pe.getTextRun() != null && pe.getTextRun().getTextStyle() != null
							&& pe.getTextRun().getTextStyle().getLink() != null) {
							String link = pe.getTextRun().getTextStyle().getLink().getUrl();
							if (link != null) {
								// 만약 한번 연동한 링크라면 더 이상 진행하지 않는다.
								if (bookmarkService.existByBookmarkLinkAndCategoryId(link,
									googlePage.getCategory().getCategoryId())) {
									continue;
								}

								// 링크 항목이 있을 경우 해당 링크의 사이트 제목을 Jsoup 라이브러리를 사용해 받아온다.
								// 이를 생성할 북마크의 이름으로 사용한다.
								// 만약 해당 데이터를 읽어오지 못하는 경우 link로 저장한다.
								String bookmarkName = link;
								bookmarkName = jsoupUtils.getTitle(link);
								resultBookmarks.add(Bookmark.builder()
									.bookmarkLink(link)
									.bookmarkName(bookmarkName.length() > 30
										? bookmarkName.substring(0, 30) : bookmarkName)
									.category(googlePage.getCategory())
									.build());
							}
						}
					}
				}
			}
		} catch (GeneralSecurityException error) {
			log.error("구글 인증에 문제가 있습니다.");
			throw new InvalidGoogleDocsApiException();
		} catch (IOException error) {
			log.error("구글Docs 연동 중 문제가 발생했습니다.");
			throw new InvalidGoogleDocsApiException();
		}

		return resultBookmarks.stream().toList();
	}
}
