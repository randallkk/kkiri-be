package com.lets.kkiri.dto.moim;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.lets.kkiri.entity.MoimExpense;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoimExpenseDto {
    private String place;
    private Integer expense;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;
    private String receiptUrl;
    private List<String> memberKakaoIds;

    public MoimExpenseDto(MoimExpense moimExpense) {
        this.place = moimExpense.getPlace();
        this.expense = moimExpense.getExpense();
        this.time = moimExpense.getTime();
        this.receiptUrl = moimExpense.getReceiptUrl();
    }
}
