package com.lets.kkiri.dto.member;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class LoginMemberInfoRes {
    String accessToken;
    String refreshToken;
    List<String> deviceTokens;
}
