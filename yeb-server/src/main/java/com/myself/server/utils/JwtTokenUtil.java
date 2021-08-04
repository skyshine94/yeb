package com.myself.server.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken工具类
 *
 * @author Wei
 * @since 2021/5/28
 */
@Component
public class JwtTokenUtil {

    //负载中用户名和创建时间的key
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.secret}")
    private String secret;

    //根据用户信息创建token
    //根据用户信息创建负载，根据负载创建token
    public String createTokenByUser(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return createTokenByClaims(claims);
    }

    //根据负载创建Token
    private String createTokenByClaims(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims) //负载
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000)) //失效时间
                .signWith(SignatureAlgorithm.HS512, secret) //签名
                .compact();
    }

    //从token中获取登录用户名
    //从token中获取负载，根据负载获取用户名
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    //从token中获取负载
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret) //密钥
                    .parseClaimsJws(token) //token
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    //验证token是否有效
    //验证用户名且是否有效
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    //验证token是否失效
    private boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expirationDate = claims.getExpiration();
        return expirationDate.before(new Date());
    }

}
