package com.dambae200.dambae200.global.socket.config;

import com.dambae200.dambae200.domain.access.service.AccessService;
import com.dambae200.dambae200.domain.sessionInfo.service.SessionService;
import com.dambae200.dambae200.global.error.GlobalExceptionHandler;
import com.dambae200.dambae200.global.socket.dto.CustomHandshakeHandler;
import com.dambae200.dambae200.global.socket.exeption.GlobalMessageExceptionHandler;
import com.dambae200.dambae200.global.socket.exeption.SocketExceptionHandler;
import com.dambae200.dambae200.global.socket.interceptor.SocketAuthorizationInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final SocketAuthorizationInterceptor socketAuthorizationInterceptor;
    private final SocketExceptionHandler socketExceptionHandler;
    private final CustomHandshakeHandler customHandshakeHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .setErrorHandler(socketExceptionHandler)
                .addEndpoint("/stomp/store")
//                .setAllowedOriginPatterns("*")
                .setAllowedOriginPatterns("http://*:5500", "http://*.*.*.*:5500")
                .setHandshakeHandler(customHandshakeHandler)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/pub");
        config.enableSimpleBroker("/sub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(socketAuthorizationInterceptor);
    }

}