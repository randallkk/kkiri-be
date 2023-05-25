package com.lets.kkiri.repository.moim;

import com.lets.kkiri.entity.MoimPayment;

import java.util.List;

public interface MoimPaymentRepositorySupport {
    MoimPayment findByMoimIdAndKakaoId(Long moimId, String kakaoId);
    List<MoimPayment> findAllByMoimId(Long moimId);
}
