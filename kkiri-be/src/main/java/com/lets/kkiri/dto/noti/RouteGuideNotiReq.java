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
    private Long chatRoomId;
    // 수신 유저 이메일
    private String receiverKakaoId;
    private List<PointDto> path;
}
