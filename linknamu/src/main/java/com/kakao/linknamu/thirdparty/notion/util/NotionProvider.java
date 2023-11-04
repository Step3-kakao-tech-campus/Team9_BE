package com.kakao.linknamu.thirdparty.notion.util;

import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception500;
import com.kakao.linknamu.thirdparty.notion.NotionExceptionStatus;
import com.kakao.linknamu.thirdparty.notion.dto.NotionTokenDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotionProvider {

    @Value("${oauth2.notion.client_id:null}")
    private String NOTION_CLIENT_ID;
    @Value("${oauth2.notion.secret:null}")
    private String NOTION_CLIENT_SECRET;
    @Value("${oauth2.notion.redirect_uri:null}")
    private String REDIRECT_URI;
    private static final String TOKEN_URL = "https://api.notion.com/v1/oauth/token";
    private static final String PAGE_URL = "https://api.notion.com/v1/pages/";
    private final static String NOTION_VERSION = "2022-06-28";

    private final RestTemplate restTemplate;
    private final ObjectProvider<JSONParser> jsonParserProvider;

    public String getAccessToken(String code) {
        String authKey = Base64.getEncoder().encodeToString(
                (NOTION_CLIENT_ID + ":" + NOTION_CLIENT_SECRET).getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Basic %s", authKey));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", REDIRECT_URI);
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<NotionTokenDto> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, httpEntity, NotionTokenDto.class);
            return Objects.requireNonNull(response.getBody()).accessToken();
        } catch (HttpClientErrorException e) {
            if(e.getStatusCode().is4xxClientError())
                throw new Exception400(NotionExceptionStatus.INVALID_NOTION_CODE);

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
            ResponseEntity<String> response = restTemplate.exchange(PAGE_URL + pageId, HttpMethod.GET, httpEntity, String.class);
            JSONParser jsonParser = jsonParserProvider.getObject();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
            JSONObject properties = (JSONObject) jsonObject.get("properties");
            JSONObject titleObject = (JSONObject) properties.get("title");
            JSONArray titleArray = (JSONArray) titleObject.get("title");
            StringBuilder titleBuilder = new StringBuilder();
            for (Object o : titleArray) {
                JSONObject titleInfo = (JSONObject) o;
                titleBuilder.append((String) titleInfo.get("plain_text"));
            }
            return titleBuilder.toString();
         } catch(HttpClientErrorException e) {
            if(e.getStatusCode().value() == 400) {
                throw new Exception400(NotionExceptionStatus.INVALID_NOTION_CODE);
            }else if(e.getStatusCode().value() == 404) {
                throw new Exception400(NotionExceptionStatus.INVALID_NOTION_PAGE_AND_AUTHORIZATION);
            }
            log.error(e.getMessage());
            throw new Exception500(NotionExceptionStatus.NOTION_LINK_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception500(NotionExceptionStatus.NOTION_LINK_ERROR);
        }
    }
}
