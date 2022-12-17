package com.dambae200.dambae200.global.socket.config;

import com.dambae200.dambae200.global.socket.dto.CustomHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/stomp/store")
                .setAllowedOriginPatterns("*")
//                .setAllowedOriginPatterns("http://*:*", "http://*.*.*.*:*")
//                .setAllowedOriginPatterns("http://*:80", "http://*.*.*.*:80", "http://*.343", "http://*.*.*.*:343")
                .setHandshakeHandler(new CustomHandshakeHandler())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        config.setApplicationDestinationPrefixes("/pub");
        config.enableSimpleBroker("/sub");
    }

}