package com.dambae200.dambae200.global.filter;

import com.dambae200.dambae200.domain.sessionInfo.exception.SessionInfoNotExistsException;
import com.dambae200.dambae200.domain.sessionInfo.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Slf4j
@Order(2)
@Component
@RequiredArgsConstructor
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {
            "/","/null/swagger-resources/*","/v3/*","/swagger-ui.html", "/swagger-ui.html#/*",
            "/swagger-resources","/swagger-resources/*", "/webjars/springfox-swagger-ui/*",
            "/api/favicon.ico", "/swagger-ui", "/swagger-ui/*",
            "/api/login", "/api/logout", "/api/forgot_pw", "/api/users",
            "/api/users/exists_by_email","/api/users/exists_by_nickname",
            "/stomp/store/*",
            "/api/cigarettes/multiple"
    };


    private final SessionService sessionService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        try {
            // 인증 체크가 필요한 URL이라면 인증 체크함
            if (isLoginCheckPath(requestURI)) {
                String accessToken = ((HttpServletRequest) request).getHeader("Authorization");
                log.info("accessToken : " + accessToken);
                sessionService.checkValidation(accessToken);
                }
            chain.doFilter(request, response); //다음 필터 진행. 없다면 서블릿 띄우기

        } catch (SessionInfoNotExistsException ex){
            log.info("미인증 사용자 요청 {}", requestURI);
            throw ex;
        // 로그인 안되었다는 예외 발생 후 종료 - 바깥 필터에서 처리
        }
        catch (Exception e) {
            throw e;
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크X
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}