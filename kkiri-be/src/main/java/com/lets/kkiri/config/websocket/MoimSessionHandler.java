package com.lets.kkiri.config.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import com.lets.kkiri.service.AuthService;
import com.lets.kkiri.service.GpsService;
import com.lets.kkiri.service.MessageRoomService;
import com.lets.kkiri.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class MoimSessionHandler extends TextWebSocketHandler {

	private final ObjectMapper objectMapper;
	private final MessageService messageService;
	private final MessageRoomService messageRoomService;
	private final GpsService gpsService;
	private final AuthService authService;
	private WebSocketSession mySession;

	@Override
	protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		mySession = session;
		MoimSessionReq msg = objectMapper.readValue(payload, MoimSessionReq.class);
		Object content = msg.getContent();
		switch (msg.getType()) {
			case JOIN:
				authService.handleActions(session, content);
				break;
			case MESSAGE:
			case EMOJI:
				messageRoomService.handleActions(session, msg.getType(), content, messageService);
				break;
			case GPS:
				gpsService.handleActions(session, content);
				break;
		}
	}
	public WebSocketSession getSession() {
		return mySession;
	}
}
