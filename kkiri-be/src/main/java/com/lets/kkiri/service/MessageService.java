package com.lets.kkiri.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.dto.chatting.MessageSub;

import com.lets.kkiri.entity.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final ObjectMapper objectMapper;

    public void sendMessage(WebSocketSession session, MessageSub msg)  {
        try{
            System.out.println("session = " + session);
            if(session.isOpen()) {
                try{
                    synchronized (session){
                        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
                        log.debug("sendMessage : {}", msg);
                    }
                }catch (IOException e){
                    log.error("세션 동기화 오류", new IOException("세션 동기화 오류"));
                }
            }
            else log.error("sendMessage : 세션이 닫혀있습니다.", new IOException("sendMessage : 세션이 닫혀있습니다."));
        }catch (Exception e){
            log.debug("sendMessage error : {}", e.getMessage());
        }
    }

}
