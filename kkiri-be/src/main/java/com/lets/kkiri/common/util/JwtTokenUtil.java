package com.lets.kkiri.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * jwt 토큰 유틸 정의.
 */
@Component
public class JwtTokenUtil {
    private static String secretKey;

    public static Long atkExpirationTime;

    public static Long rtkExpirationTime;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String ISSUER = "KKIRI";

    @Autowired
    public JwtTokenUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration.atk}") Long atkExpirationTime, @Value("${jwt.expiration.rtk}") Long rtkExpirationTime) {
        JwtTokenUtil.secretKey = secretKey;
        JwtTokenUtil.atkExpirationTime = atkExpirationTime;
        JwtTokenUtil.rtkExpirationTime = rtkExpirationTime;
    }

    public static String getToken(long expiresToken, String kakaoId) {
        Date expires = JwtTokenUtil.getTokenExpiration(expiresToken);
        return Jwts.builder()
                .setSubject(kakaoId)
                .setExpiration(expires)
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }

    public static String getIdentifier(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();
    }

    public static Date getTokenExpiration(long expirationTime) {
        Date now = new Date();
        return new Date(now.getTime() + expirationTime);
    }

    public static Long getExpiration(String accessToken){
        Date expiration = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();

        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}