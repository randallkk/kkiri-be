package com.lets.kkiri.dto.noti;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class MessageNotiDto {
    private long moimId;
    private String senderKakaoId;
    private String message;
    private LocalDateTime time;
}
