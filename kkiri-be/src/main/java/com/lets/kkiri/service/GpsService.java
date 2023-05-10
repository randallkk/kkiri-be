package com.lets.kkiri.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.dto.gps.GpsPub;
import com.lets.kkiri.dto.moim.MoimSessionListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@RequiredArgsConstructor
@Service
public class GpsService {

    private final ObjectMapper objectMapper;
    MoimSessionListDto sessions;

    public void handleActions(WebSocketSession session, GpsPub gpsPub) {
        session.
        if () {
            sessions.add(session);
        }
        sendMessage(gpsPub);
    }

    private GpsPub

    void sendMessage(GpsPub gpsPub) {
        sessions.parallelStream().forEach(session -> send(session, gpsPub));
    }

    private GpsPub

    void send(WebSocketSession session, GpsPub gpsDto) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(gpsDto)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
