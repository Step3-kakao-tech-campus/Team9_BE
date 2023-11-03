package com.kakao.linknamu.googleDocs.service;

import static com.kakao.linknamu.core.config.GoogleDocsConfig.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.Document;
import com.google.api.services.docs.v1.model.ParagraphElement;
import com.google.api.services.docs.v1.model.StructuralElement;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkCreateService;
import com.kakao.linknamu.core.config.GoogleDocsConfig;
import com.kakao.linknamu.googleDocs.entity.GooglePage;
import com.kakao.linknamu.googleDocs.util.InvalidGoogleDocsApiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleDocsApiBatchService {

	@Value("${proxy.enabled:false}")
	private boolean isProxyEnabled;

	private final GoogleDocsApiGetService googleDocsApiGetService;
	private final BookmarkCreateService bookmarkCreateService;

	private final static String PROXY_HOST = "krmp-proxy.9rum.cc";
	private final static int PROXY_PORT = 3128;

	@Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
	public void googleDocsApiCronJob() {
		// 활성화된 페이지들을 리스트에 저장
		List<GooglePage> activeGoogleDocsPages = googleDocsApiGetService.getActiveGoogleDocsPage();

		// 활성화된 구글 독스 페이지들에 대해 배치를 실행한다.
		activeGoogleDocsPages.forEach((GooglePage gp) -> {
			try {
				List<Bookmark> resultBookmarks = getLinks(gp);
				bookmarkCreateService.batchInsertBookmark(resultBookmarks);
			} catch (InvalidGoogleDocsApiException e) {
				gp.deactivate();
			}
		});
	}

	private List<Bookmark> getLinks(GooglePage googlePage) {
		List<Bookmark> resultBookmarks = new ArrayList<>();
		try {
			// 서비스 생성
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			Docs service = new Docs.Builder(HTTP_TRANSPORT, GoogleDocsConfig.getJSON_FACTORY(),
				getCredentials(HTTP_TRANSPORT))
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
								// 링크 항목이 있을 경우 해당 링크의 사이트 제목을 Jsoup 라이브러리를 사용해 받아온다.
								// 이를 생성할 북마크의 이름으로 사용한다.
								// 만약 해당 데이터를 읽어오지 못하는 경우 link로 저장한다.
								String bookmarkName = link;
								bookmarkName = getTitleName(link);
								resultBookmarks.add(Bookmark.builder()
									.bookmarkLink(link)
									.bookmarkName(
										bookmarkName.length() > 30 ? bookmarkName.substring(0, 30) : bookmarkName)
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

		return resultBookmarks;
	}

	private String getTitleName(String href) {
		try {
			org.jsoup.nodes.Document document = getWebPage(href);
			return document.title();
		} catch (IOException e) {
			return href;
		}
	}

	private org.jsoup.nodes.Document getWebPage(String href) throws IOException {
		if (isProxyEnabled) {
			return Jsoup.connect(href)
				.timeout(5000)
				.proxy(PROXY_HOST, PROXY_PORT)
				.get();
		} else {
			return Jsoup.connect(href)
				.timeout(5000)
				.get();
		}
	}
}
