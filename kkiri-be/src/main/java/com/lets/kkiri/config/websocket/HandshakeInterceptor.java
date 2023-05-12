package com.lets.kkiri.config.websocket;

import com.lets.kkiri.common.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor{

    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        // attributes 에 값을 저장하면 웹소켓 핸들러 클래스의 WebSocketSession에 전달된다
        log.debug("[ws://] === Before Handshake ===");

        ServletServerHttpRequest ssreq = (ServletServerHttpRequest) request;
        HttpServletRequest req =  ssreq.getServletRequest();

        // accessToken에서 kakaoId를 추출하여 attributes에 저장
        String accessToken = req.getHeader("Authorization");
        String memberIdentifier = JwtTokenUtil.getIdentifier(accessToken);
        attributes.put("kakaoId", memberIdentifier);

        // moimId를 pathVariable로 받아서 session attributes에 저장
        Map pathVariables = (Map) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long moimId = Long.parseLong((String) pathVariables.get("moimId"));
        attributes.put("moimId", moimId);

        log.info("[https://] {} 회원님이 {} 모임에 접속하기 위해 3handshake 하는 중...", memberIdentifier, moimId);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request,
                               @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler,
                               Exception ex) {
        log.debug("[ws://] === After Handshake ===");
        super.afterHandshake(request, response, wsHandler, ex);
    }

}