package com.dambae200.dambae200.global.interceptor;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsInterceptor implements HandlerInterceptor {

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        return HttpMethod.OPTIONS.matches(request.getMethod());
//        // 요청이 OPTIONS일 경우 별도의 인증과정 없이 해당 요청을 받아들인다.
//        // preflight엔 인증헤더가 없기 때문에, preflight를 정상처리하기 위해서 필요하다.
//    }
}
