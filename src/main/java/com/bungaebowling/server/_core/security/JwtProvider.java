package com.bungaebowling.server._core.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bungaebowling.server._core.errors.exception.client.Exception401;
import com.bungaebowling.server.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class JwtProvider {
    public static final Long ACCESS_EXP_SECOND = 60L * 60 * 24 * 2; // 토큰 유효기간 2일 <- 개발용
    public static final Long REFRESH_EXP_SECOND = 60L * 60 * 24 * 30; // 30일
    public static final String TOKEN_PREFIX = "Bearer "; // 스페이스 필요함
    public static final String HEADER = "Authorization";
    public static final String TYPE_ACCESS = "access";
    public static final String TYPE_REFRESH = "refresh";
    public static final String TYPE_EMAIL_VERIFICATION = "email-verification";
    private static String SECRET;

    @Value("${bungaebowling.secret}")
    public void setKey(String value) {
        SECRET = value;
    }

    public static String createAccess(User user) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime expired = now.plusSeconds(ACCESS_EXP_SECOND);
        String jwt = JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("role", String.valueOf(user.getRole()))
                .withClaim("type", TYPE_ACCESS)
                .withExpiresAt(Timestamp.valueOf(expired))
                .sign(Algorithm.HMAC512(SECRET));
        return TOKEN_PREFIX + jwt;
    }

    public static String createRefresh(User user) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime expired = now.plusSeconds(REFRESH_EXP_SECOND);
        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("type", TYPE_REFRESH)
                .withExpiresAt(Timestamp.valueOf(expired))
                .sign(Algorithm.HMAC512(SECRET));
    }

    public static DecodedJWT verify(String jwt, String type) {
        try {
            DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC512(SECRET)).build()
                    .verify(jwt.replace(TOKEN_PREFIX, ""));
            
            if (!Objects.equals(decodedJwt.getClaim("type").asString(), type)) {
                throw new JWTVerificationException("토큰 검증 실패");
            }
            return decodedJwt;
        } catch (JWTVerificationException e) {
            throw new Exception401("토큰 검증 실패");
        }
    }
}
