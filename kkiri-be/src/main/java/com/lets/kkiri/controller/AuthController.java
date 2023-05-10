package com.lets.kkiri.controller;

import com.lets.kkiri.config.jwt.JwtTokenUtil;
import com.lets.kkiri.dto.member.KakaoIdPostReq;
import com.lets.kkiri.dto.member.KakaoUserPostDto;
import com.lets.kkiri.dto.member.MemberLoginPostRes;
import com.lets.kkiri.dto.member.ReissueGetRes;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.service.MemberService;
import com.lets.kkiri.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

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
                MemberLoginPostRes.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .deviceTokens(deviceTokens)
                        .build()
        );
    }

    @PostMapping("/reissue")
    public ResponseEntity getAccessToken(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String refreshToken,
            @RequestBody KakaoIdPostReq request
    ) {
        String kakaoId = request.getKakaoId();
        if (!redisService.getValues(kakaoId).equals(refreshToken.substring(7))) return ResponseEntity.status(401).body("INVALID TOKEN");

        // 프론트로 보내줄 access, refresh token 생성
        String afterATK = JwtTokenUtil.getToken(JwtTokenUtil.atkExpirationTime, kakaoId);
        String afterRTK = JwtTokenUtil.getToken(JwtTokenUtil.rtkExpirationTime, kakaoId);

        redisService.setValues(afterRTK, kakaoId);
        return ResponseEntity.status(200).body(ReissueGetRes.builder()
                .accessToken(afterATK)
                .refreshToken(afterRTK)
                .build());
    }
}
