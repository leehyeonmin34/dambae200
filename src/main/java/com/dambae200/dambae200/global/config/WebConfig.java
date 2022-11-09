package com.dambae200.dambae200.global.config;

import com.dambae200.dambae200.global.interceptor.CorsInterceptor;
import com.dambae200.dambae200.global.interceptor.LoggerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoggerInterceptor loggerInterceptor;

    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loggerInterceptor)
//                .addPathPatterns("/**");
//    }

    @Override
    // CORS 정책을 허용해줄 url과 메서드를 재정의해준다
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/api/**") // url path
                .allowedMethods(ALLOWED_METHOD_NAMES.split(",")) // 메서드 종류
                .exposedHeaders(HttpHeaders.LOCATION); // 서버에 반환해줄 헤더 지정
        // origin은 별도 명시를 안하면 DEFAULT_PERMIT_ALL로 모두 허용하게 된다.
    }




}