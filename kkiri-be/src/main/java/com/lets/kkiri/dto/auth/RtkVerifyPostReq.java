package com.lets.kkiri.dto.auth;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RtkVerifyPostReq {
    String refreshToken;
}
