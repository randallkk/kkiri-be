package com.lets.kkiri.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lets.kkiri.common.util.RedisStoreUtil;
import com.lets.kkiri.dto.chatting.MessageDto;
import com.lets.kkiri.dto.chatting.MessageMetaData;
import com.lets.kkiri.dto.chatting.MessageRes;
import com.lets.kkiri.dto.chatting.MessageSub;
import com.lets.kkiri.dto.moim.MoimSessionListDto;
import com.lets.kkiri.dto.moim.MoimSessionReq;

import com.lets.kkiri.entity.Message;
import com.lets.kkiri.repository.chatting.MessageRepositorySupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageRoomService {
    MoimSessionListDto moimSessionListDto;
    private final RedisStoreUtil redisStoreUtil;

    private final MessageRepositorySupport messageRepositorySupport;

    public void handleActions(WebSocketSession session, MoimSessionReq.MoimSessionType type, Object content, MessageService messageService) {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        MessageDto msg = mapper.convertValue(content, MessageDto.class);
        if(msg != null) {
            sendMessage(session, type, msg, messageService);
        }
        else {
            throw new IllegalStateException("message null");
        }
    }

    public <T> void sendMessage(WebSocketSession session, MoimSessionReq.MoimSessionType type, MessageDto msg, MessageService messageService) {
        log.debug("MessageRoomService / sendMessage ()");
        ArrayList<WebSocketSession> sessions = redisStoreUtil.getAllSessionsByMoimId(Long.parseLong(session.getAttributes().get("moimId").toString()), WebSocketSession.class);
        MessageSub sub = MessageSub.messageDtoToSub(msg);
        sub.setMessageType(type);
        if(sub.getMessageType() == MoimSessionReq.MoimSessionType.URGENT) {
            sub.setMoimId(Long.parseLong(session.getAttributes().get("moimId").toString()));
        }
        sessions.parallelStream().forEach(s -> messageService.sendMessage(s, sub));
    }

    public MessageRes getFirstChat(Long moimId, Pageable pageable){
        Page<Message> messages = messageRepositorySupport.findRecent(moimId, pageable);
        MessageRes res = makeChatList(messages);
        return res;
    }

    public MessageRes getChat(Long moimId, String lastMessageId, Pageable pageable){
        Page<Message> messages = messageRepositorySupport.findMessage(moimId, pageable, lastMessageId);
        MessageRes res = makeChatList(messages);
        return res;
    }

    private MessageRes makeChatList(Page<Message> msgList) {
        MessageRes res = new MessageRes();
        MessageMetaData meta = MessageMetaData.builder()
            .last(msgList.isLast())
            .lastMessageId(msgList.getContent().get(msgList.getContent().size()-1).getId())
            .build();

        List<MessageSub> msgsubList = new ArrayList<>();
        for(Message msg : msgList.getContent()){
            MessageSub dto = MessageSub.messageToDto(msg);
            msgsubList.add(dto);
        }
        res.setMeta(meta);
        res.setChat(msgsubList);
        return res;
    }

}
