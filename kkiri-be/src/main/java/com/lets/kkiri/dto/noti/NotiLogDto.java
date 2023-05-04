package com.lets.kkiri.dto.noti;

import com.lets.kkiri.entity.NotiLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class NotiLogDto {
    private String token;
    private String title;
    private String body;
    private String image;
    private String messageId;

    public static NotiLogDto of(NotiLog notiLog) {
        return NotiLogDto.builder()
                .token(notiLog.getToken())
                .title(notiLog.getTitle())
                .body(notiLog.getBody())
                .image(notiLog.getImage())
                .messageId(notiLog.getMessageId())
                .build();
    }
    public NotiLog toEntity() {
        return NotiLog.builder()
                .token(token)
                .title(title)
                .body(body)
                .image(image)
                .messageId(messageId)
                .build();
    }
}
