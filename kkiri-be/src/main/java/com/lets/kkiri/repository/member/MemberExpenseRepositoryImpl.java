package com.lets.kkiri.repository.member;

import com.lets.kkiri.dto.member.MemberExpenditureDto;
import com.lets.kkiri.entity.QMember;
import com.lets.kkiri.entity.QMemberExpense;
import com.lets.kkiri.entity.QMoim;
import com.lets.kkiri.entity.QMoimExpense;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MemberExpenseRepositoryImpl implements MemberExpenseRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;
    QMemberExpense qMemberExpense = QMemberExpense.memberExpense;
    QMoimExpense qMoimExpense = QMoimExpense.moimExpense;
    QMember qMember = QMember.member;
    QMoim qMoim = QMoim.moim;


    @Override
    public List<MemberExpenditureDto> findEachExpenditureForMoim(Long moimId) {
        return jpaQueryFactory.from(qMemberExpense)
                .where(qMemberExpense.moimExpense.moim.id.eq(moimId))
                .groupBy(qMemberExpense.member.id)
                .select(Projections.bean(
                        MemberExpenditureDto.class,
                        qMemberExpense.member.kakaoId,
                        qMemberExpense.expenditure.sum().as("expenditure")
                ))
                .fetch();
    }

    @Override
    public Map<Long, List<String>> findAllKakaoIdByMoimId(Long moimId) {
        return jpaQueryFactory.selectFrom(qMemberExpense)
                .innerJoin(qMemberExpense.member, qMember)
                .innerJoin(qMemberExpense.moimExpense, qMoimExpense)
                .where(qMoimExpense.moim.id.eq(moimId))
                .transform(GroupBy.groupBy(qMoimExpense.id).as(GroupBy.list(qMember.kakaoId)));
    }
}
