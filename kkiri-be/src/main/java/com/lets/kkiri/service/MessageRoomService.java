package com.lets.kkiri.service;

import com.lets.kkiri.dto.chatting.MessagePub;
import com.lets.kkiri.dto.chatting.MessageRes;
import com.lets.kkiri.dto.chatting.MessageSub;
import com.lets.kkiri.dto.moim.MoimSessionListDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

@Getter
@Setter
public class MessageRoomService {
    MoimSessionListDto moimSessionListDto;
    
    public void handleActions(WebSocketSession session, Object content, MessageService messageService) {    

        content = (MessagePub) content;

        sendMessage(content, messageService);
    }

    public <T> void sendMessage(T content, MessageService messageService) {
        Set<WebSocketSession> sessions = moimSessionListDto.getSessions();
        sessions.parallelStream().forEach(session -> messageService.sendMessage(session, content));
    }

    public MessageRes getChat(Long moimId) {

        return null;
    }

}
