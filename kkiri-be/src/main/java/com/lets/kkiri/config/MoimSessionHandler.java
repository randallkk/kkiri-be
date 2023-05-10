package com.lets.kkiri.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import com.lets.kkiri.service.GpsService;
import com.lets.kkiri.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
	private final GpsService gpsService;

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("payload : {}" + payload);

		MoimSessionReq msg = objectMapper.readValue(payload, MoimSessionReq.class);
		Object content = msg.getContent();
		log.info("content : {}" + content);
		switch (msg.getType()) {
			case MESSAGE:

				break;
			case GPS:

				break;
			case EMOJI:

				break;
			case URGENT:

				break;
			default:
				throw new IllegalStateException("Unexpected value: " + msg);
		}
	}
}
