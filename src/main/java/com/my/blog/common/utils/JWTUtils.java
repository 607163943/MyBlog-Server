package com.my.blog.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expire}")
    private long expire;

    /**
     * 创建JWT令牌
     * @param userId 用户id
     * @return 令牌
     */
    public String createJWTWithUserId(Long userId) {
        return JWT.create()
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expire))
                .withSubject("admin")
                .withClaim("userId",userId)
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * 获取用户id
     * @param jwt 令牌
     * @return 用户id
     */
    public Long getUserId(String jwt) {
        return JWT.decode(jwt).getClaim("userId").asLong();
    }

    /**
     * 验证JWT令牌
     * @param jwt 令牌
     */
    public void verifyJWT(String jwt) {
        JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(jwt);
    }
}
