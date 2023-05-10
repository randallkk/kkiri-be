package com.lets.kkiri.service;

import com.lets.kkiri.dto.chatting.MessageDto;
import com.lets.kkiri.service.MessageService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class MessageRoomDto {
    private Long roomId;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public MessageRoomDto(Long roomId) {
        this.roomId = roomId;
    }

    public void handleActions(WebSocketSession session, MessageDto messageDto, MessageService messageService) {
        if (messageDto.getMessageType().equals(MessageDto.MessageType.ENTER)) {
            sessions.add(session);
            messageDto.setMessage(messageDto.getNickname() + "님이 입장했습니다.");
        }
        sendMessage(messageDto, messageService);
    }

    public <T> void sendMessage(T messageDto, MessageService messageService) {
        sessions.parallelStream().forEach(session -> messageService.sendMessage(session, messageDto));
    }
}
