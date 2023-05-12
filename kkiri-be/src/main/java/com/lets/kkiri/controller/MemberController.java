package com.lets.kkiri.controller;

import com.lets.kkiri.common.util.JwtTokenUtil;
import com.lets.kkiri.dto.member.KakaoUserPostDto;
import com.lets.kkiri.dto.member.MemberDevicePostReq;
import com.lets.kkiri.dto.member.MemberLoginPostRes;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/devices")
    public ResponseEntity deviceAdd(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String accessToken,
            @RequestBody MemberDevicePostReq request)
    {
        String kakaoId = JwtTokenUtil.getIdentifier(accessToken);
        memberService.addDeviceToken(kakaoId, request.getDeviceToken());
        return ResponseEntity.ok().body("SUCCESS");
    }
}
