package com.lets.kkiri.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.common.util.RedisStoreUtil;
import com.lets.kkiri.dto.gps.GpsPub;
import com.lets.kkiri.dto.gps.GpsSub;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@Service
public class GpsService {

    private final ObjectMapper objectMapper;
    private final RedisStoreUtil redisStoreUtil;

    public void handleActions(WebSocketSession session, GpsPub gpsPub) {
        Long moimId = (Long) session.getAttributes().get("moimId");
        String kakaoId = (String) session.getAttributes().get("kakaoId");
        log.debug("[ws://] {} 회원님의 위치 - gpsPub : {}", kakaoId, gpsPub.toString());
        sendMessage(new GpsSub(moimId, kakaoId, gpsPub));
    }

    void sendMessage(GpsSub gpsSub) {
        Long moimId = gpsSub.getMoimId();
        String kakaoId = gpsSub.getKakaoId();
        ArrayList<WebSocketSession> sessions = redisStoreUtil.getAllSessionsByMoimId(moimId, WebSocketSession.class);
        sessions.parallelStream().forEach(session -> send(session, gpsSub));
        log.debug("[ws://] {} 회원님의 위치 발송 완료!", kakaoId);
    }

    void send(WebSocketSession session, GpsSub gpsSub) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(gpsSub)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
