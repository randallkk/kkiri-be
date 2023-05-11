package com.lets.kkiri.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.mongodb.core.MongoTemplate;
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
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;

    public void sendMessage(WebSocketSession session, Object content)  {
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(content)));
            log.debug("sendMessage : {}", content);
            // mongoTemplate.save(content);
        }catch (Exception e){
            log.debug("sendMessage error : {}", e.getMessage());
        }
    }

}
