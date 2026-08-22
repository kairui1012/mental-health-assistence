package com.example.aispringboot.util;

import com.example.aispringboot.common.ResultCode;
import com.example.aispringboot.config.securityConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String requestsUrl = request.getRequestURI();
        // 检查是否为公开路径
        return securityConfig.isPublicPATH(requestsUrl);
    }

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain

    ) throws ServletException, IOException {

        // 获取请求的URL和方法
        String requesUri = request.getRequestURI();
        String method = request.getMethod();

        // 第一步：提取Jwt token
        String token = JwtTokenUtil.extractTokenFromRequests(request);
        if (StringUtils.hasText(token)){
            // 第二步：验证token并获取用户信息
            JwtTokenUtil.tokenVerificationResult validationResult = JwtTokenUtil.validateToken(token);
            if (validationResult != null && validationResult.isValid()){

            }else{
                clearSecurityContext();
                ResponseUtil.WriteError(response,ResultCode.TOKEN_INVALID);
            }
        }
        else{
            // 清理上下文
            clearSecurityContext();
            ResponseUtil.WriteError(response, ResultCode.ACCESS_UNAUTHORIZED);
            return;
        }
        // 继续过滤器链
        chain.doFilter(request,response);
    }

    // 清理Spring Security的上下文
    private void clearSecurityContext(){
        SecurityContextHolder.clearContext();
    }
}
