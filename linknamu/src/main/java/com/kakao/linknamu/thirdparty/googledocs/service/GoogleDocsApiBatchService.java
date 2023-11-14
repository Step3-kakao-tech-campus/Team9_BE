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
import com.kakao.linknamu.thirdparty.googledocs.util.GoogleDocsProvider;
import com.kakao.linknamu.thirdparty.googledocs.util.InvalidGoogleDocsApiException;

import com.kakao.linknamu.core.util.JsoupResult;
import com.kakao.linknamu.core.util.JsoupUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleDocsApiBatchService {
	private final GooglePageJpaRepository googlePageJpaRepository;
	private final GoogleDocsProvider googleDocsProvider;
	private final BookmarkService bookmarkService;

	@Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
	public void googleDocsApiCronJob() {
		// 활성화된 페이지들을 리스트에 저장
		List<GooglePage> activeGoogleDocsPages = googlePageJpaRepository.findByActivePage();

		// 활성화된 구글 독스 페이지들에 대해 배치를 실행한다.
		activeGoogleDocsPages.forEach((GooglePage gp) -> {
			try {
				List<Bookmark> resultBookmarks = googleDocsProvider.getLinks(gp.getDocumentId(), gp.getCategory());
				bookmarkService.batchInsertBookmark(resultBookmarks);
			} catch (InvalidGoogleDocsApiException e) {
				gp.deactivate();
				googlePageJpaRepository.save(gp);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		});
	}


}
