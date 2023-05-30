package com.lets.kkiri.dto.moim;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoimAccountUrlDto {
    private String kakaoId;
    private String accountUrl;
}
