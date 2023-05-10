package com.lets.kkiri.config.websocket;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lets.kkiri.config.jwt.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Slf4j
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor{

    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        // 위의 파라미터 중, attributes 에 값을 저장하면 웹소켓 핸들러 클래스의 WebSocketSession에 전달된다
        log.debug("ws:// Before Handshake");

        ServletServerHttpRequest ssreq = (ServletServerHttpRequest) request;
        HttpServletRequest req =  ssreq.getServletRequest();

        String accessToken = req.getHeader("Authorization");
        String memberIdentifier = JwtTokenUtil.getIdentifier(accessToken);
        attributes.put("memberKakaoId", memberIdentifier);
        log.info("ws:// memberKakaoId: {} 회원님이 웹소켓에 접속햇삼 꺄륵><", memberIdentifier);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request,
                               @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler,
                               Exception ex) {
        log.debug("ws:// After Handshake");
        super.afterHandshake(request, response, wsHandler, ex);
    }

}