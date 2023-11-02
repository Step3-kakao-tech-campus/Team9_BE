package com.kakao.linknamu.core.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kakao.linknamu.user.entity.User;
import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    public static final Long ACCESS_EXP = 1000L * 60 * 30; // 30분
    public static final Long REFRESH_EXP = 1000L * 60 * 60 * 24 * 14; // 2주
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
    public static String ACCESS_SECRET;
    public static String REFRESH_SECRET;

    @Value("${access-jwt-secret-key}")
    public void setACCESS_SECRET(String value) {
        ACCESS_SECRET = value;
    }

    @Value("${refresh-jwt-secret-key}")
    public void setREFRESH_SECRET(String value) {
        REFRESH_SECRET = value;
    }

    public static String create(User user) {
        String jwt = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .withClaim("id", user.getUserId())
                .withClaim("role", user.getRole().name())
                .sign(Algorithm.HMAC512(ACCESS_SECRET));
        return TOKEN_PREFIX + jwt;
    }

    public static String createRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .withClaim("id", user.getUserId())
                .withClaim("role", user.getRole().name())
                .sign(Algorithm.HMAC512(REFRESH_SECRET));
    }

    public static DecodedJWT verify(String jwt) throws SignatureVerificationException, TokenExpiredException {
        jwt = jwt.replace(JwtProvider.TOKEN_PREFIX, "");
        return JWT.require(Algorithm.HMAC512(ACCESS_SECRET))
                .build().verify(jwt);
    }

    public static DecodedJWT verifyRefreshToken(String jwt) throws SignatureVerificationException, TokenExpiredException {
        return JWT.require(Algorithm.HMAC512(REFRESH_SECRET))
                .build().verify(jwt);
    }

    public static Long getRemainExpiration(String jwt) {
        DecodedJWT decodedJWT = verify(jwt);
        Date now = new Date();
        Date end = decodedJWT.getExpiresAt();
        return end.getTime() - now.getTime();
    }
}
