package com.lets.kkiri.dto.moim;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MoimReceiptPostReq {
    private Long moimId;
    private Long memberId;
    private Integer expense;
}
