package com.kakao.linknamu._core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.linknamu._core.exception.Exception401;
import com.kakao.linknamu._core.exception.Exception403;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

public class FilterResponseUtils {
    private static final ObjectMapper om = new ObjectMapper();
    public static void unAuthorized(HttpServletResponse response, Exception401 e) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(e.status().value());
        response.getWriter().println(om.writeValueAsString(e.body()));
    }

    public static void forbidden(HttpServletResponse response, Exception403 e) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(e.status().value());
        response.getWriter().println(om.writeValueAsString(e.body()));
    }
}
