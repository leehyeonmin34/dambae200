package com.dambae200.dambae200.global.error;

import com.dambae200.dambae200.global.error.exception.ErrorCode;
import lombok.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse{
    private int status;
    private String errorCode;
    private String errorType;
    private String message;
    private List<FieldError> errors = new ArrayList<>();

    private ErrorResponse(ErrorCode errorCode, List<ErrorResponse.FieldError> errors, String message){
        this.status = errorCode.getStatus();
        this.errorCode = errorCode.getCode();
        this.message = message;
        this.errors = errors;
        this.errorType = errorCode.getType();
    }

//    private ErrorResponse of(ErrorCode errorCode, List<ErrorResponse.FieldError> errors){
//        return new ErrorResponse(errorCode, errors, null);
//    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code, null, null);
    }

    public static ErrorResponse of(ErrorCode errorCode, List<ErrorResponse.FieldError> errors){
        return new ErrorResponse(errorCode, errors, null);
    }

    public static ErrorResponse of(final ErrorCode code, final String message) {
        return new ErrorResponse(code, null, message);
    }

    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult){
        return ErrorResponse.of(errorCode, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(e.getName(), value, e.getErrorCode());
        return ErrorResponse.of(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        public static List<ErrorResponse.FieldError> of(String name, String value, String errorCode){
            List<ErrorResponse.FieldError> feildErrors = new ArrayList<>();
            feildErrors.add(new FieldError(name, value, errorCode));
            return feildErrors;
        }

        public static List<ErrorResponse.FieldError> of(final BindingResult result){
            final List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(error.getField()
                            , error.getRejectedValue() == null ? "" : error.getRejectedValue().toString()
                            , error.getDefaultMessage())).collect(Collectors.toList());
        }

    }

}