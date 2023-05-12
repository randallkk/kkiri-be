package com.lets.kkiri.controller;

import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.common.util.JwtTokenUtil;
import com.lets.kkiri.dto.moim.MoimInfoGetRes;
import com.lets.kkiri.dto.moim.MoimLinkPostReq;
import com.lets.kkiri.dto.moim.MoimPostReq;
import com.lets.kkiri.dto.moim.MoimRegisterRes;
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
        Long moimId = moimService.addMoim(kakaoId, moimPostReq);
        MoimRegisterRes res = MoimRegisterRes.builder().moimId(moimId).build();
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{moimId}")
    public ResponseEntity moimDetail(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String accessToken,
            @PathVariable Long moimId
    ) {
        MoimInfoGetRes res = moimService.findMoimById(moimId);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/links")
    public ResponseEntity moinLinkAdd(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String accessToken,
            @RequestBody MoimLinkPostReq moimPostReq

    ) {
        try {
            moimService.addLinkToMoim(moimPostReq);
        } catch (KkiriException e) {
            throw e;
        } catch (Exception e) {
            throw new KkiriException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().build();
    }
}
