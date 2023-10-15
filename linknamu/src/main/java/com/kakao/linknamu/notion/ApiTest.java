package com.kakao.linknamu.notion;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class ApiTest {
    private final static JSONParser jsonParser = new JSONParser();
    private final static RestTemplate restTemplate = new RestTemplate();
    private final static String NOTION_API_KEY = "Bearer ";
    private final static String NOTION_VERSION = "2022-06-28";
    private final static String URI = "https://api.notion.com/v1/blocks/";
    private final static int PAGE_SIZE = 100;

    public static void main(String[] args) throws ParseException {
        Boolean hasMore = false;
//        String id = "34dab791d3644da28508a6e5b7bd9467";
        String id = "6f22ca3d24934b8ea5dfbaed710e5c50";
        do{
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", NOTION_API_KEY);
            headers.set("Notion-Version", NOTION_VERSION);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            String uri = uriBuilder(id, Optional.empty());
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
            JSONArray result = (JSONArray) jsonObject.get("results");
            hasMore = (Boolean) jsonObject.get("has_more");
            id = (String) jsonObject.get("next_cursor");

            for (int i = 0; i < result.size(); i++) {
                JSONObject subObject = (JSONObject) result.get(i);
                String type = (String) subObject.get("type");
                JSONObject subObject2 = (JSONObject) subObject.get(type);
                JSONArray richTexts = (JSONArray) subObject2.get("rich_text");
                if (Objects.isNull(richTexts)) continue;

                for (int j = 0; j < richTexts.size(); j++) {
                    String href = (String) ((JSONObject) richTexts.get(j)).get("href");

                    if (Objects.nonNull(href)) {
                        System.out.println(href);
                    }
                }
            }
        } while(hasMore);
    }

    private static String uriBuilder(String id, Optional<String> page) {
        StringBuilder sb = new StringBuilder();
        if(page.isEmpty()) {
            sb.append(URI)
                    .append(id)
                    .append("/children?")
                    .append(String.format("page_size=%d", PAGE_SIZE));
        } else{
            sb.append(URI)
                    .append(id)
                    .append("/children?")
                    .append(String.format("page_size=%d", PAGE_SIZE))
                    .append(String.format("start_cursor=%s", page.get()));
        }
        return sb.toString();
    }
}
