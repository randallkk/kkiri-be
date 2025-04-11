package com.lets.kkiri.config.websocket;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@RequiredArgsConstructor
@Component
public class MoimSessionHandler {
    private final ObjectMapper objectMapper;

    private final Map<Long, Map<String, WebSocketSession>> sessionMap = new ConcurrentHashMap<>();

    public void register(Long moimId, String userId, WebSocketSession session) {
        sessionMap.computeIfAbsent(moimId, k -> new ConcurrentHashMap<>());
        sessionMap.get(moimId).put(userId, session);
        log.debug("[ws://] {} 회원님이 {} 모임에 등록되었습니다.", moimId, userId);
    }

    public void remove(Long moimId, String userId) {
        sessionMap.get(moimId).remove(userId);
        log.debug("[ws://] {} 회원님이 {} 모임에서 제거되었습니다.", moimId, userId);
    }

    public Map<String, WebSocketSession> getAllSessionsByMoimId(Long moimId) {
        return sessionMap.get(moimId);
    }

    public void sendMessage(WebSocketSession session, MoimSessionReq moimSessionReq) {
            try {
                if (session.isOpen()) {
                    try {
                        synchronized (session) {
                            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(moimSessionReq)));
                        }
                    } catch (IOException e) {
                        log.error("세션 동기화 오류", new IOException("세션 동기화 오류"));
                    }
                }
                else log.error("세션이 닫혀있습니다.", new IOException("세션이 닫혀있습니다."));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
    }
}
