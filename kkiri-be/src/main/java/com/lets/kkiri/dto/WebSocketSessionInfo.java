package com.lets.kkiri.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WebSocketSessionInfo {

    private Map<Long, Map<String, WebSocketSession>> sessionMap = new HashMap<>();
    private static WebSocketSessionInfo instance = new WebSocketSessionInfo();

    private WebSocketSessionInfo() {
        // 생성자는 외부에서 호출못하게 private 으로 지정해야 한다.
    }

    public static WebSocketSessionInfo getInstance() {
        return instance;
    }

    public void addSession(Long moimId, String kakaoId, WebSocketSession session) {
        sessionMap.computeIfAbsent(moimId, k -> new HashMap());
        sessionMap.get(moimId).put(kakaoId, session);
        log.debug("[ws://] sessionMap에 {} 회원님의 session이 추가되었습니다.", kakaoId);
    }

    public Map<String, WebSocketSession> getAllSessionsByMoimId(Long moimId) {
        return sessionMap.get(moimId);
    }
}