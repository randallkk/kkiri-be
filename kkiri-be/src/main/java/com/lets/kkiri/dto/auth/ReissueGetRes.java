package com.lets.kkiri.dto.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReissueGetRes {
    String accessToken;
    String refreshToken;
}
