package com.lets.kkiri.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;

import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


import static com.google.common.collect.Lists.newArrayList;

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
        this.secretKey = secretKey;
        this.atkExpirationTime = atkExpirationTime;
        this.rtkExpirationTime = rtkExpirationTime;
    }


    public void setExpirationTime() {
        JwtTokenUtil.rtkExpirationTime = rtkExpirationTime;
    }

    public static JWTVerifier getVerifier() {
        return JWT
                .require(Algorithm.HMAC512(secretKey.getBytes()))
                .withIssuer(ISSUER)
                .build();
    }

    public static String getToken(String kakaoId) {
//        int expirationTime = (type.equals("access-token")) ? atkExpirationTime : rtkExpirationTime;

        Date expires = JwtTokenUtil.getTokenExpiration(rtkExpirationTime);
        return JWT.create()
                .withSubject(kakaoId)
                .withExpiresAt(expires)
                .withIssuer(ISSUER)
                .withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC512(secretKey.getBytes()));
    }

    public static String getToken(long expiresToken, String kakaoId) {
        Date expires = JwtTokenUtil.getTokenExpiration(expiresToken);
        return JWT.create()
                .withSubject(kakaoId)
                .withExpiresAt(expires)
                .withIssuer(ISSUER)
                .withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC512(secretKey.getBytes()));
    }

    public static String getIdentifier(String token){
        handleError(token);
        return getVerifier().verify(token.replace(TOKEN_PREFIX, "")).getSubject();
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

    public static void handleError(String token) {
        JWTVerifier verifier = JWT
                .require(Algorithm.HMAC512(secretKey.getBytes()))
                .withIssuer(ISSUER)
                .build();

        try {
            verifier.verify(token.replace(TOKEN_PREFIX, ""));
        } catch (AlgorithmMismatchException ex) {
            throw new KkiriException(ErrorCode.JWT_ALGORITHM_NOT_SUPPORTED);
        } catch (InvalidClaimException ex) {
            // INVALID_CLAIM
            throw new KkiriException(ErrorCode.JWT_INVALID_CLAIM);
        } catch (SignatureGenerationException ex) {
            // JWT_SIGNATURE_GENERATION_FAILED
            throw new KkiriException(ErrorCode.JWT_SIGNATURE_VERIFICATION_FAILED);
        } catch (SignatureVerificationException ex) {
            // JWT_SIGNATURE_VERIFICATION_FAILED
            throw new KkiriException(ErrorCode.JWT_SIGNATURE_VERIFICATION_FAILED);
        } catch (TokenExpiredException ex) {
            // JWT_TOKEN_EXPIRED
            throw new KkiriException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (JWTCreationException ex) {
            // JWT_CREATION_FAILED
            throw new KkiriException(ErrorCode.JWT_CREATION_FAILED);
        } catch (JWTDecodeException ex) {
            // JWT_DECODE_FAILED
            throw new KkiriException(ErrorCode.JWT_DECODE_FAILED);
        } catch (JWTVerificationException ex) {
            // JWT_VERIFICATION_FAILED
            throw new KkiriException(ErrorCode.JWT_VERIFICATION_FAILED);
        } catch (Exception ex) {
            // JWT_VERIFICATION_FAILED
            throw new KkiriException(ErrorCode.JWT_VERIFICATION_FAILED);
        }
    }

    public static void handleError(JWTVerifier verifier, String token) {
        try {
            verifier.verify(token.replace(TOKEN_PREFIX, ""));
        } catch (AlgorithmMismatchException ex) {
            throw new KkiriException(ErrorCode.JWT_ALGORITHM_NOT_SUPPORTED);
        } catch (InvalidClaimException ex) {
            // INVALID_CLAIM
            throw new KkiriException(ErrorCode.JWT_INVALID_CLAIM);
        } catch (SignatureGenerationException ex) {
            // JWT_SIGNATURE_GENERATION_FAILED
            throw new KkiriException(ErrorCode.JWT_SIGNATURE_VERIFICATION_FAILED);
        } catch (SignatureVerificationException ex) {
            // JWT_SIGNATURE_VERIFICATION_FAILED
            throw new KkiriException(ErrorCode.JWT_SIGNATURE_VERIFICATION_FAILED);
        } catch (TokenExpiredException ex) {
            // JWT_TOKEN_EXPIRED
            throw new KkiriException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (JWTCreationException ex) {
            // JWT_CREATION_FAILED
            throw new KkiriException(ErrorCode.JWT_CREATION_FAILED);
        } catch (JWTDecodeException ex) {
            // JWT_DECODE_FAILED
            throw new KkiriException(ErrorCode.JWT_DECODE_FAILED);
        } catch (JWTVerificationException ex) {
            // JWT_VERIFICATION_FAILED
            throw new KkiriException(ErrorCode.JWT_VERIFICATION_FAILED);
        } catch (Exception ex) {
            // JWT_VERIFICATION_FAILED
            throw new KkiriException(ErrorCode.JWT_VERIFICATION_FAILED);
        }
    }
}