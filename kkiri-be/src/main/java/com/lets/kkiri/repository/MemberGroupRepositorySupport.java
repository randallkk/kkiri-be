package com.lets.kkiri.repository;

import com.lets.kkiri.dto.member.MemberProfileDto;
import com.lets.kkiri.entity.QMember;
import com.lets.kkiri.entity.QMemberGroup;
import com.lets.kkiri.entity.QMoim;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import com.querydsl.core.types.ExpressionUtils;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberGroupRepositorySupport {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    QMember qMember = QMember.member;
    QMemberGroup qMemberGroup = QMemberGroup.memberGroup;
    QMoim qMoim = QMoim.moim;

    public List<MemberProfileDto> findMembersByMoimId(Long moimId) {
        return jpaQueryFactory.select(
                        Projections.constructor(
                                MemberProfileDto.class,
                                qMember
                        )).distinct()
                .from(qMember, qMemberGroup)
                .where(
                        qMemberGroup.moim.id.eq(moimId),
                        qMemberGroup.member.id.eq(qMember.id)
                )
                .fetch();
    }

    public Long getCountMoimByMemberIdAndDate(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory.select(qMemberGroup).distinct()
            .from(qMemberGroup)
            .leftJoin(qMoim).on(qMemberGroup.moim.id.eq(qMoim.id))
            .where(qMemberGroup.member.id.eq(memberId).and(qMoim.meetingAt.between(startDate, endDate)))
            .fetchCount();
    }

    public List<Tuple> getMemberListByMemberIdAndDate(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory.select(qMemberGroup.member.id, qMemberGroup.member.id.count())
            .from(qMemberGroup)
            .where(qMoim.id.in(
                JPAExpressions
                    .select(qMoim.id)
                    .from(qMemberGroup)
                    .leftJoin(qMoim).on(qMemberGroup.moim.id.eq(qMoim.id))
                    .where(qMemberGroup.member.id.eq(memberId).and(qMoim.meetingAt.between(startDate, endDate)))
            ))
            .groupBy(qMemberGroup.member.id)
            .orderBy(qMemberGroup.member.id.count().desc())
            .fetch();
    }

    public String getMostLocByMemberIdAndDate(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory.select(
                        Expressions.stringTemplate("{0}", Expressions.template(
                                String.class, "SUBSTRING_INDEX({0}, ' ', 1)", qMoim.placeName
                        ))
                )
                .from(qMoim)
                .where(qMoim.id.in(
                        JPAExpressions
                                .select(qMoim.id)
                                .from(qMemberGroup)
                                .leftJoin(qMoim).on(qMemberGroup.moim.id.eq(qMoim.id))
                                .where(qMemberGroup.member.id.eq(memberId).and(qMoim.meetingAt.between(startDate, endDate)))
                ))
                .groupBy(
                        Expressions.stringTemplate("{0}", Expressions.template(
                                String.class, "SUBSTRING_INDEX({0}, ' ', 1)", qMoim.placeName
                        ))
                )
                .orderBy(
                        Expressions.stringTemplate("{0}", Expressions.template(
                                String.class, "SUBSTRING_INDEX({0}, ' ', 1)", qMoim.placeName
                        )).count().desc()
                )
                .fetch().get(0);
    }
}
