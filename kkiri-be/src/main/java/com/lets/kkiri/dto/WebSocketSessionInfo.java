package com.lets.kkiri.dto;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@NoArgsConstructor
public class WebSocketSessionInfo {

    private Map<Long, Map<String, WebSocketSession>> sessionMap = new HashMap<>();
    private static WebSocketSessionInfo instance = new WebSocketSessionInfo();

    public static WebSocketSessionInfo getInstance() {
        return instance;
    }

    public void addSession(Long moimId, String kakaoId, WebSocketSession session) {
        sessionMap.computeIfAbsent(moimId, k -> new HashMap());
        sessionMap.get(moimId).put(kakaoId, session);
    }

    public void removeSession(Long moimId, String sessionId) {
        sessionMap.get(moimId).remove(sessionId);
        log.debug("[ws://] sessionMap에서 {} 회원님의 session이 제거되었습니다.", sessionId);
    }

    public Map<String, WebSocketSession> getAllSessionsByMoimId(Long moimId) {
        return sessionMap.get(moimId);
    }
}