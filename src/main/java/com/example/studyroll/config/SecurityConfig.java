package com.example.studyroll.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(
                        mvcMatcherBuilder.pattern("/"),                    mvcMatcherBuilder.pattern("/login"),                mvcMatcherBuilder.pattern("/sign-up")
                        ,mvcMatcherBuilder.pattern("/check-email"),         mvcMatcherBuilder.pattern("/check-email-token"),    mvcMatcherBuilder.pattern("/email-login")
                        ,mvcMatcherBuilder.pattern("/check-email-login"),   mvcMatcherBuilder.pattern("/login-link"),           mvcMatcherBuilder.pattern("/profile/*")
                        , mvcMatcherBuilder.pattern("/images/*") // 정적 이미지들도 security로 허용해줘야함.
                ).permitAll() // 권한 확인 없이 확인할 수 있는 페이지들.
                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "/profile/*")).permitAll() // 프로필 요청은 GET만 허용하고 나머지는 author가 필요함!
                .anyRequest().authenticated()
        );

        return http.build();
    }

}

