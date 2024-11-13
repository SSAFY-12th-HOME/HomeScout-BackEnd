package com.ssafy.homescout.config;

import com.ssafy.homescout.interceptor.AuthArgumentResolver;
import com.ssafy.homescout.interceptor.JwtAuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthArgumentResolver authArgumentResolver;
    private final JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

    private final String[] EXCLUDE_PATH = {
            "/error", "/api-test", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**",
            "/user/login", "/user/password",
            "/auth/nickname-duplication", "/auth/email-duplication", "/auth/email-code", "/auth/email",
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/**") /// 인터셉터를 적용할 URL 패턴 (모든 경로 적용)
                .excludePathPatterns(EXCLUDE_PATH); // 인터셉터 적용을 제외할 URL 패턴
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver); // @Auth 어노테이션 등록
    }
}
