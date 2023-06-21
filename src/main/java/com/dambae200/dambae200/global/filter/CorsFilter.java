package com.dambae200.dambae200.global.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    private final static String[] allowedOrigins = {"http://localhost:5500", "http://192.100.0.223:5500", "http://192.100.3.182:5500", "http://49.50.164.244:9090", "http://49.50.164.244:9999", "http://49.50.164.244", "http://49.50.164.244:80"};
    // 로컬 VS Code
    // ncp-main nginx
    // ncp-main 프론트엔드 포트

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        System.out.println(request.getHeader("Origin"));
        int idx = Arrays.asList(allowedOrigins).indexOf(request.getHeader("Origin"));
        if(idx > -1) response.setHeader("Access-Control-Allow-Origin", allowedOrigins[idx]);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Authorization, StoreId");

        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }
}
