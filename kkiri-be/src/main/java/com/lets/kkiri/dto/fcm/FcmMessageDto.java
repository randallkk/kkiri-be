package com.lets.kkiri.dto.fcm;

import com.lets.kkiri.dto.gps.PointDto;
import lombok.*;

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
}
