package com.kakao.linknamu.user.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception500;
import com.kakao.linknamu.user.UserExceptionStatus;
import com.kakao.linknamu.user.dto.oauth.GoogleUserInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleService {
	private final String GOOGLE_INFO_URI = "https://www.googleapis.com/oauth2/v2/userinfo";

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
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().value() == 401)
				throw new Exception400(UserExceptionStatus.GOOGLE_TOKEN_INVALID);

			log.error(e.getMessage());
			throw new Exception500(UserExceptionStatus.GOOGLE_API_CONNECTION_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception500(UserExceptionStatus.GOOGLE_API_CONNECTION_ERROR);
		}
	}
}
