package com.lets.kkiri.repository.moim;

import com.lets.kkiri.entity.QMoimExpense;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MoimExpenseRepositoryImpl implements MoimExpenseRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    QMoimExpense qMoimExpense = QMoimExpense.moimExpense;

    @Override
    public Integer findMoimExpenseByMoimId(Long moimId) {
        return jpaQueryFactory.select(qMoimExpense.expense.sum())
                .from(qMoimExpense)
                .where(qMoimExpense.moim.id.eq(moimId))
                .fetchOne();
    }
}
