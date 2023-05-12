package com.lets.kkiri.service;

import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.dto.moim.MoimPostReq;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.entity.MemberTopic;
import com.lets.kkiri.entity.Moim;
import com.lets.kkiri.repository.moim.MoimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MoimService {
    private final MemberService memberService;
    private final MoimRepository moimRepository;
    private final MemberTopicService memberTopicService;

    public void addMoim(String kakaoId, MoimPostReq moimPostReq) {
        Moim moim = moimRepository.save(moimPostReq.toEntity());

        Long memberId = memberService.getMemberByKakaoId(kakaoId).getId();
        memberTopicService.addMemberToMoim(memberId, moim.getId());
    }
}
