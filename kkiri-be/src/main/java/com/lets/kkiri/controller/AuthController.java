package com.lets.kkiri.controller;

import com.lets.kkiri.config.jwt.JwtTokenUtil;
import com.lets.kkiri.dto.member.KakaoUserPostDto;
import com.lets.kkiri.dto.member.MemberTokenRes;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.service.AuthService;
import com.lets.kkiri.service.MemberService;
import com.lets.kkiri.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@EnableWebSecurity
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Value("${jwt.expiration.atk}")
    Integer atkExpirationTime;

    @Value("${jwt.expiration.rtk}")
    Integer rtkExpirationTime;
    private final MemberService memberService;
    private final RedisService redisService;

    @PostMapping("/login")
    public ResponseEntity getKakaoCode(@RequestBody KakaoUserPostDto request) {
        Member member = memberService.getMemberByKakaoUserInfo(request);
        // 로그인 처리
        String accessToken = JwtTokenUtil.getToken(JwtTokenUtil.atkExpirationTime, member.getKakaoId());
        String refreshToken = JwtTokenUtil.getToken(JwtTokenUtil.rtkExpirationTime, member.getKakaoId());

        redisService.setValues(refreshToken, member.getKakaoId());

        List<String> deviceTokens = memberService.getDeviceTokensByMemberId(member.getId());

        return ResponseEntity.ok().body(
                MemberTokenRes.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .deviceTokens(deviceTokens)
                        .build()
        );
    }
}
