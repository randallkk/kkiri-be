package com.lets.kkiri.dto.noti;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PressNotiReq {
    // 수신 유저 카카오 id
    private String receiverKakaoId;
}
