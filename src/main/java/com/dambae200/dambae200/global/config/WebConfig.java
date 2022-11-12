package com.dambae200.dambae200.global.config;

import com.dambae200.dambae200.domain.sessionInfo.service.SessionService;
import com.dambae200.dambae200.global.filter.ExceptionHandlerFilter;
//import com.dambae200.dambae200.global.filter.LoginCheckFilter;
import com.dambae200.dambae200.global.filter.LoginCheckFilter;
import com.dambae200.dambae200.global.interceptor.CorsInterceptor;
import com.dambae200.dambae200.global.interceptor.LoggerInterceptor;
import com.dambae200.dambae200.global.utils.RequestJsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    final private SessionService sessionService;
    final private RequestJsonMapper requestJsonMapper;

    @Autowired
    private LoggerInterceptor loggerInterceptor;

    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH,CUSTOM";

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loggerInterceptor)
//                .addPathPatterns("/**");
//    }

    @Override
    // CORS 정책을 허용해줄 url과 메서드를 재정의해준다
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**") // url path
                .allowedMethods(ALLOWED_METHOD_NAMES.split(",")) // 메서드 종류
                .exposedHeaders(HttpHeaders.LOCATION)// 서버에 반환해줄 헤더 지정
//                .allowedOriginPatterns("*")
//                .allowedOriginPatterns("app://localhost:*")
//                .allowedOriginPatterns("*") // 허용할 도메인
//                .allowedOrigins("myapp://dambae200.com")
        ;

        // origin은 별도 명시를 안하면 DEFAULT_PERMIT_ALL로 모두 허용하게 된다.
    }

//    @Bean
//    public FilterRegistrationBean filterExceptionHandlerFilter(){
//        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new ExceptionHandlerFilter()); //내가 구현한 필터 넣기
//        filterRegistrationBean.setOrder(1); //필터 체인할 때 가장 먼저 실행
//        filterRegistrationBean.addUrlPatterns("/*"); //모든 요청 url에 대해 실행
//        return filterRegistrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean loginFilter(){
//        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new LoginCheckFilter(sessionService, requestJsonMapper)); //내가 구현한 필터 넣기
//        filterRegistrationBean.setOrder(2); //필터 체인할 때 두번째 먼저 실행
//        filterRegistrationBean.addUrlPatterns("/*"); //모든 요청 url에 대해 실행
//        return filterRegistrationBean;
//    }





}