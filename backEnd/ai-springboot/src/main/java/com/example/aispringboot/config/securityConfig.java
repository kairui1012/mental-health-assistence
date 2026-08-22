package com.example.aispringboot.config;

import cn.hutool.core.text.AntPathMatcher;
import com.example.aispringboot.util.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity

public class securityConfig {

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private static final String[] PUBLIC_PATHS={
            "/","/api/test","/api/user/login","/api/user/add"
    };

    public static Boolean isPublicPATH(String requestUrl){
        for (String publicPath:PUBLIC_PATHS){
            if (antPathMatcher.match(publicPath,requestUrl)){
                return true;
            }
        }
        return false;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
            // 禁用csrf保护 （API服务已搬家不需要）
            .csrf(AbstractHttpConfigurer::disable)
            // 配置会话管理为无状态（JWT需要）
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 配置请求授权规则
            .authorizeHttpRequests(auth->auth
                    // 公开路经（无需登录）
                    .requestMatchers(PUBLIC_PATHS).permitAll()
                    // 其他请求都需要登录认证
                    .anyRequest().authenticated()
            )
            // 添加JWT认证过滤器
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
