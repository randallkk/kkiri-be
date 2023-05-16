package com.lets.kkiri.dto.fcm;

import com.lets.kkiri.dto.gps.PointDto;
import com.lets.kkiri.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessageDto {
    private List<String> tokenList;
    private String channelId;
    private String title;
    private String body;
    private List<PointDto> path;
    private String moimName;
    private Member sender;
    private String message;
    private LocalDateTime time;
}
