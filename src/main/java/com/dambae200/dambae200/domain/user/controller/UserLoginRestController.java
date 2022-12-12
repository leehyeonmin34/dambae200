package com.dambae200.dambae200.domain.user.controller;

import com.dambae200.dambae200.domain.sessionInfo.domain.SessionInfo;
import com.dambae200.dambae200.domain.sessionInfo.service.SessionService;
import com.dambae200.dambae200.domain.user.dto.UserGetResponse;
import com.dambae200.dambae200.domain.user.dto.UserLoginRequest;
import com.dambae200.dambae200.domain.user.dto.UserLoginResponse;
import com.dambae200.dambae200.domain.user.exception.LoginInfoNotMatched;
import com.dambae200.dambae200.domain.user.service.UserFindService;
import com.dambae200.dambae200.domain.user.service.UserLoginService;
import com.dambae200.dambae200.domain.user.service.UserUpdateService;
import com.dambae200.dambae200.global.common.StandardResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"로그인 API"})
public class UserLoginRestController {

    final UserLoginService userLoginService;
    final UserFindService userFindService;
    final UserUpdateService userUpdateService;
    final SessionService sessionService;
//    final RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/login")
    @ApiOperation(value = "아이디와 비밀번호를 받아 로그인하고, Access Token을 응답받는다")
    public ResponseEntity<StandardResponse<UserLoginResponse>> login(@RequestHeader(value="User-Agent") String userAgent, @RequestBody @Valid UserLoginRequest request) throws LoginInfoNotMatched {
        UserGetResponse userInfo = userLoginService.authenticate(request.getEmail(), request.getPw());
        SessionInfo sessionInfo = sessionService.registerSession(userInfo, userAgent);
        UserLoginResponse response = new UserLoginResponse(userInfo, sessionInfo.getAccessToken());

        return StandardResponse.ofOk(response);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃처리하며 Access Token을 폐기한다")
    public ResponseEntity<StandardResponse<String>> logout(@RequestHeader(value="Authorization") String accessToken) {
        sessionService.removeSession(accessToken);
        return StandardResponse.ofOk("로그아웃 되었습니다. 세션 id = " + accessToken);
    }

    @GetMapping("/am_i_logged_in")
    @ApiOperation(value = "로그인된 사용자만 접근할 수 있다")
    @ApiResponse(code = 401, message = "액세스 토큰이 없거나 만료된 사용자일때")
    public ResponseEntity<StandardResponse<String>> home(){
        return StandardResponse.ofOk("로그인된 사용자가 맞습니다.");
    }

    @PostMapping("/forgot_pw")
    public ResponseEntity<StandardResponse<String>> forgotPw(@RequestParam String email){
        userUpdateService.sendNewPwAndChangePw(email);
        return StandardResponse.ofOk("이메일을 전송했습니다.");

    }


}

