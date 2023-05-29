package com.lets.kkiri.repository.moim;

import com.lets.kkiri.dto.moim.MoimExpenseDto;
import com.lets.kkiri.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MoimExpenseRepositoryImpl implements MoimExpenseRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    QMoimExpense qMoimExpense = QMoimExpense.moimExpense;
    QMemberExpense qMemberExpense = QMemberExpense.memberExpense;
    QMoim qMoim = QMoim.moim;

    @Override
    public Integer findMoimExpenseByMoimId(Long moimId) {
        return jpaQueryFactory.select(qMoimExpense.expense.sum())
                .from(qMoimExpense)
                .where(qMoimExpense.moim.id.eq(moimId))
                .fetchOne();
    }

    @Override
    public Page<MoimExpenseDto> findAllByMoimId(Long moimId, Pageable pageable) {
        List<MoimExpense> moimExpenseList = jpaQueryFactory.selectFrom(qMoimExpense)
                .leftJoin(qMoimExpense.moim, qMoim).fetchJoin()
                .where(qMoimExpense.moim.id.eq(moimId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        log.debug("이게 맞나?");
        List<MoimExpenseDto> moimExpenseDtoList = moimExpenseList.stream().map(moimExpense -> new MoimExpenseDto(moimExpense,
                jpaQueryFactory.select(qMemberExpense.member.kakaoId)
                        .from(qMemberExpense)
                        .leftJoin(qMemberExpense.moimExpense, qMoimExpense)//.fetchJoin()
                        .where(qMemberExpense.moimExpense.moim.id.eq(moimId))
                        .fetch()
                )).collect(Collectors.toList());
        log.debug("moimExpenseDtoList: {}", moimExpenseDtoList);
        return new PageImpl<>(moimExpenseDtoList, pageable, moimExpenseDtoList.size());
    }
}
