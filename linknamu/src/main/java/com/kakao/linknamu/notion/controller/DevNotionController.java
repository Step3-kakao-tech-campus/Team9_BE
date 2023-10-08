package com.kakao.linknamu.notion.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Profile({"local"})
@RestController
@RequestMapping("/dev/notion")
@RequiredArgsConstructor
public class DevNotionController {

    private final static String OAUTH_CLIENT_ID = "";
    private final static String OAUTH_CLIENT_SECRET = "";
    private final static String OAUTH_URL = "";

    @GetMapping("")
    public void notionLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect(OAUTH_URL);
    }
}
