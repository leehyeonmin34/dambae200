package com.dambae200.dambae200.global.error;

import com.dambae200.dambae200.global.common.StandardResponse;
import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements GlobalExceptionHandlerInterface{
    /**
     *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     *  HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     *  주로 @RequestBody, @RequestPart 어노테이션에서 발생
     * @return
     */
    @Override
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        final StandardResponse response = StandardResponse.builder().errorResponse(errorResponse).status(ErrorCode.INVALID_INPUT_VALUE.getStatus()).build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.INVALID_INPUT_VALUE.getStatus()));
    }

    /**
     * @ModelAttribute 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     * @return
     */
    @Override
    @ExceptionHandler(BindException.class)
    public ResponseEntity<StandardResponse<ErrorResponse>> handleBindException(BindException e) {
        log.error("handleBindException", e);
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return StandardResponse.of(errorResponse);
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     * @return
     */
    @Override
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardResponse<ErrorResponse>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        final ErrorResponse errorResponse = ErrorResponse.of(e);
        return StandardResponse.of(errorResponse);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     * @return
     */
    @Override
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardResponse<ErrorResponse>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return StandardResponse.of(errorResponse);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @Override
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardResponse<ErrorResponse>> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
        return StandardResponse.of(errorResponse);
    }

    // business 로직에 의해 처리되어야할 예외 ex) 중복된 아이디, 쿠폰 기한 만료
    @Override
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardResponse<ErrorResponse>> handleBusinessException(final BusinessException e) {
        log.error("handleBusinessException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse errorResponse = ErrorResponse.of(errorCode, e.getMessage());
        return StandardResponse.of(errorResponse);
    }

    // 그 외 모든 예외
    @Override
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse<ErrorResponse>> handleException(Exception e) {
        log.error("Exception", e);
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return StandardResponse.of(errorResponse);
    }

}
