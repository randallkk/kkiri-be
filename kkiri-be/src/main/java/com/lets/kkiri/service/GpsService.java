package com.lets.kkiri.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.config.websocket.MoimSessionHandler;
import com.lets.kkiri.dto.gps.GpsDto;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import com.lets.kkiri.dto.moim.MoimSessionReq.MoimSessionType;
import com.lets.kkiri.pubsub.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static com.lets.kkiri.dto.moim.MoimSessionReq.MoimSessionType.GPS;

@Slf4j
@RequiredArgsConstructor
@Service
public class GpsService {

    private final ObjectMapper objectMapper;
    private final RedisPublisher redisPublisher;

    public void handleActions(WebSocketSession session, MoimSessionType type, GpsDto gpsDto) {
        Long moimId = gpsDto.getMoimId();
        try {
            redisPublisher.publish("kkiri:moim",
                    objectMapper.writeValueAsString(new MoimSessionReq(GPS, gpsDto)));
            log.debug("[ws://] {} 회원님의 위치 발송", gpsDto.getKakaoId());
        } catch (JsonProcessingException e) {
            log.error("위치 발송에 실패했습니다.", e);
            throw new KkiriException(ErrorCode.INTERNAL_SERVER_ERROR, "위치 발송에 실패했습니다. 다시 시도해주세요.");
        }
    }


}
