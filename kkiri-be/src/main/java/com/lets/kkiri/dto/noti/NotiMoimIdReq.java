package com.lets.kkiri.dto.noti;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HelpNotiReq {
    // 수신 모임 id
    private Long chatRoomId;
}
