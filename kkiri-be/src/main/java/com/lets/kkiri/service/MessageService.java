package com.lets.kkiri.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    public void sendMessage(WebSocketSession session, Object content) throws IOException {
        String payload = content.toString();
        log.debug("context payload : {}", content.toString());
        session.sendMessage(new TextMessage(payload));
    }

}
