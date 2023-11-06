package com.kakao.linknamu.user.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

/**
 * 개발용으로 사용하는 웹 소셜 로그인 컨트롤러,
 * 개발 테스트용이기에 구조를 신경쓰진 않았습니다.
 */
@Profile("local")
@RequiredArgsConstructor
@RestController
@RequestMapping("/dev")
public class DevSocialLoginController {


	@Value("${oauth2.google.client_id}")
	private String clientId;

	@Value("${oauth2.google.auth_uri}")
	private String authUri;

	@Value("${oauth2.google.token_uri}")
	private String tokenUri;

	@Value("${oauth2.google.client_secret}")
	private String clientSecret;

	@Value("${oauth2.google.redirect_uri}")
	private String redirectUri;

	private final ObjectMapper om;
	private final RestTemplate restTemplate;

	@GetMapping("/google/login")
	public void loginGoogle(HttpServletResponse response) throws IOException {
		String uri = String.format(authUri
				+ "?client_id=%s&response_type=code&redirect_uri=%s&scope=https://www.googleapis.com/auth/userinfo.email",
			clientId, redirectUri);
		response.sendRedirect(uri);
	}

	@GetMapping("/google/login/redirect")
	public String redirectGoogleLogin(
		@RequestParam("code") String code,
		@RequestParam(value = "error", required = false) Optional<String> error
	) {

		// 로그인 요청 에러 발생 시 error 쿼리 스트링 값이 존재.
		if (error.isPresent()) {
			return error.get();
		}
		return code;
//		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
//		parameters.add("grant_type", "authorization_code");
//		parameters.add("client_id", clientId);
//		parameters.add("redirect_uri", redirectUri);
//		parameters.add("code", code);
//		parameters.add("client_secret", clientSecret);
//
//		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			HttpEntity<?> httpRequestEntity = new HttpEntity<>(parameters, headers);
//			ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, httpRequestEntity, String.class);
//			GoogleTokenResponseDto responseDto = om.readValue(response.getBody(), GoogleTokenResponseDto.class);
//			return responseDto.accessToken();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		return "failed";
	}

	private record GoogleTokenResponseDto(
		@JsonProperty("access_token") String accessToken,
		@JsonProperty("expires_in") Integer expiresIn,
		@JsonProperty("token_type") String tokenType,
		@JsonProperty("scope") String scope,
		@JsonProperty("refresh_token") String refreshToken
	) {
	}
}
