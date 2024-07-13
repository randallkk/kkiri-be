package com.lets.kkiri.config.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.dto.WebSocketSessionInfo;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import com.lets.kkiri.service.GpsService;
import com.lets.kkiri.service.MessageRoomService;
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
	private final MessageRoomService messageRoomService;
	private final GpsService gpsService;
	private WebSocketSession mySession;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		// 클라이언트가 서버에 연결되면 호출되는 메소드
		StringBuilder sb = new StringBuilder();
		Long moimId = (Long) session.getAttributes().get("moimId");
		String kakaoId = (String) session.getAttributes().get("kakaoId");
		sb.append(moimId).append(":")
				.append(kakaoId);

		WebSocketSessionInfo webSocketSessionInfo = WebSocketSessionInfo.getInstance();
		webSocketSessionInfo.addSession(moimId, kakaoId, session);

		log.info("[ws://] kakaoId: {} 회원님이 {} 모임 웹소켓에 접속 완료 햇삼 꺄륵><", kakaoId, moimId);
	}

	@Override
	protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		mySession = session;
		MoimSessionReq msg = objectMapper.readValue(payload, MoimSessionReq.class);
		Object content = msg.getContent();

		switch (msg.getType()) {
			case MESSAGE:
			case EMOJI:
				messageRoomService.handleActions(session, msg.getType(), content);
				break;
			case GPS:
				gpsService.handleActions(session, content);
				break;
			default:
				log.error("Unknown message type");
		}
	}
	public WebSocketSession getSession() {
		return mySession;
	}
}
