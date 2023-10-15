package com.kakao.linknamu.notion.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Profile({"local"})
@RestController
@RequestMapping("/dev/notion")
@RequiredArgsConstructor
public class DevNotionController {

    @Value("${oauth2.notion.client_id}")
    private String OAUTH_CLIENT_ID;

    @Value("${oauth2.notion.secret}")
    private String OAUTH_CLIENT_SECRET;

    @Value("${oauth2.notion.auth_uri}")
    private String OAUTH_URL;

    private final RestTemplate restTemplate;

    private static final String TOKEN_URL = "https://api.notion.com/v1/oauth/token";

    @GetMapping("")
    public void notionLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect(OAUTH_URL);
    }

    @GetMapping("/redirect")
    public String notionRedirect(@RequestParam(value = "code") String code) {
        String authKey = Base64.getEncoder().encodeToString(
                (OAUTH_CLIENT_ID + ":" + OAUTH_CLIENT_SECRET).getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Basic %s", authKey));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", "https://localhost:8000/dev/notion/redirect");
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<NotionTokenDto> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, httpEntity, NotionTokenDto.class);
        return Objects.requireNonNull(response.getBody()).accessToken();
    }

    private record NotionTokenDto(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("token_type") String tokenType,
            @JsonProperty("bot_id") String botId,
            @JsonProperty("workspace_name") String workspaceName,
            @JsonProperty("workspace_icon") String workspaceIcon,
            @JsonProperty("workspace_id") String workspaceId,
            Owner owner,
            @JsonProperty("duplicated_template_id") String duplicatedTemplateId
            ) {

         record Owner(String type,
                      User user){
            record User(
                    String object,
                    String id
            ) {}
        }
    }
}
