package com.lets.kkiri.repository.moim;

import com.lets.kkiri.entity.MoimPayment;

public interface MoimPaymentRepositorySupport {
    MoimPayment findByMoimIdAndKakaoId(Long moimId, String kakaoId);
}
