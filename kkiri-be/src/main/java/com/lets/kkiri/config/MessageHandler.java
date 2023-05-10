package com.lets.kkiri.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.dto.chatting.MessageDto;
import com.lets.kkiri.service.MessageRoomDto;
import com.lets.kkiri.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@RequiredArgsConstructor
public class MessageHandler extends TextWebSocketHandler {

	private final MessageService messageService;
	private final ObjectMapper objectMapper;
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("payload : {}" + payload);

		MessageDto msg = objectMapper.readValue(payload, MessageDto.class);
		MessageRoomDto room = messageService.findById(msg.getMoimId());
		room.handleActions(session, msg, messageService);
	}
}
