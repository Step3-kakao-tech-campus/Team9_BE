package com.kakao.linknamu.thirdparty.notion.service;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkCreateService;
import com.kakao.linknamu.bookmark.service.BookmarkReadService;
import com.kakao.linknamu.thirdparty.notion.entity.NotionPage;
import com.kakao.linknamu.thirdparty.notion.repository.NotionPageJpaRepository;
import com.kakao.linknamu.thirdparty.notion.util.InvalidNotionApiException;
import com.kakao.linknamu.thirdparty.notion.util.NotionApiUriBuilder;
import com.kakao.linknamu.thirdparty.utils.JsoupUtils;
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

	private final RestTemplate restTemplate;
	private final ObjectProvider<JSONParser> jsonParserProvider;
	private final NotionApiUriBuilder notionApiUriBuilder;
	private final NotionApiGetService notionApiGetService;
	private final BookmarkCreateService bookmarkCreateService;
	private final BookmarkReadService bookmarkReadService;
	private final JsoupUtils jsoupUtils;

	// 멘션 타입의 노션 페이지 링크일 시, Untitled로 나와 이를 기본 설정으로 변경해서 저장한다.
	private static final String DEFAULT_NOTION_BOOKMARK_NAME = "노션 페이지";
	private static final String NOTION_VERSION = "2022-06-28";

	// 한 시간마다 notion API를 통해서 연동한 페이지의 링크를 가져오는 기능 수행
	@Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
	public void notionApiCronJob() {
		// 1. isActive==true인 NotionPage를 가져온다.
		log.info("clone start");
		List<NotionPage> activeNotionPages = notionApiGetService.getActiveNotionPage();

		// 2. NotionPage 정보 호출
		activeNotionPages.forEach((NotionPage n) -> {
			try {
				List<Bookmark> resultBookmarks = getPageLinks(n.getPageId(), n.getNotionAccount().getToken(), n);
				// 2-2 배치 insert 추가
				bookmarkCreateService.batchInsertBookmark(resultBookmarks);
			} catch (InvalidNotionApiException e) {
				// 2-2 NotionPage 접근 권한, 없는 페이지라면 isActive를 false로 만들고 종료
				n.deactivate();
				notionPageJpaRepository.save(n);
			}
		});
	}

	private List<Bookmark> getPageLinks(String pageId, String accessToken, NotionPage notionPage) {
		Boolean hasMore = true;
		String nextCursor = null;
		Set<Bookmark> resultBookmarks = new HashSet<>();
		JSONParser jsonParser = jsonParserProvider.getObject();

		while (hasMore) {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", accessToken);
			headers.set("Notion-Version", NOTION_VERSION);
			HttpEntity<?> httpEntity = new HttpEntity<>(headers);
			String uri = notionApiUriBuilder.getBlockUri(pageId, Optional.ofNullable(nextCursor));
			try {
				ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
				JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
				JSONArray result = (JSONArray) jsonObject.get("results");
				hasMore = (Boolean) jsonObject.get("has_more");
				nextCursor = (String) jsonObject.get("next_cursor");

				for (Object o : result) {
					JSONObject subObject = (JSONObject) o;
					String type = (String) subObject.get("type");
					JSONObject subObjectChild = (JSONObject) subObject.get(type);
					if (type.equals("bookmark") | type.equals("embed")) {
						saveBookmarkOrEmbedLink(subObjectChild, resultBookmarks, notionPage);
					} else {
						saveOtherLink(subObjectChild, resultBookmarks, type, notionPage);
					}
				}

			} catch (HttpClientErrorException e) {
				if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					log.error("유효하지 않은 토큰");
					throw new InvalidNotionApiException();
				} else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
					log.error("요청 한도 초과");
				} else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
					log.error("잘못된 Page Id");
					throw new InvalidNotionApiException();
				} else {
					log.error(e.getMessage());
				}
			} catch (ParserException e) {
				log.error("올바르지 않은 json parser : " + e.getMessage());
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

		return resultBookmarks.stream().toList();
	}

	// 노션 링크 데이터 종류 중 북마크와 임베드 형태를 저장한다.
	private void saveBookmarkOrEmbedLink(
		JSONObject bookmarkTypeObject,
		Set<Bookmark> resultBookmarks, NotionPage notionPage) {
		JSONArray caption = (JSONArray) bookmarkTypeObject.get("caption");
		String url = (String) bookmarkTypeObject.get("url");
		// 만약 한번 연동한 링크라면 더 이상 진행하지 않는다.
		if (bookmarkReadService.existByBookmarkLinkAndCategoryId(url, notionPage.getCategory().getCategoryId())) {
			return;
		}

		String title = url;
		if (!caption.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < caption.size(); i++) {
				JSONObject captionObject = (JSONObject) caption.get(i);
				sb.append(captionObject.get("plain_text"));
				title = sb.toString().strip();
			}
		} else {
			title = jsoupUtils.getTitle(url);
		}

		resultBookmarks.add(Bookmark.builder()
			.bookmarkLink(url)
			.bookmarkName(title.length() > 30 ? title.substring(0, 30) : title)
			.category(notionPage.getCategory())
			.build()
		);
	}

	// 북마크, 임베드 이외의 링크 데이터를 저장한다.
	private void saveOtherLink(JSONObject otherTypeObject, Set<Bookmark> resultBookmarks, String type, NotionPage notionPage) {
		JSONArray richTexts = (JSONArray) otherTypeObject.get("rich_text");
		if (Objects.isNull(richTexts)) {
			return;
		}

		for (Object richText : richTexts) {
			String href = (String) ((JSONObject) richText).get("href");
			String plainText = (String) ((JSONObject) richText).get("plain_text");

			if (Objects.nonNull(href)) {
				// 만약 한번 연동한 링크라면 더 이상 진행하지 않는다.
				if (bookmarkReadService.existByBookmarkLinkAndCategoryId(href, notionPage.getCategory().getCategoryId())) {
					continue;
				}

				if (href.equals(plainText)) {
					plainText = jsoupUtils.getTitle(href);
				} else if(type.equals("mention") || plainText.equals("Untitled")) {
					plainText = DEFAULT_NOTION_BOOKMARK_NAME;
				}

				resultBookmarks.add(Bookmark.builder()
					.bookmarkLink(href.startsWith("/") ? "https://www.notion.so" + href : href)
					.bookmarkName(plainText.length() > 30 ? plainText.substring(0, 30) : plainText)
					.category(notionPage.getCategory())
					.build());
			}
		}
	}

}
