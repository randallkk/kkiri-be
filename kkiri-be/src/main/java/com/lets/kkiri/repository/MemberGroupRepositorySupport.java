package com.lets.kkiri.repository;

import com.lets.kkiri.dto.member.MemberProfileDto;
import com.lets.kkiri.entity.QMember;
import com.lets.kkiri.entity.QMemberGroup;
import com.lets.kkiri.entity.QMoim;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
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

    //통계: 가장 많이 만난 사람
    public List<Tuple> getMemberListByMemberIdAndDate(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        QMemberGroup subMG = new QMemberGroup("subMG");
        return jpaQueryFactory.select(qMemberGroup.member.id, qMemberGroup.member.id.count())
            .from(qMemberGroup)
            .leftJoin(qMoim).on(qMemberGroup.moim.id.eq(qMoim.id))
            .leftJoin(subMG).on(qMoim.id.eq(subMG.moim.id).and(subMG.member.id.eq(memberId)).and(qMoim.meetingAt.between(startDate, endDate)))
            .where(subMG.moim.id.isNotNull())
            .groupBy(qMemberGroup.member.id)
            .orderBy(qMemberGroup.member.id.count().desc())
            .fetch();
    }

    //통계: 가장 많이 만난 장소
    public String getMostLocByMemberIdAndDate(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory.select(
                qMoim.placeName.substring(0, qMoim.placeName.indexOf(" "))
            )
            .from(qMoim)
            .join(qMemberGroup).on(qMoim.id.eq(qMemberGroup.moim.id))
            .where(qMemberGroup.member.id.eq(memberId).and(qMoim.meetingAt.between(startDate, endDate)))
            .groupBy(
                qMoim.placeName.substring(0, qMoim.placeName.indexOf(" "))
            )
            .orderBy(
                qMoim.placeName.substring(0, qMoim.placeName.indexOf(" ")).count().desc()
            )
            .fetchFirst();
    }

    //통계: 가장 많이 만난 시간대
    public String getMostTimeByMemberIdAndDate(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        StringExpression cases = new CaseBuilder()
            .when(qMoim.meetingAt.hour().between(0, 5)).then("새벽시간대")
            .when(qMoim.meetingAt.hour().between(6, 11)).then("오전시간대")
            .when(qMoim.meetingAt.hour().between(12, 17)).then("오후시간대")
            .when(qMoim.meetingAt.hour().between(18, 24)).then("저녁시간대")
            .otherwise("시간대없음").as("time");

        Tuple tuple = jpaQueryFactory.select(cases, qMoim.meetingAt.count())
            .from(qMoim)
            .where(qMoim.id.in(
                JPAExpressions
                    .select(qMoim.id)
                    .from(qMemberGroup)
                    .leftJoin(qMoim).on(qMemberGroup.moim.id.eq(qMoim.id))
                    .where(qMemberGroup.member.id.eq(memberId).and(qMoim.meetingAt.between(startDate, endDate)))
            ))
            .orderBy(qMoim.meetingAt.count().desc())
            .fetchFirst();
        return tuple.get(0, String.class);
    }
}
