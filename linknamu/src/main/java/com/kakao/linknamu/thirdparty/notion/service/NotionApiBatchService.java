package com.kakao.linknamu.thirdparty.notion.service;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkService;
import com.kakao.linknamu.thirdparty.notion.entity.NotionPage;
import com.kakao.linknamu.thirdparty.notion.repository.NotionPageJpaRepository;
import com.kakao.linknamu.thirdparty.notion.util.InvalidNotionApiException;
import com.kakao.linknamu.thirdparty.notion.util.NotionApiUriBuilder;
import com.kakao.linknamu.core.util.JsoupResult;
import com.kakao.linknamu.core.util.JsoupUtils;
import com.kakao.linknamu.thirdparty.notion.util.NotionProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.parser.ParserException;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotionApiBatchService {
	private final NotionPageJpaRepository notionPageJpaRepository;
	private final BookmarkService bookmarkService;
	private final NotionProvider notionProvider;

	// 한 시간마다 notion API를 통해서 연동한 페이지의 링크를 가져오는 기능 수행
	@Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
	public void notionApiCronJob() {
		// 1. isActive==true인 NotionPage를 가져온다.
		log.info("clone start");
		List<NotionPage> activeNotionPages = notionPageJpaRepository.findByActivePageFetchJoin();

		// 2. NotionPage 정보 호출
		activeNotionPages.forEach((NotionPage n) -> {
			try {
				List<Bookmark> resultBookmarks = notionProvider.getPageLinks(
					n.getPageId(),
					n.getNotionAccount().getToken(),
					n.getCategory().getCategoryId()
				);
				// 2-2 배치 insert 추가
				bookmarkService.batchInsertBookmark(resultBookmarks);
			} catch (InvalidNotionApiException e) {
				// 2-2 NotionPage 접근 권한, 없는 페이지라면 isActive를 false로 만들고 종료
				n.deactivate();
				notionPageJpaRepository.save(n);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		});
	}
}
