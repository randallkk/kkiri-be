package com.lets.kkiri.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.dto.WebSocketSessionInfo;
import com.lets.kkiri.dto.gps.GpsPub;
import com.lets.kkiri.dto.gps.GpsSub;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

import static com.lets.kkiri.dto.moim.MoimSessionReq.MoimSessionType.GPS;

@Slf4j
@RequiredArgsConstructor
@Service
public class GpsService {

    private final ObjectMapper objectMapper;

    public void handleActions(WebSocketSession session, Object object) {
        Long moimId = (Long) session.getAttributes().get("moimId");
        String kakaoId;
        try {
            kakaoId = (String) session.getAttributes().get("kakaoId");
            GpsPub gpsPub = objectMapper.convertValue(object, GpsPub.class);
            log.debug("[ws://] {} 회원님의 위치 - gpsPub : {}", kakaoId, gpsPub.toString());
            sendMessage(new GpsSub(moimId, kakaoId, gpsPub));
        } catch (NullPointerException e) {
            log.error("세션에 kakaoId가 없습니다.", e);
            try {
                session.close();
                log.debug("[ws://] 세션을 닫았습니다.");
                throw new KkiriException(ErrorCode.UNAUTHORIZED);
            } catch (IOException ioException) {
                log.error("세션을 닫는데 실패했습니다.", ioException);

            }
        }
    }

    void sendMessage(GpsSub gpsSub) {
        Long moimId = gpsSub.getMoimId();
        String kakaoId = gpsSub.getKakaoId();
        WebSocketSessionInfo webSocketSessionInfo = WebSocketSessionInfo.getInstance();
        Map<String, WebSocketSession> sessions = webSocketSessionInfo.getAllSessionsByMoimId(moimId);
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            String key = entry.getKey();
            WebSocketSession session = entry.getValue();
            if (key.equals(kakaoId)) continue;
            send(session, new MoimSessionReq(GPS, gpsSub));
        }
        log.debug("[ws://] {} 회원님의 위치 발송 완료!", kakaoId);
    }

    void send(WebSocketSession session, MoimSessionReq moimSessionReq) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(moimSessionReq)));
            }
            else log.error("세션이 닫혀있습니다.", new IOException("세션이 닫혀있습니다."));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
