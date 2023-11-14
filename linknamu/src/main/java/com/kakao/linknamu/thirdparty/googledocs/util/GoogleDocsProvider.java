package com.kakao.linknamu.thirdparty.googledocs.util;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.Document;
import com.google.api.services.docs.v1.model.ParagraphElement;
import com.google.api.services.docs.v1.model.StructuralElement;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkService;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.core.config.GoogleDocsConfig;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception500;
import com.kakao.linknamu.core.util.JsoupResult;
import com.kakao.linknamu.core.util.JsoupUtils;
import com.kakao.linknamu.thirdparty.googledocs.GoogleDocsExceptionStatus;
import com.kakao.linknamu.thirdparty.googledocs.entity.GooglePage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
public class GoogleDocsProvider {
	private final BookmarkService bookmarkService;
	private final JsoupUtils jsoupUtils;

	public String getGoogleDocsTitle(String documentId) {
		try {
			final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			Docs service = new Docs.Builder(httpTransport,
				GoogleDocsConfig.getJSON_FACTORY(), getCredentials(httpTransport))
				.setApplicationName(GoogleDocsConfig.getAPPLICATION_NAME())
				.build();

			// google docs 객체 생성 및 get API를 사용해서 link 항목 불러오기
			Document response = service.documents().get(documentId).execute();

			return response.getTitle();
		} catch (GeneralSecurityException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} catch (GoogleJsonResponseException e) {
			if (e.getStatusCode() == 404) {
				throw new Exception400(GoogleDocsExceptionStatus.GOOGLE_DOCS_NOT_EXIST);
			} else if (e.getStatusCode() == 403) {
				throw new Exception400(GoogleDocsExceptionStatus.GOOGLE_DOCS_NOT_ACCESS);
			}
			log.error(e.getMessage());
			throw new Exception500(GoogleDocsExceptionStatus.GOOGLE_DOCS_LINK_ERROR);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new Exception500(GoogleDocsExceptionStatus.GOOGLE_DOCS_LINK_ERROR);
		}
	}

	public List<Bookmark> getLinks(String documentId, Category category) {
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
			Document response = service.documents().get(documentId).execute();
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
									category.getCategoryId())) {
									continue;
								}

								JsoupResult jsoupResult = jsoupUtils.getTitleAndImgUrl(link);
								resultBookmarks.add(Bookmark.builder()
									.bookmarkLink(link)
									.bookmarkName(jsoupResult.getTitle())
									.bookmarkThumbnail(jsoupResult.getImageUrl())
									.category(category)
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
