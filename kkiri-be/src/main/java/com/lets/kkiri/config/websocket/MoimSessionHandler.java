package com.lets.kkiri.config.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import com.lets.kkiri.service.MessageRoomService;
import com.lets.kkiri.service.MessageService;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Log4j2
@RequiredArgsConstructor
public class MoimSessionHandler extends TextWebSocketHandler {

	private final ObjectMapper objectMapper;
	private final MessageService messageService;
	private final MessageRoomService messageRoomService;
	// private final GpsService gpsService;

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("payload : {}" + payload);

		MoimSessionReq msg = objectMapper.readValue(payload, MoimSessionReq.class);
		Object content = msg.getContent();
		log.info("content : {}" + content);
		if(msg.getType().equals(MoimSessionReq.MoimSessionType.GPS)) {

		}
		else if(msg.getType().equals(MoimSessionReq.MoimSessionType.MESSAGE)
				|| msg.getType().equals(MoimSessionReq.MoimSessionType.EMOJI)) {
			messageRoomService.handleActions(session, msg.getType(), content, messageService);
		}
		else {
			throw new IllegalStateException("Unexpected value: " + msg);
		}
	}
}
