package com.lets.kkiri.config.websocket;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
public class MoimSessionHandler {

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
}
