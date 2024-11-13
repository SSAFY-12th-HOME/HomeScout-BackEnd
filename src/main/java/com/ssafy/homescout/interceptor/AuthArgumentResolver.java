package com.ssafy.homescout.interceptor;

import com.ssafy.homescout.annotation.Auth;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthAnnotation = parameter.hasParameterAnnotation(Auth.class); // @Auth 어노테이션을 갖고있는지
        boolean hasLongType = Long.class.isAssignableFrom(parameter.getParameterType()); // Long 타입인지

        return hasAuthAnnotation && hasLongType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Auth annotation = parameter.getParameterAnnotation(Auth.class);

        // Http Request 객체 추출
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        // @Auth(required=false)인 경우 null 반환
        if(!annotation.required()) {
            return null;
        }

        return request.getAttribute("userId");
    }

}
