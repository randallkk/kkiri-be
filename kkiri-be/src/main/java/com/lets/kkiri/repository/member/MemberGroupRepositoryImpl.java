package com.lets.kkiri.repository.member;

import com.lets.kkiri.dto.moim.MoimAccountUrlDto;
import com.lets.kkiri.entity.MemberGroup;
import com.lets.kkiri.entity.QMember;
import com.lets.kkiri.entity.QMemberGroup;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MemberGroupRepositoryImpl implements MemberGroupRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;
    QMemberGroup qMemberGroup = QMemberGroup.memberGroup;
    QMember qMember = QMember.member;

    @Override
    public MemberGroup findByMoimIdAndKakaoId(Long moimId, String kakaoId) {
        return jpaQueryFactory
                .selectFrom(qMemberGroup)
                .where(qMemberGroup.moim.id.eq(moimId).and(qMemberGroup.member.kakaoId.eq(kakaoId)))
                .fetchOne();
    }

    @Override
    public Map<String, Long> findAllMemberRank(Long moimId) {
        return jpaQueryFactory
                .select(qMemberGroup.member.kakaoId, qMemberGroup.rank)
                .from(qMemberGroup)
                .where(qMemberGroup.moim.id.eq(moimId))
                .fetch()
                .stream()
                .collect(Collectors.toMap(t->t.get(0, String.class), t-> t.get(1, Integer.class).longValue()));
    }

    @Override
    public MoimAccountUrlDto findHostByMoimId(Long moimId) {
        return jpaQueryFactory
                .from(qMemberGroup)
                .select(Projections.bean(
                        MoimAccountUrlDto.class,
                        qMember.kakaoId,
                        qMember.accountUrl
                ))
                .innerJoin(qMemberGroup.member, qMember)
                .where(qMemberGroup.moim.id.eq(moimId))
                .fetchFirst();
    }
}
