package com.ssafy.homescout.interceptor;

import com.ssafy.homescout.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // OPTIONS 제외
        if(HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        // POST /user -> 회원가입일 때는 토큰 검사 X
        if(HttpMethod.POST.matches(request.getMethod()) && request.getRequestURI().equals("/user")) {
            return true;
        }

        String token = resolveToken(request);
        if (token != null && jwtUtil.validateToken(token)) {
            // Set authentication information in request
            request.setAttribute("userId", jwtUtil.getUserId(token));
            return true;
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
