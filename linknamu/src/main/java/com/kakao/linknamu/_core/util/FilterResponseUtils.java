package com.kakao.linknamu._core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class FilterResponseUtils {
    public static void unAuthorized(HttpServletResponse response, RuntimeException e) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(e.getMessage());
        response.getWriter().println(responseBody);
    }

    public static void forbidden(HttpServletResponse response, RuntimeException e) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(e.getMessage());
        response.getWriter().println(responseBody);
    }
}
