package com.dambae200.dambae200.global.error;

import com.dambae200.dambae200.global.common.dto.StandardResponse;
import com.dambae200.dambae200.global.error.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;

public interface GlobalExceptionHandlerInterface {


    ResponseEntity<StandardResponse<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e);

    /**
     * @ModelAttribute 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    ResponseEntity<StandardResponse<ErrorResponse>> handleBindException(BindException e);

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    ResponseEntity<StandardResponse<ErrorResponse>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e);

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    ResponseEntity<StandardResponse<ErrorResponse>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e);

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    ResponseEntity<StandardResponse<ErrorResponse>> handleAccessDeniedException(AccessDeniedException e);

    // business 로직에 의해 처리되어야할 예외 ex) 중복된 아이디, 쿠폰 기한 만료
    ResponseEntity<StandardResponse<ErrorResponse>> handleBusinessException(final BusinessException e);

    // 그 외 모든 예외
    ResponseEntity<StandardResponse<ErrorResponse>> handleException(Exception e);
}

