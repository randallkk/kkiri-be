package com.lets.kkiri.config.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.dto.gps.GpsDto;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import com.lets.kkiri.service.GpsService;
import com.lets.kkiri.service.MessageRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 모임에 대한 웹소켓 연결을 처리하는 핸들러
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MoimWebSocketHandler extends TextWebSocketHandler {

	private final ObjectMapper objectMapper;
	private final MessageRoomService messageRoomService;
	private final GpsService gpsService;
	private final MoimSessionHandler moimSessionHandler;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		// 클라이언트가 서버에 연결되면 호출되는 메소드
		Long moimId = (Long) session.getAttributes().get("moimId");
		String kakaoId = (String) session.getAttributes().get("kakaoId");
		moimSessionHandler.register(moimId, kakaoId, session);
		log.info("[ws://] kakaoId: {} 회원님이 {} 모임 웹소켓에 접속 완료 햇삼 꺄륵><", kakaoId, moimId);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) {
		// 클라이언트가 서버와의 연결을 끊으면 호출되는 메소드
		Long moimId = (Long) session.getAttributes().get("moimId");
		String kakaoId = (String) session.getAttributes().get("kakaoId");
		moimSessionHandler.remove(moimId, kakaoId);
		log.info("[ws://] kakaoId: {} 회원님이 {} 모임 웹소켓에서 나갔삼", kakaoId, moimId);
	}

	@Override
	protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		MoimSessionReq msg = objectMapper.readValue(payload, MoimSessionReq.class);
		Object content = msg.getContent();
		log.debug(msg.getContent() instanceof GpsDto ? "true" : content.toString());

		switch (msg.getType()) {
			case MESSAGE:
			case EMOJI:
				messageRoomService.handleActions(session, msg.getType(), content);
				break;
			case GPS:
				if (content instanceof GpsDto) {
					gpsService.handleActions(session, msg.getType(), (GpsDto) content);
					break;
				}
				else throw new KkiriException(ErrorCode.INVALID_PARAMETER, "Invalid GPS message format");
			default:
				log.error("Unknown message type");
		}
	}
}
