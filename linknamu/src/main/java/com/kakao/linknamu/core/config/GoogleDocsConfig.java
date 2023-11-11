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
	@Getter
	private static final String APPLICATION_NAME = "Google Docs API Java Quickstart";
	@Getter
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens/googledocs";
	private static final List<String> SCOPES =
		Collections.singletonList(DocsScopes.DOCUMENTS_READONLY);
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

		// Credential 객체 반환
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}
}
