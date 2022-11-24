package com.dambae200.dambae200.global.filter;

import com.dambae200.dambae200.domain.sessionInfo.exception.SessionInfoNotExistsException;
import com.dambae200.dambae200.domain.sessionInfo.service.SessionService;
import com.dambae200.dambae200.global.utils.RequestJsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Order(2)
@Component
@RequiredArgsConstructor
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {
            "/","/null/swagger-resources/*","/v3/*","/swagger-ui.html", "/swagger-ui.html#/*"
            , "/swagger-resources","/swagger-resources/*", "/webjars/springfox-swagger-ui/*", "/api/login", "/api/logout"
            ,"/api/users/exists_by_email","/api/users/exists_by_nickname", "/api/favicon.ico", "/api/users", "/swagger-ui", "/swagger-ui/*"
    };


    private final SessionService sessionService;
    private final RequestJsonMapper requestJsonMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        try {
            // 인증 체크가 필요한 URL이라면 인증 체크함
            if (isLoginCheckPath(requestURI)) {
//                StandardRequest<Object> requestBody = requestJsonMapper.mapToJson(request, StandardRequest.class);
                String accessToken = ((HttpServletRequest) request).getHeader("Authorization");
                log.info("accessToken" + accessToken);
                if (accessToken == null || !sessionService.existsByToken(accessToken)) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    // 로그인 안되었다는 예외 발생 후 종료
                    throw new SessionInfoNotExistsException();
                }
            }
            chain.doFilter(request, response); //다음 필터 진행. 없다면 서블릿 띄우기
        } catch (Exception e) {
            throw e; //예외 로깅 가능 하지만, 톰캣까지 예외를 보내주어야 함
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