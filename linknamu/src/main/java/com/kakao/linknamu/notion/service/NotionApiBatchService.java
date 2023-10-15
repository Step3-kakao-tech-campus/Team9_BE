package com.kakao.linknamu.notion.service;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkCreateService;
import com.kakao.linknamu.notion.entity.NotionPage;
import com.kakao.linknamu.notion.util.InvalidNotionApiException;
import com.kakao.linknamu.notion.util.NotionApiUriBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.parser.ParserException;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotionApiBatchService {
    private final RestTemplate restTemplate;
    private final ObjectProvider<JSONParser> jsonParserProvider;
    private final NotionApiUriBuilder notionApiUriBuilder;
    private final NotionApiGetService notionApiGetService;
    private final BookmarkCreateService bookmarkCreateService;

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
                bookmarkCreateService.bookmarkBatchInsert(resultBookmarks);
            } catch (InvalidNotionApiException e) {
                // 2-2 NotionPage 접근 권한, 없는 페이지라면 isActive를 false로 만들고 종료
                n.deactivate();
            }
        });
    }

    private List<Bookmark> getPageLinks(String pageId, String accessToken, NotionPage notionPage) {
        Boolean hasMore = true;
        String nextCursor = null;
        List<Bookmark> resultBookmarks = new ArrayList<>();
        JSONParser jsonParser = jsonParserProvider.getObject();

        while(hasMore) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", accessToken);
            headers.set("Notion-Version", NOTION_VERSION);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            String uri = notionApiUriBuilder.getBlockUri(pageId, Optional.ofNullable(nextCursor));
            try{
                ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
                JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
                JSONArray result = (JSONArray) jsonObject.get("results");
                hasMore = (Boolean) jsonObject.get("has_more");
                nextCursor = (String) jsonObject.get("next_cursor");

                for (Object o : result) {
                    JSONObject subObject = (JSONObject) o;
                    String type = (String) subObject.get("type");
                    JSONObject subObject2 = (JSONObject) subObject.get(type);
                    JSONArray richTexts = (JSONArray) subObject2.get("rich_text");
                    if (Objects.isNull(richTexts)) continue;

                    for (Object richText : richTexts) {
                        String href = (String) ((JSONObject) richText).get("href");
                        String plainText = (String) ((JSONObject) richText).get("plain_text");
                        if (Objects.nonNull(href)) {
                            resultBookmarks.add(Bookmark.builder()
                                            .bookmarkLink(href.startsWith("/") ? "https://www.notion.so" + href : href)
                                            .bookmarkName(plainText.length() > 100 ? plainText.substring(0, 100) : plainText)
                                            .category(notionPage.getCategory())
                                            .build());
                        }
                    }
                }

//                Thread.sleep(1000); // -> 다음 호출이 필요할 땐 1초 쉬어준다.(멀티 쓰레드 환경에서의 Rate_limit 대비)
            } catch(HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    log.error("유효하지 않은 토큰");
                    throw new InvalidNotionApiException();
                }else if(e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                    log.error("요청 한도 초과");
                }else if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                    log.error("잘못된 Page Id");
                    throw new InvalidNotionApiException();
                }else {
                    log.error(e.getMessage());
                }
            } catch(ParserException e) {
                log.error("올바르지 않은 json parser : " + e.getMessage());
            } catch(Exception e) {
                log.error(e.getMessage());
            }
        }


        return resultBookmarks;
    }
}
