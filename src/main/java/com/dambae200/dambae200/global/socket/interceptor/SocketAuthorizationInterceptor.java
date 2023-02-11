package com.dambae200.dambae200.global.socket.interceptor;

import com.dambae200.dambae200.domain.access.exception.AccessNotAllowedException;
import com.dambae200.dambae200.domain.access.service.AccessService;
import com.dambae200.dambae200.domain.sessionInfo.exception.SessionInfoNotExistsException;
import com.dambae200.dambae200.domain.sessionInfo.service.SessionService;
import com.dambae200.dambae200.global.error.GlobalExceptionHandler;
import com.dambae200.dambae200.global.socket.dto.SocketRequest;
import com.dambae200.dambae200.global.socket.exeption.GlobalMessageExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompDecoder;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
@Slf4j
@Component
public class SocketAuthorizationInterceptor implements ChannelInterceptor {

    final private SessionService sessionService;
    final private AccessService accessService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        System.out.println("full message:" + message);
        System.out.println("payload:" + new String((byte[])message.getPayload(), StandardCharsets.UTF_8));
        System.out.println("auth:" + headerAccessor.getNativeHeader("Authorization"));

        if (StompCommand.SEND.equals(headerAccessor.getCommand())) {


            // login check
            String destination = null;
            String accessToken = null;
            try {
                destination = headerAccessor.getNativeHeader("destination").get(0);
                accessToken = headerAccessor.getNativeHeader("Authorization").get(0);
                sessionService.checkValidation(accessToken);
            } catch (SessionInfoNotExistsException | NullPointerException e ) { // 해당 헤더가 없으면 NullPointerException이 발생
                log.info("미인증 사용자 요청 {}", destination);
                throw new SessionInfoNotExistsException();
            }

            // store access check
            if (destination.startsWith("/pub/store")) {
                Long userId = null;
                Long storeId = null;
                try {
                userId = Long.valueOf(headerAccessor.getNativeHeader("userId").get(0));
                storeId = Long.valueOf(headerAccessor.getNativeHeader("storeId").get(0));
                    accessService.checkAccess(userId, storeId);
                } catch (AccessNotAllowedException | NullPointerException e) {
                    log.info("해당 store에 대한 권한 없는 사용자 {} userId={} storeId={}", destination, userId, storeId);
                    throw e;
                }
            }
        }
        return message;
    }
}