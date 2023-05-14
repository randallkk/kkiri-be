package com.lets.kkiri.controller;

import com.lets.kkiri.common.util.JwtTokenUtil;
import com.lets.kkiri.dto.gps.ArriveReq;
import com.lets.kkiri.dto.gps.ArriveRes;
import com.lets.kkiri.entity.MemberGroup;
import com.lets.kkiri.service.MemberGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gps")
public class GpsController {
    private final MemberGroupService memberGroupService;

    @PostMapping("/arrive")
    public ResponseEntity<ArriveRes> arrive(@RequestHeader(JwtTokenUtil.HEADER_STRING) String accessToken, @RequestBody ArriveReq request) {
        String kakaoId = JwtTokenUtil.getIdentifier(accessToken);
        Long moimId = request.getMoimId();
        MemberGroup memberGroup = memberGroupService.setMemberRank(moimId, kakaoId);
        log.info("{} 회원님이 {}번 모임에 {}번째로 도착하셨습니다.", kakaoId, moimId, memberGroup.getRank());
        Long overall = memberGroupService.getMemberGroupCnt(moimId);
        if (memberGroup.getRank() == overall.intValue()) {
            log.info("{}번 모임의 모든 회원이 도착했습니다.", moimId);
            Map<String, Long> ranking = new HashMap<>();
            ranking.put("rank", memberGroup.getRank().longValue());
            ranking.put("overall", overall);
            ranking.putAll(memberGroupService.getMemberGroupRanking(moimId));
            return ResponseEntity.ok().body(new ArriveRes(request.getMoimId(), kakaoId, request.getDestinationTime(), ranking));
        }
        return ResponseEntity.ok().body(new ArriveRes(request.getMoimId(), kakaoId, request.getDestinationTime(), memberGroup.getRank().longValue(), overall));
    }

}