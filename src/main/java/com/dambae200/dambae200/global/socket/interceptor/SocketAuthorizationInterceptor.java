package com.dambae200.dambae200.global.socket.interceptor;

import com.dambae200.dambae200.domain.access.exception.AccessNotAllowedException;
import com.dambae200.dambae200.domain.access.service.AccessService;
import com.dambae200.dambae200.domain.sessionInfo.exception.SessionInfoNotExistsException;
import com.dambae200.dambae200.domain.sessionInfo.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
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
        log.info("full message:" + message);
        log.info("payload:" + new String((byte[])message.getPayload(), StandardCharsets.UTF_8));
        log.info("auth:" + headerAccessor.getNativeHeader("Authorization"));

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