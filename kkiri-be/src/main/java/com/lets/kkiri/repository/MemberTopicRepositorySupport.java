package com.lets.kkiri.repository;

import com.lets.kkiri.dto.member.MemberProfileDto;
import com.lets.kkiri.entity.QMember;
import com.lets.kkiri.entity.QMemberDevice;
import com.lets.kkiri.entity.QMemberTopic;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberTopicRepositorySupport {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    QMemberTopic qMemberTopic = QMemberTopic.memberTopic;
    QMemberDevice qMemberDevice = QMemberDevice.memberDevice;
    QMember qMember = QMember.member;

    public List<MemberProfileDto> findMembersByMoimId(Long moimId) {
        return jpaQueryFactory.select(
                Projections.constructor(
                        MemberProfileDto.class,
                        qMember
                )).distinct()
                .from(qMember, qMemberTopic, qMemberDevice)
                .where(
                        qMemberTopic.memberDevice.id.eq(qMemberDevice.id),
                        qMember.id.eq(qMemberDevice.member.id),
                        qMemberTopic.name.eq(moimId.toString())
                )
                .fetch();
    }
}
