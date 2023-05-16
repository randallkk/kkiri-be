package com.lets.kkiri.config.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().permitAll() // WebSocket 연결을 허용하는 데스트 매처
                .simpDestMatchers("/ws/**", "/wss/**").authenticated() // /ws/**로 시작하는 WebSocket 연결을 허용
                .anyMessage().authenticated(); // 인증된 사용자만 메시지 수신을 허용
    }
}
