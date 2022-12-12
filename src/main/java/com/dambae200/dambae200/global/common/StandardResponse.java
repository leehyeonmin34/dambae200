package com.dambae200.dambae200.global.common;

import com.dambae200.dambae200.global.error.ErrorResponse;
import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@ToString
@JsonInclude(JsonInclude.Include.ALWAYS)
public class StandardResponse<T> {

    @NotNull
    private int status;

    @Nullable
    private ErrorResponse errorResponse;

    @Nullable
    private T data;

    public static <T> ResponseEntity<StandardResponse<T>> ofOk(T data){
        StandardResponse<T> response = ok(data);
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<StandardResponse<ErrorResponse>> of(final ErrorResponse errorResponse){
        int status = errorResponse.getStatus();
        final StandardResponse response = StandardResponse.builder().errorResponse(errorResponse).status(status).build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(status));
    }

    public static <T> StandardResponse<T> ok(T data){
        return (StandardResponse<T>) StandardResponse.builder().data(data).status(HttpStatus.OK.value()).build();
    }

    public static <T> StandardResponse<T> error(BusinessException e){
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse errorResponse = ErrorResponse.of(errorCode, e.getMessage());
        return (StandardResponse<T>) StandardResponse.builder().errorResponse(errorResponse).status(errorCode.getStatus()).build();
    }





}