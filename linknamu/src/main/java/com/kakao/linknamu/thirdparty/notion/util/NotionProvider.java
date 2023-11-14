package com.kakao.linknamu.thirdparty.notion.util;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkService;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception500;
import com.kakao.linknamu.core.util.JsoupResult;
import com.kakao.linknamu.core.util.JsoupUtils;
import com.kakao.linknamu.thirdparty.notion.NotionExceptionStatus;
import com.kakao.linknamu.thirdparty.notion.dto.NotionTokenDto;
import com.kakao.linknamu.thirdparty.notion.entity.NotionPage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.parser.ParserException;

import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotionProvider {

	@Value("${oauth2.notion.client_id:null}")
	private String notionClientId;
	@Value("${oauth2.notion.secret:null}")
	private String notionClientSecret;
	@Value("${oauth2.notion.redirect_uri:null}")
	private String redirectUri;
	private static final String TOKEN_URL = "https://api.notion.com/v1/oauth/token";
	private static final String PAGE_URL = "https://api.notion.com/v1/pages/";
	private static final String NOTION_VERSION = "2022-06-28";
	// 멘션 타입의 노션 페이지 링크일 시, Untitled로 나와 이를 기본 설정으로 변경해서 저장한다.
	private static final String DEFAULT_NOTION_PAGE_NAME = "Notion Page";
	private static final String DEFAULT_NOTION_IMAGE = "https://www.notion.so/images/meta/default.png";

	private final RestTemplate restTemplate;
	private final ObjectProvider<JSONParser> jsonParserProvider;
	private final BookmarkService bookmarkService;
	private final NotionApiUriBuilder notionApiUriBuilder;
	private final JsoupUtils jsoupUtils;

	public String getAccessToken(String code) {
		String authKey = Base64.getEncoder().encodeToString(
			(notionClientId + ":" + notionClientSecret).getBytes());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", String.format("Basic %s", authKey));

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("code", code);
		body.add("grant_type", "authorization_code");
		body.add("redirect_uri", redirectUri);
		HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
		try {
			ResponseEntity<NotionTokenDto> response =
				restTemplate.exchange(TOKEN_URL, HttpMethod.POST, httpEntity, NotionTokenDto.class);
			return Objects.requireNonNull(response.getBody()).accessToken();
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().is4xxClientError()) {
				throw new Exception400(NotionExceptionStatus.INVALID_NOTION_CODE);
			}

			log.error(e.getMessage());
			throw new Exception500(NotionExceptionStatus.NOTION_LINK_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception500(NotionExceptionStatus.NOTION_LINK_ERROR);
		}
	}

	public String getPageTitle(String accessToken, String pageId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", accessToken);
		headers.set("Notion-Version", NOTION_VERSION);
		HttpEntity<?> httpEntity = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> response =
				restTemplate.exchange(PAGE_URL + pageId, HttpMethod.GET, httpEntity, String.class);
			JSONParser jsonParser = jsonParserProvider.getObject();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(response.getBody());
			JSONObject properties = (JSONObject)jsonObject.get("properties");
			JSONObject titleObject = (JSONObject)properties.get("title");
			JSONArray titleArray = (JSONArray)titleObject.get("title");
			StringBuilder titleBuilder = new StringBuilder();
			for (Object o : titleArray) {
				JSONObject titleInfo = (JSONObject)o;
				titleBuilder.append((String)titleInfo.get("plain_text"));
			}
			return titleBuilder.toString();
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().value() == 400) {
				throw new Exception400(NotionExceptionStatus.INVALID_NOTION_CODE);
			} else if (e.getStatusCode().value() == 404) {
				throw new Exception400(NotionExceptionStatus.INVALID_NOTION_PAGE_AND_AUTHORIZATION);
			}
			log.error(e.getMessage());
			throw new Exception500(NotionExceptionStatus.NOTION_LINK_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception500(NotionExceptionStatus.NOTION_LINK_ERROR);
		}
	}


	public List<Bookmark> getPageLinks(String pageId, String accessToken, Long categoryId) {
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
				JSONObject jsonObject = (JSONObject)jsonParser.parse(response.getBody());
				JSONArray result = (JSONArray)jsonObject.get("results");
				hasMore = (Boolean)jsonObject.get("has_more");
				nextCursor = (String)jsonObject.get("next_cursor");

				for (Object o : result) {
					JSONObject subObject = (JSONObject)o;
					String type = (String)subObject.get("type");
					JSONObject subObjectChild = (JSONObject)subObject.get(type);
					if (type.equals("bookmark") | type.equals("embed")) {
						saveBookmarkOrEmbedLink(subObjectChild, resultBookmarks, categoryId);
					} else {
						saveOtherLink(subObjectChild, resultBookmarks, categoryId);
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
		Set<Bookmark> resultBookmarks, Long categoryId) {
		JSONArray caption = (JSONArray)bookmarkTypeObject.get("caption");
		String url = (String)bookmarkTypeObject.get("url");
		// 만약 한번 연동한 링크라면 더 이상 진행하지 않는다.
		if (bookmarkService.existByBookmarkLinkAndCategoryId(url, categoryId)) {
			return;
		}

		JsoupResult jsoupResult = jsoupUtils.getTitleAndImgUrl(url);
		if (!caption.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < caption.size(); i++) {
				JSONObject captionObject = (JSONObject)caption.get(i);
				sb.append(captionObject.get("plain_text"));
			}
			jsoupResult.setTitle(sb.toString().strip());
		}

		resultBookmarks.add(Bookmark.builder()
			.bookmarkLink(url)
			.bookmarkName(jsoupResult.getTitle())
			.category(Category
				.builder()
				.categoryId(categoryId)
				.build()
			)
			.bookmarkThumbnail(jsoupResult.getImageUrl())
			.build()
		);
	}

	// 북마크, 임베드 이외의 링크 데이터를 저장한다.
	private void saveOtherLink(JSONObject otherTypeObject, Set<Bookmark> resultBookmarks, Long categoryId) {
		JSONArray richTexts = (JSONArray)otherTypeObject.get("rich_text");
		if (Objects.isNull(richTexts)) {
			return;
		}

		for (Object richText : richTexts) {
			String href = (String)((JSONObject)richText).get("href");
			String title = (String)((JSONObject)richText).get("plain_text");
			String type = (String)((JSONObject)richText).get("type");
			JsoupResult jsoupResult = new JsoupResult();

			if (Objects.nonNull(href)) {
				// 만약 한번 연동한 링크라면 더 이상 진행하지 않는다.
				if (bookmarkService.existByBookmarkLinkAndCategoryId(href, categoryId)) {
					continue;
				}

				jsoupResult = jsoupUtils.getTitleAndImgUrl(href);

				if (!href.equals(title)) {
					jsoupResult.setTitle(title);
				}

				if (type.equals("mention")) {
					if (title.equals("Untitled")) {
						jsoupResult.setTitle(DEFAULT_NOTION_PAGE_NAME);
					} else {
						jsoupResult.setTitle(title);
						jsoupResult.setImageUrl(DEFAULT_NOTION_IMAGE);
					}
				}

				resultBookmarks.add(Bookmark.builder()
					.bookmarkLink(href.startsWith("/") ? "https://www.notion.so" + href : href)
					.bookmarkName(jsoupResult.getTitle())
					.bookmarkThumbnail(jsoupResult.getImageUrl())
					.category(Category
						.builder()
						.categoryId(categoryId)
						.build()
					)
					.build());
			}
		}
	}
}
