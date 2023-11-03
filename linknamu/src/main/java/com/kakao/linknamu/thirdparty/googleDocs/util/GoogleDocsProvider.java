package com.kakao.linknamu.thirdparty.googleDocs.util;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.Document;
import com.kakao.linknamu.core.config.GoogleDocsConfig;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.core.exception.Exception500;
import com.kakao.linknamu.thirdparty.googleDocs.GoogleDocsExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.kakao.linknamu.core.config.GoogleDocsConfig.getCredentials;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleDocsProvider {

	public String getGoogleDocsTitle(String documentId) {
		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			Docs service = new Docs.Builder(HTTP_TRANSPORT, GoogleDocsConfig.getJSON_FACTORY(), getCredentials(HTTP_TRANSPORT))
				.setApplicationName(GoogleDocsConfig.getAPPLICATION_NAME())
				.build();

			// google docs 객체 생성 및 get API를 사용해서 link 항목 불러오기
			Document response = service.documents().get(documentId).execute();
          
			return response.getTitle();
		} catch (GeneralSecurityException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		} catch (GoogleJsonResponseException e) {
			if (e.getStatusCode() == 404) {
				throw new Exception404(GoogleDocsExceptionStatus.GOOGLE_DOCS_NOT_EXIST);
			} else if (e.getStatusCode() == 403) {
				throw new Exception403(GoogleDocsExceptionStatus.GOOGLE_DOCS_NOT_ACCESS);
			}
			log.error(e.getMessage());
			throw new Exception500(GoogleDocsExceptionStatus.GOOGLE_DOCS_LINK_ERROR);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new Exception500(GoogleDocsExceptionStatus.GOOGLE_DOCS_LINK_ERROR);
		}
	}
}
