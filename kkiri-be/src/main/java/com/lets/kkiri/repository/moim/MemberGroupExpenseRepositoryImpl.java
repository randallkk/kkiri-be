package com.lets.kkiri.repository.moim;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.lets.kkiri.entity.QMemberGroupExpense.memberGroupExpense;
import static com.lets.kkiri.entity.QMoimExpense.moimExpense;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MemberGroupExpenseRepositoryImpl implements MemberGroupExpenseRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public int findMoimExpenseByMoimId(Long moimId) {
        return jpaQueryFactory
                .select(moimExpense.expense.sum())
                .from(memberGroupExpense)
                .where(memberGroupExpense.memberGroup.moim.id.eq(moimId))
                .groupBy(memberGroupExpense.memberGroup.moim.id)
                .fetchOne(); 
    }
}
