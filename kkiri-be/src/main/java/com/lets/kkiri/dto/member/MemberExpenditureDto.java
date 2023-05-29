package com.lets.kkiri.dto.member;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberExpenditureDto {
    private String kakaoId;
    private int expenditure;
}
