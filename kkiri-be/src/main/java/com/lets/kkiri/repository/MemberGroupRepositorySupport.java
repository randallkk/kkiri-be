package com.lets.kkiri.repository;

import com.lets.kkiri.dto.member.MemberProfileDto;
import com.lets.kkiri.entity.QMember;
import com.lets.kkiri.entity.QMemberGroup;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberGroupRepositorySupport {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    QMember qMember = QMember.member;
    QMemberGroup qMemberGroup = QMemberGroup.memberGroup;

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
}
