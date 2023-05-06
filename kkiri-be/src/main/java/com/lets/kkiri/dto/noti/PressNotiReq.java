package com.lets.kkiri.dto.noti;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class PressNotiReq {
    // 발신 유저 이메일
    private String senderEmail;

    // 수신 유저 이메일
    private String receiverEmail;
}
