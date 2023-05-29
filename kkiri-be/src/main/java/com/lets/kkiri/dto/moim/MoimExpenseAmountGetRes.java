package com.lets.kkiri.dto.moim;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class MoimExpenseAmountGetRes {
    private int totalExpenditure;
    private int totalMemberCnt;
    private Map<String, Integer> moimPaymentList;    // memberGroupExpenditureList

    public void addMemberGroupExpenditure(String kakaoId, Integer memberGroupExpenditure) {
        moimPaymentList.put(kakaoId, memberGroupExpenditure);
    }
}
