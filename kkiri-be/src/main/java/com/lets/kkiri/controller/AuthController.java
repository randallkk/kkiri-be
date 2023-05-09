package com.lets.kkiri.controller;

import com.lets.kkiri.config.jwt.JwtTokenUtil;
import com.lets.kkiri.dto.member.KakaoInfoDto;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.service.AuthService;
import com.lets.kkiri.service.KakaoService;
import com.lets.kkiri.service.MemberService;
import com.lets.kkiri.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

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

    private final AuthService authService;
    private final KakaoService kakaoService;
    private final RedisService redisService;

    @GetMapping("/login")
    public ResponseEntity getKakaoCode(@RequestParam String code, HttpServletResponse res) {
        KakaoInfoDto reqUser = kakaoService.getKakaoInfo(code);
        HashMap<String, Object> kakaoUser = kakaoService.getUserInfo(reqUser.getAccessToken());

        if(kakaoUser == null) return ResponseEntity.status(404).body("KAKAO USER NOT FOUND");
        String email = kakaoUser.get("email").toString();

        Member isExist = memberService.getMemberByEmail(email);

        // 카카오 유저 이메일로 가입된 유저가 없다면.
        if(isExist == null) {
            String name = (String)kakaoUser.get("nickname");
            Member member = Member
                    .builder()
                    .nickname(name)
                    .build();

            memberService.createUser(member);
        }
        // 로그인 처리
        String accessToken = JwtTokenUtil.getToken(JwtTokenUtil.atkExpirationTime, email);
        String refreshToken = JwtTokenUtil.getToken(JwtTokenUtil.rtkExpirationTime, email);

        Member member = memberService.getMemberByEmail(email);
        if(member == null) return ResponseEntity.status(404).body("USER NOT FOUND");

        redisService.setValues(refreshToken, email);

        try {
            res.setHeader(JwtTokenUtil.HEADER_STRING, JwtTokenUtil.TOKEN_PREFIX + accessToken);
            res.setHeader("Refresh-Token", refreshToken);
        } catch (Exception e){
            return ResponseEntity.status(401).body("TOKEN EXPIRED EXCEPTION");
        }

        return ResponseEntity.ok().build();
    }
}
