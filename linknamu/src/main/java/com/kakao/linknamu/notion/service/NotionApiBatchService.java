package com.kakao.linknamu.notion.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.parser.ParserException;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkCreateService;
import com.kakao.linknamu.notion.entity.NotionPage;
import com.kakao.linknamu.notion.repository.NotionPageJPARepository;
import com.kakao.linknamu.notion.util.InvalidNotionApiException;
import com.kakao.linknamu.notion.util.NotionApiUriBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotionApiBatchService {
	private final NotionPageJPARepository notionPageJPARepository;
	@Value("${proxy.enabled:false}")
	private boolean isProxyEnabled;

	private final RestTemplate restTemplate;
	private final ObjectProvider<JSONParser> jsonParserProvider;
	private final NotionApiUriBuilder notionApiUriBuilder;
	private final NotionApiGetService notionApiGetService;
	private final BookmarkCreateService bookmarkCreateService;

	private final static String PROXY_HOST = "krmp-proxy.9rum.cc";
	private final static int PROXY_PORT = 3128;
	private final static String NOTION_VERSION = "2022-06-28";

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
				notionPageJPARepository.save(n);
			}
		});
	}

	private List<Bookmark> getPageLinks(String pageId, String accessToken, NotionPage notionPage) {
		Boolean hasMore = true;
		String nextCursor = null;
		List<Bookmark> resultBookmarks = new ArrayList<>();
		JSONParser jsonParser = jsonParserProvider.getObject();

		while (hasMore) {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", accessToken);
			headers.set("Notion-Version", NOTION_VERSION);
			HttpEntity<?> httpEntity = new HttpEntity<>(headers);
			String uri = notionApiUriBuilder.getBlockUri(pageId, Optional.ofNullable(nextCursor));
			try {
				ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
				JSONObject jsonObject = (JSONObject)jsonParser.parse(response.getBody());
				JSONArray result = (JSONArray)jsonObject.get("results");
				hasMore = (Boolean)jsonObject.get("has_more");
				nextCursor = (String)jsonObject.get("next_cursor");

				for (Object o : result) {
					JSONObject subObject = (JSONObject)o;
					String type = (String)subObject.get("type");
					JSONObject subObjectChild = (JSONObject)subObject.get(type);
					if (type.equals("bookmark") | type.equals("embed")) {
						saveBookmarkOrEmbedLink(subObjectChild, resultBookmarks, notionPage);
					} else {
						saveOtherLink(subObjectChild, resultBookmarks, notionPage);
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

		return resultBookmarks;
	}

	// 노션 링크 데이터 종류 중 북마크와 임베드 형태를 저장한다.
	private void saveBookmarkOrEmbedLink(JSONObject bookmarkTypeObject, List<Bookmark> resultBookmarks,
		NotionPage notionPage) {
		JSONArray caption = (JSONArray)bookmarkTypeObject.get("caption");
		String url = (String)bookmarkTypeObject.get("url");
		String title = url;
		if (!caption.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < caption.size(); i++) {
				JSONObject captionObject = (JSONObject)caption.get(i);
				sb.append(captionObject.get("plain_text"));
				title = sb.toString().strip();
			}
		} else {
			title = getTitleName(url);
		}

		resultBookmarks.add(Bookmark.builder()
			.bookmarkLink(url)
			.bookmarkName(title.length() > 30 ? title.substring(0, 30) : title)
			.category(notionPage.getCategory())
			.build()
		);
	}

	// 북마크, 임베드 이외의 링크 데이터를 저장한다.
	private void saveOtherLink(JSONObject otherTypeObject, List<Bookmark> resultBookmarks, NotionPage notionPage) {
		JSONArray richTexts = (JSONArray)otherTypeObject.get("rich_text");
		if (Objects.isNull(richTexts))
			return;

		for (Object richText : richTexts) {
			String href = (String)((JSONObject)richText).get("href");
			String plainText = (String)((JSONObject)richText).get("plain_text");
			if (Objects.nonNull(href)) {
				if (href.equals(plainText)) {
					plainText = getTitleName(href);
				}

				resultBookmarks.add(Bookmark.builder()
					.bookmarkLink(href.startsWith("/") ? "https://www.notion.so" + href : href)
					.bookmarkName(plainText.length() > 30 ? plainText.substring(0, 30) : plainText)
					.category(notionPage.getCategory())
					.build());
			}
		}
	}

	private String getTitleName(String href) {
		try {
			Document document = getWebPage(href);
			return document.title();
		} catch (IOException e) {
			return href;
		}
	}

	private Document getWebPage(String href) throws IOException {
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
