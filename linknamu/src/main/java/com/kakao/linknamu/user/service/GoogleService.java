package com.kakao.linknamu.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception500;
import com.kakao.linknamu.user.UserExceptionStatus;
import com.kakao.linknamu.user.dto.oauth.GoogleTokenResponseDto;
import com.kakao.linknamu.user.dto.oauth.GoogleUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleService {

	@Value("${oauth2.google.redirect_uri}")
	private String redirectUri;
	@Value("${oauth2.google.client_secret}")
	private String clientSecret;
	@Value("${oauth2.google.client_id}")
	private String clientId;
	private static final String tokenUri = "https://oauth2.googleapis.com/token";
	private static final String GOOGLE_INFO_URI = "https://www.googleapis.com/oauth2/v2/userinfo";

	private final RestTemplate restTemplate;

	private final ObjectMapper om;

	public GoogleUserInfo getGoogleUserInfo(String token) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + token);
		HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

		try {
			ResponseEntity<GoogleUserInfo> responseEntity = restTemplate.exchange(
				GOOGLE_INFO_URI,
				HttpMethod.GET,
				httpEntity,
				GoogleUserInfo.class
			);
			return responseEntity.getBody();
		} catch (HttpClientErrorException exception) {
			if (exception.getStatusCode().value() == 401) {
				throw new Exception400(UserExceptionStatus.GOOGLE_TOKEN_INVALID);
			}
			log.error(exception.getMessage());
			throw new Exception500(UserExceptionStatus.GOOGLE_API_CONNECTION_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception500(UserExceptionStatus.GOOGLE_API_CONNECTION_ERROR);
		}
	}

	public String getGoogleAccessToken(String code) {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("grant_type", "authorization_code");
		parameters.add("client_id", clientId);
		parameters.add("redirect_uri", redirectUri);
		parameters.add("code", code);
		parameters.add("client_secret", clientSecret);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<?> httpRequestEntity = new HttpEntity<>(parameters, headers);
			ResponseEntity<GoogleTokenResponseDto> response = restTemplate.exchange(
				tokenUri,
				HttpMethod.POST,
				httpRequestEntity,
				GoogleTokenResponseDto.class
			);

			return response.getBody().accessToken();
		}catch (HttpClientErrorException e) {
			if (e.getStatusCode().value() == 400) {
				throw new Exception400(UserExceptionStatus.GOOGLE_CODE_INVALID);
			}
			log.error(e.getMessage());
			throw new Exception500(UserExceptionStatus.GOOGLE_API_CONNECTION_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception500(UserExceptionStatus.GOOGLE_API_CONNECTION_ERROR);
		}
	}
}
