package com.acc.chatdemo.websocket;

import com.acc.chatdemo.auth.interceptor.AuthChannelInterceptor;
import com.acc.chatdemo.auth.JwtProvider;
import com.acc.chatdemo.auth.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtProvider jwtProvider;
    private final UserDetailServiceImpl userDetailService;

    @Override
    public void registerStompEndpoints(org.springframework.web.socket.config.annotation.StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat").setAllowedOrigins("*").withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new AuthChannelInterceptor(jwtProvider, userDetailService));
    }

}
