package com.lets.kkiri.config.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.common.util.RedisStoreUtil;
import com.lets.kkiri.dto.gps.GpsPub;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import com.lets.kkiri.service.GpsService;
import com.lets.kkiri.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class MoimSessionHandler extends TextWebSocketHandler {

	private final ObjectMapper objectMapper;
	private final MessageService messageService;
	private final GpsService gpsService;

	private final RedisStoreUtil redisStoreUtil;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 클라이언트가 서버에 연결되면 호출되는 메소드
		StringBuilder sb = new StringBuilder();
		Long moimId = (Long) session.getAttributes().get("moimId");
		String kakaoId = (String) session.getAttributes().get("kakaoId");
		sb.append("WebsocketSession:")
				.append(moimId).append(":")
				.append(kakaoId);
		log.debug("session Key : {}", sb.toString());
		redisStoreUtil.saveData(sb.toString(), session);
		log.info("[ws://] kakaoId: {} 회원님이 {} 모임 웹소켓에 접속 완료 햇삼 꺄륵><", kakaoId, moimId);
	}

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
				gpsService.handleActions(session, (GpsPub) content);
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
