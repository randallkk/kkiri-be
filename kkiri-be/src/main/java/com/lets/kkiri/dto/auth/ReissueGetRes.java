package com.lets.kkiri.dto.member;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReissueGetRes {
    String accessToken;
    String refreshToken;
}
