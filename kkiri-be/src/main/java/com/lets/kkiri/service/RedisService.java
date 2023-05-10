package com.lets.kkiri.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class RedisService {

    private final RedisTemplate redisTemplate;

    @Value("${jwt.expiration.rtk}")
    private Long EXPIRED_MINUTES; // 로그인 유효 시간

    public void setValues(String token, String kakaoId){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(kakaoId, token, Duration.ofMinutes(EXPIRED_MINUTES));
    }

    public void setBlackList(String token, String email, Long expiration){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(email, token, Duration.ofMillis(expiration));
    }

    public String getValues(String kakaoId){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String refreshToken = values.get(kakaoId);
        return ( refreshToken == null )? "" : refreshToken;
    }

    public void deleteValues(String token){
        redisTemplate.delete(token);
    }
}