package com.lets.kkiri.dto.moim;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class MoimSessionListDto {
    private Long moimId;
    private Set<WebSocketSession> sessions;

    public MoimSessionListDto(Long moimId) {
        this.moimId = moimId;
        this.sessions = new HashSet<>();
    }

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }
}
