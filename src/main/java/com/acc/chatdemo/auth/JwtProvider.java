package com.acc.chatdemo.auth;

import com.acc.chatdemo.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private static final String SECRET = "your-very-long-secret-key-that-must-be-at-least-32-characters-long-for-security";
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateAccessToken(User user) {
        long expirationTime = 60 * 60 * 24 * 7;
        return createToken(user.getId().toString(), expirationTime);
    }

    private String createToken(String subject, long expirationTime) {
        Claims claims = Jwts.claims();
        claims.put("username", subject);

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expirationTime);


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long parseUserIdFromAccessToken(String accessToken){
        Claims claims = parseClaims(accessToken);
        if (claims == null || claims.getSubject() == null) {
            throw new RuntimeException("Invalid access token");
        }
        return Long.valueOf(claims.getSubject());
    }

    public Claims parseClaims(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
            return claims;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
