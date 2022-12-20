package com.dambae200.dambae200.global.socket.exeption;

import com.dambae200.dambae200.global.common.dto.StandardResponse;
import com.dambae200.dambae200.global.error.ErrorResponse;
import com.dambae200.dambae200.global.error.GlobalExceptionHandler;
import com.dambae200.dambae200.global.error.GlobalExceptionHandlerInterface;
import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.socket.dto.SocketRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

@ControllerAdvice
@Slf4j
public class GlobalMessageExceptionHandler {

    private final GlobalExceptionHandler globalExceptionHandler;
    private final SimpMessagingTemplate template; // 특정 Broker로 메시지를 전달
    private final MethodProvider methodProvider;

    public GlobalMessageExceptionHandler(GlobalExceptionHandler globalExceptionHandler, SimpMessagingTemplate template) {
        this.globalExceptionHandler = globalExceptionHandler;
        this.template = template;
        this.methodProvider = new MethodProvider();
    }

    // GlobalExceptionHandler에서 처리하는 모든 타입의 예외를 그대로 메시지 예외 핸들러에서도 처리해야하기에 래퍼 구조를 사용
    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(Principal principal, @Payload SocketRequest request, MethodArgumentNotValidException e) {
        template.convertAndSendToUser(principal.getName(), request.getResponseChannel(), methodProvider.handleMethodArgumentNotValidException(e).getBody());
    }

    @MessageExceptionHandler(BindException.class)
    public void handleBindException(Principal principal, @Payload SocketRequest request,BindException e) {
        template.convertAndSendToUser(principal.getName(), request.getResponseChannel(), methodProvider.handleBindException(e).getBody());
    }

    @MessageExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void handleMethodArgumentTypeMismatchException(Principal principal, @Payload SocketRequest request,MethodArgumentTypeMismatchException e) {
        template.convertAndSendToUser(principal.getName(), request.getResponseChannel(), methodProvider.handleMethodArgumentTypeMismatchException(e).getBody());
    }

    @MessageExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void handleHttpRequestMethodNotSupportedException(Principal principal, @Payload SocketRequest request,HttpRequestMethodNotSupportedException e) {
        template.convertAndSendToUser(principal.getName(), request.getResponseChannel(), methodProvider.handleHttpRequestMethodNotSupportedException(e).getBody());
    }

    @MessageExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(Principal principal, @Payload SocketRequest request,AccessDeniedException e) {
        template.convertAndSendToUser(principal.getName(), request.getResponseChannel(), methodProvider.handleAccessDeniedException(e).getBody());
    }

    @MessageExceptionHandler(BusinessException.class)
    public void handleBusinessException(Principal principal, @Payload SocketRequest request, BusinessException e) {
        template.convertAndSendToUser(principal.getName(), request.getResponseChannel(), methodProvider.handleBusinessException(e).getBody());
    }

    @MessageExceptionHandler(Exception.class)
    public void handleException(Principal principal, @Payload SocketRequest request,Exception e) {
        template.convertAndSendToUser(principal.getName(), request.getResponseChannel(), methodProvider.handleException(e).getBody());
    }

    private class MethodProvider implements GlobalExceptionHandlerInterface {
        @Override
        public ResponseEntity<StandardResponse<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
            return globalExceptionHandler.handleMethodArgumentNotValidException(e);
        }

        @Override
        public ResponseEntity<StandardResponse<ErrorResponse>> handleBindException(BindException e) {
            return globalExceptionHandler.handleBindException(e);
        }

        @Override
        public ResponseEntity<StandardResponse<ErrorResponse>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
            return globalExceptionHandler.handleMethodArgumentTypeMismatchException(e);
        }

        @Override
        public ResponseEntity<StandardResponse<ErrorResponse>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
            return globalExceptionHandler.handleHttpRequestMethodNotSupportedException(e);
        }

        @Override
        public ResponseEntity<StandardResponse<ErrorResponse>> handleAccessDeniedException(AccessDeniedException e) {
            return globalExceptionHandler.handleAccessDeniedException(e);
        }

        @Override
        public ResponseEntity<StandardResponse<ErrorResponse>> handleBusinessException(BusinessException e) {
            return globalExceptionHandler.handleBusinessException(e);
        }

        @Override
        public ResponseEntity<StandardResponse<ErrorResponse>> handleException(Exception e) {
            return globalExceptionHandler.handleException(e);
        }
    }


}


