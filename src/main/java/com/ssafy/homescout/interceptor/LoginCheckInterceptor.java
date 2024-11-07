package com.ssafy.homescout.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        //기존 세션이 있으면 반환, 없으면 null 반환, 새로운 세션을 생성하지 않음
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("loginUser") == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "미인증 사용자 요청");
        }

        return true;
    }

}
