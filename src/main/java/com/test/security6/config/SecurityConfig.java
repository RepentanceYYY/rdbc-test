package com.test.security6.config;

import com.test.security6.filter.JwtAuthenticationFilter;
import com.test.security6.handler.CustomAccessDeniedHandler;
import com.test.security6.handler.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;

    SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtFilter = jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        loadingHttp(http);
        return http.build();
    }

    private HttpSecurity loadingHttp(HttpSecurity http) throws Exception {
        //白名单
        List<String> whitelist = new ArrayList<>() {
        };
        whitelist.add("/user/register");
        whitelist.add("/user/login");

        // Swagger 相关
        whitelist.add("/v3/api-docs/**");
        whitelist.add("/swagger-ui.html");
        whitelist.add("/swagger-ui/**");
        whitelist.add("/swagger-resources/**");
        whitelist.add("/webjars/**");
        whitelist.add("/favicon.ico");

        // 静态资源
        whitelist.add("/**/*.css");
        whitelist.add("/**/*.js");
        whitelist.add("/**/*.html");
        whitelist.add("/*.html");
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    RequestMatcher[] matchers = whitelist.stream().map(AntPathRequestMatcher::new).toArray(RequestMatcher[]::new);
                    auth.requestMatchers(matchers).permitAll();
                    auth.anyRequest().authenticated();
                }).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> {
                    exception.accessDeniedHandler(new CustomAccessDeniedHandler())
                            .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
                });

        return http;
    }
}
