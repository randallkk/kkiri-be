package com.lets.kkiri.repository.member;

import com.lets.kkiri.dto.moim.MoimAccountUrlDto;
import com.lets.kkiri.entity.MemberGroup;

import java.util.Map;

public interface MemberGroupRepositorySupport {
    /**
     * 모임에 가입된 멤버를 조회한다.
     * @param moimId 모임 아이디
     * @param kakaoId 멤버 카카오 아이디
     * @return
     */
    MemberGroup findByMoimIdAndKakaoId(Long moimId, String kakaoId);

    /**
     * 모임에 가입된 모든 멤버의 랭크를 조회한다.
     * @param moimId
     * @return 멤버 카카오 아이디, 랭크
     */
    Map<String, Long> findAllMemberRank(Long moimId);

    /**
     * 모임을 개설한 사용자의 kakaoId와 결제 url을 조회한다.
     * @param moimId 모임 id
     * @return kakaoId, accoutUrl
     */
    MoimAccountUrlDto findHostByMoimId(Long moimId);
}
