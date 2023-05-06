package com.lets.kkiri.dto.noti;

import com.lets.kkiri.dto.gps.PointDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class RouteGuideNotiReq {
    // 발신 유저 이메일
    private String senderEmail;

    // 수신 유저 이메일
    private String receiverEmail;
    private List<PointDto> path;
}
