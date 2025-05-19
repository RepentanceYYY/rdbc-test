package com.test.security6.handler;

import com.test.security6.comm.ResponseResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 已知异常：无权限访问
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResult handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseResult.error(403, "无权访问该资源", null);
    }

    // 已知异常：未登录
    @ExceptionHandler(AuthenticationException.class)
    public ResponseResult handleAuthenticationException(AuthenticationException ex) {
        return ResponseResult.error(401, "未认证，请登录", null);
    }

    // ✅ 其他未识别异常
    @ExceptionHandler(Exception.class)
    public ResponseResult handleOtherExceptions(Exception ex) {
        // 可以记录日志，方便排查
        ex.printStackTrace();
        return ResponseResult.error(500, "未识别异常:" + ex.getMessage(), null);
    }
}
