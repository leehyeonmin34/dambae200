package com.dambae200.dambae200.global.filter;

import com.dambae200.dambae200.domain.sessionInfo.exception.SessionInfoNotExistsException;
import com.google.gson.Gson;
import com.dambae200.dambae200.global.error.ErrorResponse;
import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Order(1)
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            filterChain.doFilter(request,response);
        } catch (SessionInfoNotExistsException ex) {
            log.error("exception exception handler filter");
            setErrorResponse(HttpStatus.valueOf(ex.getErrorCode().getStatus()), response, ex);
        } catch (RuntimeException ex){
            log.error("runtime exception exception handler filter");
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,response,ex);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, RuntimeException ex){
        // HTML이 UTF-8 형식이라는 것을 브라우저에게 전달
        response.setStatus(status.value());
        response.setContentType("application/json; charset=utf-8");

        ex.printStackTrace();
        ErrorCode errorCode = ex instanceof BusinessException ? ((BusinessException) ex).getErrorCode() : ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, ex.getMessage());
        try{
            Gson gson = new Gson();
            String json = gson.toJson(errorResponse);
            System.out.println(json);
            response.getWriter().write(json);
        }catch (IOException e){
            e.printStackTrace();
        }
    }



}
