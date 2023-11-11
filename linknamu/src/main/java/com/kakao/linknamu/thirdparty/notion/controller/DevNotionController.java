package com.kakao.linknamu.thirdparty.notion.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Profile({"local"})
@RestController
@RequestMapping("/dev/notion")
@RequiredArgsConstructor
public class DevNotionController {

	@Value("${oauth2.notion.client_id}")
	private String oauthClientId;

	@Value("${oauth2.notion.secret}")
	private String oauthClientSecret;

	@Value("${oauth2.notion.auth_uri}")
	private String oauthUrl;

	private final RestTemplate restTemplate;

	private static final String TOKEN_URL = "https://api.notion.com/v1/oauth/token";

	@GetMapping("")
	public void loginNotion(HttpServletResponse response) throws IOException {
		response.sendRedirect(oauthUrl);
	}

	@GetMapping("/redirect")
	public String redirectNotion(@RequestParam(value = "code") String code) {
		return code;
	}
}
