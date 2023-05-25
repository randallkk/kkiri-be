package com.lets.kkiri.dto.moim;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class MoimPaymentGetRes {
    private int totalExpenditure;
    private int totalMemberCnt;
    private Map<String, Integer> moimPaymentList;

    public void addMoimPaymentDto(String kakaoId, Integer memberPayment) {
        moimPaymentList.put(kakaoId, memberPayment);
    }
}
