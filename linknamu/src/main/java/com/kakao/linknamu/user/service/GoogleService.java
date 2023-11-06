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

	private static final String GOOGLE_INFO_URI = "https://www.googleapis.com/oauth2/v2/userinfo";

	private final RestTemplate restTemplate;


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
}
