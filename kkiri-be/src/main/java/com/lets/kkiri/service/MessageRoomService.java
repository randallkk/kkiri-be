package com.lets.kkiri.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.dto.WebSocketSessionInfo;
import com.lets.kkiri.dto.chatting.MessageDto;
import com.lets.kkiri.dto.chatting.MessageMetaData;
import com.lets.kkiri.dto.chatting.MessageRes;
import com.lets.kkiri.dto.chatting.MessageSub;
import com.lets.kkiri.dto.moim.MoimSessionReq;

import com.lets.kkiri.dto.noti.MessageNotiDto;
import com.lets.kkiri.entity.Message;
import com.lets.kkiri.repository.chatting.MessageRepositorySupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageRoomService {
    private final ObjectMapper objectMapper;
    private final MessageRepositorySupport messageRepositorySupport;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final MongoTemplate mongoTemplate;
    private final ChatNotiService chatNotiService;

    public void handleActions(WebSocketSession session, MoimSessionReq.MoimSessionType type, Object content, MessageService messageService) {
        Long moimId = (Long) session.getAttributes().get("moimId");
        String kakaoId;

        try {
            kakaoId = (String) session.getAttributes().get("kakaoId");
            MessageDto msg = objectMapper.convertValue(content, MessageDto.class);
            log.debug("[ws://] {} 회원님의 채팅 - Message : {}", kakaoId, msg.toString());
            sendMessage(type, msg, messageService);
        } catch (NullPointerException e) {
            log.error("세션에 kakaoId가 없습니다.", e);
            try {
                session.close();
                log.debug("[ws://] 세션을 닫았습니다.");
                throw new KkiriException(ErrorCode.UNAUTHORIZED);
            } catch (IOException ioException) {
                log.error("세션을 닫는데 실패했습니다.", ioException);

            }
        }
    }

    public <T> void sendMessage(MoimSessionReq.MoimSessionType type, MessageDto msg, MessageService messageService) {
        log.debug("MessageRoomService / sendMessage ()");
        WebSocketSessionInfo webSocketSessionInfo = WebSocketSessionInfo.getInstance();
        Map<String, WebSocketSession> sessions = webSocketSessionInfo.getAllSessionsByMoimId(msg.getMoimId());
        MessageSub sub = MessageSub.messageDtoToSub(msg);
        sub.setMessageType(type);
        sub.setSeq(sequenceGeneratorService.generateSequence(Message.SEQUENCE_NAME));
        //메세지 전송 후 DB에 저장
        sub.setTime(LocalDateTime.now());
        Message saveMsg = sub.toEntity(sub);
        mongoTemplate.save(saveMsg);

        //채팅 알림
        if (type.equals(MoimSessionReq.MoimSessionType.MESSAGE)) {
            chatNotiService.sendChatNoti(MessageNotiDto.builder()
                    .message(sub.getMessage())
                    .time(sub.getTime())
                    .moimId(sub.getMoimId())
                    .senderKakaoId(sub.getKakaoId())
                    .build());
        }

        log.debug("[ws://] session size : {}", sessions.size());
        log.debug("[ws://] MessageSub : {}", sub.toString());
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            WebSocketSession session = entry.getValue();
            messageService.sendMessage(session, sub);
        }
    }


    public MessageRes getFirstChat(Long moimId, Pageable pageable) {
        Page<Message> messages = messageRepositorySupport.findRecent(moimId, pageable);
        MessageRes res = makeChatList(messages);
        return res;
    }

    public MessageRes getChat(Long moimId, String lastMessageId, Pageable pageable) {
        Page<Message> messages = messageRepositorySupport.findMessage(moimId, pageable, lastMessageId);
        MessageRes res = makeChatList(messages);
        return res;
    }

    private MessageRes makeChatList(Page<Message> msgList) {
        MessageRes res = new MessageRes();
        if(msgList.getContent().size() <= 0) return null;
        MessageMetaData meta = MessageMetaData.builder()
                .last(msgList.isLast())
                .lastMessageId(msgList.getContent().get(msgList.getContent().size() - 1).getId())
                .build();

        List<MessageSub> msgsubList = new ArrayList<>();
        for(Message msg : msgList.getContent()){
            if(msg.getMessageType().equals(MoimSessionReq.MoimSessionType.EMOJI)) continue;
            MessageSub dto = MessageSub.messageToDto(msg);
            msgsubList.add(dto);
        }
        res.setMeta(meta);
        res.setChat(msgsubList);
        return res;
    }

}
