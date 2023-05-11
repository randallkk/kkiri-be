package com.lets.kkiri.service;

import com.lets.kkiri.dto.chatting.MessageMetaData;
import com.lets.kkiri.dto.chatting.MessagePub;
import com.lets.kkiri.dto.chatting.MessageRes;
import com.lets.kkiri.dto.chatting.MessageSub;
import com.lets.kkiri.dto.moim.MoimSessionListDto;
import com.lets.kkiri.entity.Message;
import com.lets.kkiri.repository.chatting.MessageRepositorySupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageRoomService {
    MoimSessionListDto moimSessionListDto;
    private final MessageRepositorySupport messageRepositorySupport;
    
    public void handleActions(WebSocketSession session, Object content, MessageService messageService) {    

        content = (MessagePub) content;

        sendMessage(content, messageService);
    }

    public <T> void sendMessage(T content, MessageService messageService) {
        Set<WebSocketSession> sessions = moimSessionListDto.getSessions();
        sessions.parallelStream().forEach(session -> messageService.sendMessage(session, content));
    }

    public MessageRes getFirstChat(Long moimId, Pageable pageable){
        MessageRes res = new MessageRes();
        Page<Message> messages = messageRepositorySupport.findRecent(moimId, pageable);
        MessageMetaData meta = MessageMetaData.builder()
            .last(messages.isLast())
            .lastMessageId(messages.getContent().get(messages.getContent().size()-1).getId())
            .build();

        List<MessageSub> msgList = new ArrayList<>();
        for(Message msg : messages.getContent()){
            MessageSub dto = MessageSub.toDto(msg);
            msgList.add(dto);
        }
        res.setMeta(meta);
        res.setChat(msgList);
        return res;
    }

    public MessageRes getChat(Long moimId, String lastMessageId, Pageable pageable){

        return null;
    }

}
