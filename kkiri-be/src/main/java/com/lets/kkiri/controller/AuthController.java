package com.lets.kkiri.controller;

import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.common.util.JwtTokenUtil;
import com.lets.kkiri.dto.member.KakaoUserPostDto;
import com.lets.kkiri.dto.member.MemberLoginPostRes;
import com.lets.kkiri.dto.auth.ReissueGetRes;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.service.AuthService;
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
    Long atkExpirationTime;

    @Value("${jwt.expiration.rtk}")
    Long rtkExpirationTime;
    private final MemberService memberService;
    private final RedisService redisService;
    private final AuthService authService;

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
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String refreshToken
    ) {
        String kakaoId = JwtTokenUtil.getIdentifier(refreshToken.substring(7));
        if (!redisService.getValues(kakaoId).equals(refreshToken.substring(7))) throw new KkiriException(ErrorCode.INVALID_TOKEN);

        // 프론트로 보내줄 access, refresh token 생성
        String afterATK = JwtTokenUtil.getToken(JwtTokenUtil.atkExpirationTime, kakaoId);
        String afterRTK = JwtTokenUtil.getToken(JwtTokenUtil.rtkExpirationTime, kakaoId);

        redisService.setValues(afterRTK, kakaoId);
        return ResponseEntity.status(200).body(ReissueGetRes.builder()
                .accessToken(afterATK)
                .refreshToken(afterRTK)
                .build());
    }

    @PostMapping("/verify")
    public ResponseEntity getLoginVerify(@RequestHeader(JwtTokenUtil.HEADER_STRING) String refreshToken){
        if (JwtTokenUtil.getExpiration(refreshToken.substring(7)) <= 0) return ResponseEntity.status(401).build();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity doLogout(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String accessToken
    ){
        authService.logout(accessToken.substring(7));
        return ResponseEntity.ok().body("SUCCESS");
    }

}
