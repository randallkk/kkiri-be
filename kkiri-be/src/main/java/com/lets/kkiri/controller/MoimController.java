package com.lets.kkiri.controller;

import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.common.util.JwtTokenUtil;
import com.lets.kkiri.dto.MoimGroupPostReq;
import com.lets.kkiri.dto.moim.*;
import com.lets.kkiri.dto.noti.PressNotiReq;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.service.MemberService;
import com.lets.kkiri.service.MoimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping()
    public ResponseEntity moimCardList(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String accessToken,
            @RequestParam(required = false) String date
    ) {
        String kakaoId = JwtTokenUtil.getIdentifier(accessToken);

        List<MoimCardDto> moimCards = moimService.findMoimsByKakaoId(kakaoId, date);

        return ResponseEntity.ok().body(MoimCardListGetRes.builder()
                .moimCardList(moimCards)
                .build());
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
    public ResponseEntity moimLinkAdd(
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

    @PostMapping("/groups")
    public ResponseEntity moimGroupJoin(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String accessToken,
            @RequestBody MoimGroupPostReq moimGroupPostReq
    ) {
        String kakaoId = JwtTokenUtil.getIdentifier(accessToken);
        Long moimId = moimService.addMemberToMoim(kakaoId, moimGroupPostReq);
        MoimRegisterRes res = MoimRegisterRes.builder().moimId(moimId).build();
        return ResponseEntity.ok().body(res);
    }

}
