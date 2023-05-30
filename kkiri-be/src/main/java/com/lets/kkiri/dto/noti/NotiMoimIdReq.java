package com.lets.kkiri.dto.noti;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class NotiMoimIdReq {
    // 수신 모임 id
    private Long chatRoomId;
}
