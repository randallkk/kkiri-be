package com.lets.kkiri.dto.moim;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoimExpenseListGetRes {
    private Map<String, Integer> meta;
    private List<MoimExpenseDto> paymentList;
}
