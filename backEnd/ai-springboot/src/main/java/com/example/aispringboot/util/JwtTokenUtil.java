package com.example.aispringboot.util;
import ch.qos.logback.core.util.StringUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.aispringboot.config.JwtConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class JwtTokenUtil implements ApplicationContextAware {
    private static final String ISSUER = "kairui";
    private static ApplicationContext applicationContext;

    // 用于在静态工具类中获取Spring容器管理的Bean
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext){
        JwtTokenUtil.applicationContext = applicationContext;
    }

    private static JwtConfig getJwtConfig(){
        return applicationContext.getBean(JwtConfig.class);
    }

    // 生成token方法
    public static String generateToken(Long userId,String username,Integer roleType){
        try{
            // 获取JWT的配置
            JwtConfig jwtConfig = getJwtConfig();
            // 生成签名的算法
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
            // 生成过期的时间
            Date expiration = new Date(System.currentTimeMillis()+jwtConfig.getExpiration());

            String token = JWT.create()
                    .withClaim("userId",userId)
                    .withClaim("username",username)
                    .withClaim("roleType",roleType)
                    // 设置过期时间
                    .withExpiresAt(expiration).
                    // 设置签发时间
                            withIssuedAt(new Date())
                    // 设置签发者
                    .withIssuer(ISSUER)
                    // 生成签名
                    .sign(algorithm);

            return token;
        } catch (Exception e) {
            throw new RuntimeException("生成token失败"+e);
        }
    }

    // 提取token
    public static String extractTokenFromRequests(HttpServletRequest request){
        if (request == null){
            return null;
        }
        String tokenHeader = request.getHeader("token");
        if(StringUtils.hasText(tokenHeader)){
            return tokenHeader;
        }
        return null;
    }

    // 验证token
    public static tokenVerificationResult validateToken(String token){
        DecodedJWT decodedJWT = verifyToken(token);
        Long userId = decodedJWT.getClaim("userId").asLong();
        String username = decodedJWT.getClaim("username").asString();

        // 角色类型
        Integer roleType = null;
        try{
            roleType = decodedJWT.getClaim("roleType").asInt();
        }
        catch (Exception e){
            String roleTypeStr = decodedJWT.getClaim("roleType").asString();
            if (StringUtils.hasText(roleTypeStr)){
                roleType = Integer.valueOf(roleTypeStr);
            }
        }

        // 判断是否正确
        if (userId != null && StringUtils.hasText(username) && roleType != null)
        {
            return new tokenVerificationResult(userId,username,roleType,true);
        }
        return null;
    }

    // 验证token有效性
    public static DecodedJWT verifyToken(String token){
        if(!StringUtils.hasText(token)){
            throw new JWTVerificationException(("Token不能为空"));
        }
        // token解码
        JwtConfig jwtConfig = getJwtConfig();
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        return verifier.verify(token);
    }


    // Token验证结果封装类
    @Getter
    public static class tokenVerificationResult{
        private final Long userId;
        private final String username;
        private final Integer roleType;
        private final Boolean valid;

        public tokenVerificationResult(Long userId,String username,Integer roleType,Boolean valid){
            this.userId = userId;
            this.username = username;
            this.roleType = roleType;
            this.valid = valid;
        }

        public boolean isValid(){
            return Boolean.TRUE.equals(valid);
        }
    }

}
