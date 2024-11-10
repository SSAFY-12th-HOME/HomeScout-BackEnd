package com.ssafy.homescout.config;

import com.ssafy.homescout.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String[] EXCLUDE_PATH = {
            "/error", "/api-test", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**",
            "/user", "/user/login", "/user/logout",
            "/auth/nickname-duplication", "/auth/email-duplication", "/auth/email-code", "/auth/email",
            "/apt", "/apt/**",
            "/notice/**",
            "/sale/**"
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
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/**") /// 인터셉터를 적용할 URL 패턴 (모든 경로 적용)
                .excludePathPatterns(EXCLUDE_PATH); // 인터셉터 적용을 제외할 URL 패턴
    }

}
