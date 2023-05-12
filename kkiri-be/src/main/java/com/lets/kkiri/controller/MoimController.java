package com.lets.kkiri.controller;

import com.lets.kkiri.common.util.JwtTokenUtil;
import com.lets.kkiri.dto.moim.MoimPostReq;
import com.lets.kkiri.dto.noti.PressNotiReq;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.service.MemberService;
import com.lets.kkiri.service.MoimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/moims")
@RequiredArgsConstructor
public class MoimController {
    private final MoimService moimService;
    private final MemberService memberService;
    @PostMapping()
    public ResponseEntity moinAdd(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String accessToken,
            @RequestBody MoimPostReq moimPostReq
    ) {
        String kakaoId = JwtTokenUtil.getIdentifier(accessToken);
        moimService.addMoim(kakaoId, moimPostReq);
        return ResponseEntity.ok().build();
    }
}
