package com.lets.kkiri.repository.member;

import com.lets.kkiri.entity.MemberGroup;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MemberGroupRepositorySupport {
    /**
     * 모임에 가입된 멤버의 랭크를 조회한다.
     * @param moimId 모임 아이디
     * @param kakaoId 멤버 카카오 아이디
     * @return
     */
    Integer findMemberRank(Long moimId, String kakaoId);

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
}
