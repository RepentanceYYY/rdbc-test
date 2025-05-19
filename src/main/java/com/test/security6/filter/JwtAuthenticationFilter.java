package com.test.security6.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.security6.entity.ClaimsContent;
import com.test.security6.entity.LoginUser;
import com.test.security6.utils.JwtUtil;
import com.test.security6.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil;
    private RedisUtil redisUtil;

    @Autowired
    JwtAuthenticationFilter(JwtUtil jwtUtil, RedisUtil redisUtil) {
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        ClaimsContent claimsContent;
        try {
            Claims claims = jwtUtil.parseToken(token);
            String claimsContentJson = (String) claims.get("claimsContent");
            claimsContent = new ObjectMapper().readValue(claimsContentJson, ClaimsContent.class);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("非法token");
        }
        redisUtil.setDbIndex(0);
        Object redisTokenObject = redisUtil.get(claimsContent.getUserId());
        if (redisTokenObject == null || !redisTokenObject.equals(token)) {
            throw new RuntimeException("会话已经失效");
        }

        redisUtil.setDbIndex(2);
        Object permissionObj = redisUtil.get(claimsContent.getUserEmail());
        if (permissionObj == null) {
            throw new RuntimeException("权限列表获取失败");
        }
        ObjectMapper mapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        List<String> permissionList = mapper.readValue((String) permissionObj, new TypeReference<>() {
        });
        List<SimpleGrantedAuthority> authorities = permissionList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(claimsContent.getUserId());
        loginUser.setEmail(claimsContent.getUserEmail());
        loginUser.setAuthorities(authorities);
        loginUser.setIsEnabled(true);
        loginUser.setIsAccountNonExpired(true);
        loginUser.setIsAccountNonLocked(true);
        loginUser.setIsCredentialsNonExpired(true);
        //如果token合法，就设置为有效用户
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    /**
     * 获取请求中的token
     *
     * @param request
     * @return
     */
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }
}
