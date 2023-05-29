package com.lets.kkiri.repository.member;

import com.lets.kkiri.dto.member.MemberExpenditureDto;
import com.lets.kkiri.entity.QMemberExpense;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MemberExpenseRepositoryImpl implements MemberExpenseRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;
    QMemberExpense qMemberExpense = QMemberExpense.memberExpense;


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
}
