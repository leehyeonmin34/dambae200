package com.dambae200.dambae200.global.socket.exeption;

import com.dambae200.dambae200.global.common.dto.StandardResponse;
import com.dambae200.dambae200.global.error.GlobalExceptionHandler;
import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketExceptionHandler extends StompSubProtocolErrorHandler {

    private final GlobalExceptionHandler globalExceptionHandler;
    private final ObjectMapper objectMapper;

//    MessageDeliveryException
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]>clientMessage, Throwable ex)
    {
        Throwable cause = ex.getCause();
        try {
            if (cause instanceof BindException)
                return prepareErrorMessage(globalExceptionHandler.handleBindException((BindException) cause).getBody());
            if (cause instanceof MethodArgumentTypeMismatchException)
                return prepareErrorMessage(globalExceptionHandler.handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) cause).getBody());
            if (cause instanceof HttpRequestMethodNotSupportedException)
                return prepareErrorMessage(globalExceptionHandler.handleHttpRequestMethodNotSupportedException((HttpRequestMethodNotSupportedException) cause).getBody());
            if (cause instanceof AccessDeniedException)
                return prepareErrorMessage(globalExceptionHandler.handleAccessDeniedException((AccessDeniedException) cause).getBody());
            if (cause instanceof BusinessException)
                return prepareErrorMessage(globalExceptionHandler.handleBusinessException((BusinessException) cause).getBody());
            if (cause instanceof Exception)
                return prepareErrorMessage(globalExceptionHandler.handleException((Exception) cause).getBody());
        }catch (Exception e) {
            e.printStackTrace();
            return super.handleClientMessageProcessingError(clientMessage, e);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }


//    // 메세지 생성
    private Message<byte[]> prepareErrorMessage(StandardResponse body) throws JsonProcessingException {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(objectMapper.writeValueAsString(body).getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }


}
