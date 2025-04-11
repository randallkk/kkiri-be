package com.lets.kkiri.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.config.websocket.MoimSessionHandler;
import com.lets.kkiri.dto.BaseMessageDto;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscribeListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final MoimSessionHandler moimSessionHandler;

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        try {
            MoimSessionReq messageBody = objectMapper.readValue(message.getBody(), MoimSessionReq.class);
            BaseMessageDto baseMessageDto = messageBody.getContent();
            String kakaoId = baseMessageDto.getKakaoId();
            Map<String, WebSocketSession> sessions = moimSessionHandler.getAllSessionsByMoimId(baseMessageDto.getMoimId());
            for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
                String key = entry.getKey();
                WebSocketSession session = entry.getValue();
                if (key.equals(kakaoId)) continue;
                moimSessionHandler.sendMessage(session, messageBody);
            }
            log.debug("[ws://] {} 회원님의 위치 발송 완료!", kakaoId);
        } catch (Exception e) {
            log.error("메세지 전파 실패: {}", e.getMessage());
            throw new KkiriException(ErrorCode.INTERNAL_SERVER_ERROR, "메시지 처리 중 오류가 발생했습니다.");
        }
    }
}