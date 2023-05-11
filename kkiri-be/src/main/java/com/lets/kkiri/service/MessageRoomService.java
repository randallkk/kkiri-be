package com.lets.kkiri.service;

import com.lets.kkiri.common.util.RedisStoreUtil;
import com.lets.kkiri.dto.chatting.MessageDto;
import com.lets.kkiri.dto.chatting.MessageMetaData;
import com.lets.kkiri.dto.chatting.MessagePub;
import com.lets.kkiri.dto.chatting.MessageRes;
import com.lets.kkiri.dto.chatting.MessageSub;
import com.lets.kkiri.dto.moim.MoimSessionListDto;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import com.lets.kkiri.entity.Message;
import com.lets.kkiri.repository.chatting.MessageRepositorySupport;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Array;
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
    private final RedisStoreUtil redisStoreUtil;

    private final MessageRepositorySupport messageRepositorySupport;

    public void handleActions(WebSocketSession session, MoimSessionReq.MoimSessionType type, Object content, MessageService messageService) {
        MessageDto msg = null;
        switch (type) {
            case MESSAGE:
                msg = (MessageDto) content;
                break;
            case EMOJI:
                msg = (MessageDto) content;
                break;
            case URGENT:
                msg = (MessageDto) content;
                msg.setMessage(msg.getNickname() + "님이 재촉하셨습니다.");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        if(msg != null) {
            sendMessage(session, type, msg, messageService);
        }
        else {
            throw new IllegalStateException("message null");
        }
    }

    public <T> void sendMessage(WebSocketSession session, MoimSessionReq.MoimSessionType type, MessageDto msg, MessageService messageService) {
        // Set<WebSocketSession> sessions = moimSessionListDto.getSessions();
        ArrayList<WebSocketSession> sessions = redisStoreUtil.getAllSessionsByMoimId(Long.parseLong(session.getAttributes().get("moimId").toString()), WebSocketSession.class);
        sessions.parallelStream().forEach(s -> messageService.sendMessage(s, msg));
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
