package com.lets.kkiri.service;

import com.lets.kkiri.common.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final RedisService redisService;

    public void logout(String accessToken) {
        String kakaoId = JwtTokenUtil.getIdentifier(accessToken);

        if(redisService.getValues(kakaoId) != null) redisService.deleteValues(kakaoId);

        /*
         * redis에
         *   KEY: kakaoId
         *   VALUE: "logout" 으로 저장하여, 이전 구한 토큰의 유효시간동안 해당 엑세스토큰을 사용하지 못하게 처리
         */
        redisService.setBlackList(kakaoId, JwtTokenUtil.getExpiration(accessToken));
    }
}
