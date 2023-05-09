package com.lets.kkiri.dto.member;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KakaoInfoDto {
    String accessToken;
    String refreshToken;
    Long refreshTokenExpiration;
    Long expiresIn;
}
