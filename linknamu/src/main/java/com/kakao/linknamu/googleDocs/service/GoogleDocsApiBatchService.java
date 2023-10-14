package com.kakao.linknamu.googleDocs.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.Document;
import com.google.api.services.docs.v1.model.ParagraphElement;
import com.google.api.services.docs.v1.model.StructuralElement;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkCreateService;
import com.kakao.linknamu.googleDocs.entity.GooglePage;
import com.kakao.linknamu.googleDocs.util.InvalidGoogleDocsApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleDocsApiBatchService {

    // Application Name
    private static final String APPLICATION_NAME = "Google Docs API Java Quickstart";
    // Json Factory
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
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private final GoogleDocsApiGetService googleDocsApiGetService;
    private final BookmarkCreateService bookmarkCreateService;

    @Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
    public void googleDocsApiCronJob() {
        // 활성화된 페이지들을 리스트에 저장
        List<GooglePage> activeGoogleDocsPages = googleDocsApiGetService.getActiveGoogleDocsPage();

        // 활성화된 구글 독스 페이지들에 대해 배치를 실행한다.
        activeGoogleDocsPages.forEach((GooglePage gp) -> {
            try{
                List<Bookmark> resultBookmarks = getLinks(gp);
                bookmarkCreateService.bookmarkBatchInsert(resultBookmarks);
            } catch (InvalidGoogleDocsApiException e) {
                gp.deactivate();
            }
        });
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // credentials.json 가져오기
        InputStream in = GoogleDocsApiBatchService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // flow생성 및 유저 인증 요청 처리
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        // Credential 객체 반환
        return credential;
    }

    private List<Bookmark> getLinks(GooglePage googlePage) {
        List<Bookmark> resultBookmarks = new ArrayList<>();
        try {
            // 서비스 생성
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Docs service = new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // google docs 객체 생성 및 get API를 사용해서 link 항목 불러오기
            Document response = service.documents().get(googlePage.getDocumentId()).execute();
            List<StructuralElement> contents = response.getBody().getContent();
            for (StructuralElement e : contents) {
                if (e.getParagraph() != null && e.getParagraph().getElements() != null) {
                    List<ParagraphElement> elements = e.getParagraph().getElements();
                    for (ParagraphElement pe : elements) {
                        if (pe.getTextRun() != null && pe.getTextRun().getTextStyle() != null
                                && pe.getTextRun().getTextStyle().getLink() != null) {
                            String link = pe.getTextRun().getTextStyle().getLink().getUrl();
                            if (link != null) {
                                // 링크 항목이 있을 경우 해당 링크의 사이트 제목을 Jsoup 라이브러리를 사용해 받아온다.
                                // 이를 생성할 북마크의 이름으로 사용한다.
                                String bookmarkName = Jsoup.connect(link).get().title();
                                resultBookmarks.add(Bookmark.builder()
                                        .bookmarkLink(link)
                                        .bookmarkName(bookmarkName)
                                        .category(googlePage.getCategory())
                                        .build());
                                }
                            }
                        }
                    }
                }
        } catch(GeneralSecurityException gse) {
            log.error("구글 인증에 문제가 있습니다.");
            throw new InvalidGoogleDocsApiException();
        } catch(IOException ioe) {
            log.error("파싱된 사이트의 이름을 불러올 수 없습니다.");
        }
        return resultBookmarks;
    }
}
