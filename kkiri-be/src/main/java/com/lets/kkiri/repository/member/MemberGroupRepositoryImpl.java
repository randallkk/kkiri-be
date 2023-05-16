package com.lets.kkiri.repository.member;

import com.lets.kkiri.entity.MemberGroup;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lets.kkiri.entity.QMemberGroup.memberGroup;

@Repository
@RequiredArgsConstructor
public class MemberGroupRepositoryImpl implements MemberGroupRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Integer findMemberRank(Long moimId, String kakaoId) {
        return jpaQueryFactory
                .select(memberGroup.rank)
                .from(memberGroup)
                .where(memberGroup.moim.id.eq(moimId).and(memberGroup.member.kakaoId.eq(kakaoId)))
                .fetchOne();
    }

    @Override
    public MemberGroup findByMoimIdAndKakaoId(Long moimId, String kakaoId) {
        return jpaQueryFactory
                .selectFrom(memberGroup)
                .where(memberGroup.moim.id.eq(moimId).and(memberGroup.member.kakaoId.eq(kakaoId)))
                .fetchOne();
    }

    @Override
    public Map<String, Long> findAllMemberRank(Long moimId) {
        return jpaQueryFactory
                .select(memberGroup.member.kakaoId, memberGroup.rank)
                .from(memberGroup)
                .where(memberGroup.moim.id.eq(moimId))
                .fetch()
                .stream()
                .collect(Collectors.toMap(t->t.get(0, String.class), t-> t.get(1, Integer.class).longValue()));
    }
}
