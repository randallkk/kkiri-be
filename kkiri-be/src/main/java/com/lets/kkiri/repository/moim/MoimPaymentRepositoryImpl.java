package com.lets.kkiri.repository.moim;

import com.lets.kkiri.entity.MoimPayment;
import com.lets.kkiri.entity.QMoimPayment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MoimPaymentRepositoryImpl implements MoimPaymentRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public MoimPayment findByMoimIdAndKakaoId(Long moimId, String kakaoId) {
        return jpaQueryFactory
                .selectFrom(QMoimPayment.moimPayment)
                .where(QMoimPayment.moimPayment.memberGroup.moim.id.eq(moimId).and(QMoimPayment.moimPayment.memberGroup.member.kakaoId.eq(kakaoId)))
                .fetchOne();
    }

    @Override
    public List<MoimPayment> findAllByMoimId(Long moimId) {
        return jpaQueryFactory
                .selectFrom(QMoimPayment.moimPayment)
                .where(QMoimPayment.moimPayment.memberGroup.moim.id.eq(moimId))
                .fetch();
    }
}
