package com.test.security6.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${custom.jwt.key}")
    private String SECRET_KEY;
    @Value("${custom.jwt.period-of-validity}")
    private long EXPIRATION_MS;//一周
    private Key key;
    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * 生成token
     * @param subject
     * @param claims
     * @return
     */
    public String generateToken(String subject, Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析token
     * @param token
     * @return
     * @throws JwtException
     */
    public Claims parseToken(String token) throws JwtException{
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 判断是否过期
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token){
        try{
            return parseToken(token).getExpiration().before(new Date());
        }catch (JwtException e){
            return true;
        }
    }

    /**
     * 获取token中的主题
     * @param token
     * @return
     */
    public String getSubject(String token){
        return parseToken(token).getSubject();
    }
}
