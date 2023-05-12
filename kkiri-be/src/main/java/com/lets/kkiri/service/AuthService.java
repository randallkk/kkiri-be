package com.lets.kkiri.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.common.util.JwtTokenUtil;
import com.lets.kkiri.dto.WebSocketSessionInfo;
import com.lets.kkiri.dto.member.MemberJoinWebsocketMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final ObjectMapper objectMapper;

    private final RedisService redisService;

    public void logout(String accessToken) {
        String kakaoId = JwtTokenUtil.getIdentifier(accessToken);

        if(redisService.getValues(kakaoId) != null) redisService.deleteValues(kakaoId);

        /*
         * redis에
         *   KEY: kakaoId
         *   VALUE: "logout" 으로 저장하여, 이전 구한 토큰의 유효시간동안 해당 엑세스토큰을 사용하지 못하게 처리
         */
        redisService.setBlackList(kakaoId, JwtTokenUtil.getExpiration(accessToken));
    }

    /**
     * WebSocketSession을 받아서 처리할 로직을 구현한다.
     * @param session WebSocketSession
     * @param object WebSocketSession에서 받은 LinkedHashMap
     */
    public void handleActions(WebSocketSession session, Object object) {
        Long moimId = (Long) session.getAttributes().get("moimId");
        MemberJoinWebsocketMsg memberJoinWebsocketMsg = objectMapper.convertValue(object, MemberJoinWebsocketMsg.class);
        String kakaoId = memberJoinWebsocketMsg.getKakaoId();
        if ("".equals(redisService.getValues(kakaoId))) {
            try {
                session.close();
                throw new KkiriException(ErrorCode.UNAUTHORIZED);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            session.getAttributes().put("kakaoId", kakaoId);
            WebSocketSessionInfo webSocketSessionInfo = WebSocketSessionInfo.getInstance();
            webSocketSessionInfo.addSession(moimId, kakaoId, session);
            log.debug("[ws://] {} 회원님이 {}번 모임에 접속하셨습니다.", kakaoId, moimId);
        }
    }
}
