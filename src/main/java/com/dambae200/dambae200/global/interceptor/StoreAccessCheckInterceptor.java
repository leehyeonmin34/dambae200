package com.dambae200.dambae200.global.interceptor;

import com.dambae200.dambae200.domain.access.service.AccessService;
import com.dambae200.dambae200.domain.sessionInfo.service.SessionService;
import com.dambae200.dambae200.domain.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreAccessCheckInterceptor implements HandlerInterceptor {

    private final AccessService accessService;
    private final SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String uri = request.getRequestURI();
        final String method = request.getMethod();

        if(isAdminAccessRequest(uri, method))
            checkAccess(request, true);
        else if(isStaffAccessRequest(uri, method))
            checkAccess(request, false);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean isAdminAccessRequest(String uri, String method){
        return uri.matches("^/api/accesses$") && HttpMethod.GET.matches(method)
                || uri.matches("^/api/accesses/[0-9]+/byadmin$");
    }

    private boolean isStaffAccessRequest(String uri, String method){
        return uri.matches("^/api/accesses/[0-9]+") || uri.equals("/api/cigarette_on_lists/display_order");
    }


    private void checkAccess(HttpServletRequest request, boolean isAdminCheck){
        final Long storeId = getStoreId(request);
        final Long userId = getUserId(request);

        if(isAdminCheck)
            accessService.checkAdminAccess(userId, storeId);
        else
            accessService.checkAccess(userId, storeId);

        log.info("Store 권한 검사 성공 - Request URI ===> " + request.getRequestURI());
    }

    private Long getStoreId(HttpServletRequest request){
        return Long.valueOf(request.getHeader("storeId"));
    }

    private Long getUserId(HttpServletRequest request){
        return Long.valueOf(sessionService.getSessionElseThrow(request.getHeader("Authorization")).getUserId());
    }

}
