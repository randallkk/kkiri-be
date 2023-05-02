package com.lets.kkiri.dto.fcm;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessageDto {
    // 테스트 데이터 여부 (true: 테스트 데이터, false: 실제 데이터)
    private boolean validateOnly;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Notification notification;
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}
