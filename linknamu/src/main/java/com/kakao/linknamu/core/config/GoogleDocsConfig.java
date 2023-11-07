package com.kakao.linknamu.core.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.docs.v1.DocsScopes;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.Collections;
import java.util.List;

@Configuration
@Getter
public class GoogleDocsConfig {
	// Application Name
	@Getter
	private static final String APPLICATION_NAME = "Google Docs API Java Quickstart";
	// Json Factory
	@Getter
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	// google credential.json을 통해 생성한 클라이언트 비밀키(구글 인증 토큰)를 저장할 위치 지정
	private static final String TOKENS_DIRECTORY_PATH = "tokens/googledocs";
	/*
	 * Global instance of the scopes required by this quickstart.
	 * If modifying these scopes, delete your previously saved tokens/ folder.
	 */
	private static final List<String> SCOPES =
		Collections.singletonList(DocsScopes.DOCUMENTS_READONLY);
	// OAuth 2.0 credentials.json 경로 지정
	private static final String CREDENTIALS_FILE_PATH = "./src/main/resources/credentials.json";

	public static Credential getCredentials(final NetHttpTransport httpTransport)
		throws IOException {
		// credentials.json 가져오기
		InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets =
			GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// flow생성 및 유저 인증 요청 처리
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
			httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
			.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
			.setAccessType("offline")
			.build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

		// Credential 객체 반환
		return credential;
	}
}
