package com.lets.kkiri.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final ObjectMapper objectMapper;
    private Map<Long, MessageRoomDto> messageRooms;
//    private final MoimRepository moimRepository;

    @PostConstruct
    private void init() {
        messageRooms = new LinkedHashMap<>();
        messageRooms.put(1L, createRoom(1L));
    }

    public MessageRoomDto findById(Long roodId) {
        return messageRooms.get(roodId);
    }

    public MessageRoomDto createRoom(Long roomId) {
        return MessageRoomDto.builder().roomId(roomId).build();
    }

    public <T> void sendMessage(WebSocketSession session, T messageDto) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDto)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
