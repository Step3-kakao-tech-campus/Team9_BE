package com.kakao.linknamu._core.security;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// jwt 토큰관련 예외처리를 위한 클래스
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException | JWTCreationException e) {
            setJwtExceptionResponse(request, response, e);
        }
    }

    private void setJwtExceptionResponse(HttpServletRequest request, HttpServletResponse response, Throwable exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper om = new ObjectMapper();
        response.getOutputStream().write(om.writeValueAsBytes(exception.getMessage()));
        String responseBody = om.writeValueAsString(exception.getMessage());
        response.getWriter().println(responseBody);
    }
}
