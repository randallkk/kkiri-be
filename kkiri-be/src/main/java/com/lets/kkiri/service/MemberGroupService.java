package com.lets.kkiri.service;

import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.dto.member.MemberAccountDto;
import com.lets.kkiri.dto.moim.MoimAccountUrlDto;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.entity.MemberGroup;
import com.lets.kkiri.repository.member.MemberGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberGroupService {
    private final MemberGroupRepository memberGroupRepository;

    public Long getMemberGroupCnt(Long moimId) {
        return memberGroupRepository.countByMoimId(moimId);
    }

    @Transactional
    public MemberGroup setMemberRank(Long moimId, String kakaoId) {
        MemberGroup memberGroup = memberGroupRepository.findByMoimIdAndKakaoId(moimId, kakaoId);
        if (memberGroup == null) {
            throw new KkiriException(ErrorCode.MEMBER_NOT_FOUND, "유저가 해당 모임에 가입되어 있지 않습니다.");
        }
        if (memberGroup.getRank() == null) {
            Long rank = memberGroupRepository.countByMoimIdAndRankIsNotNull(moimId);
            memberGroup.changeRank(rank);
        }
        return memberGroup;
    }

    public Map<String, Long> getMemberGroupRanking(Long moimId) {
        return memberGroupRepository.findAllMemberRank(moimId);
    }

    public MoimAccountUrlDto findHostByMoimId(Long moimId) {
        return memberGroupRepository.findHostByMoimId(moimId);
    }
}
