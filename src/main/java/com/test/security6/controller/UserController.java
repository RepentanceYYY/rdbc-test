package com.test.security6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.security6.comm.ResponseResult;
import com.test.security6.entity.ClaimsContent;
import com.test.security6.entity.basic.UserBasicInfo;
import com.test.security6.entity.db.Permission;
import com.test.security6.entity.db.UserInfo;
import com.test.security6.service.ISecurityService;
import com.test.security6.service.IUserService;
import com.test.security6.utils.JwtUtil;
import com.test.security6.utils.RedisUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Tag(name = "用户控制器", description = "用户控制器描述")
@SecurityRequirement(name = "bearerAuth") // 声明整个控制器需要token
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${custom.root.email}")
    private String rootEmail;
    @Value("${custom.root.password}")
    private String rootPassword;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public ResponseResult addUser(@RequestBody UserBasicInfo userBasicInfo) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserEmail(userBasicInfo.getUserEmail());
        userInfo.setPassword(userBasicInfo.getPassword());
        UserInfo resUser = userService.addUserInfo(userInfo);
        return ResponseResult.success(resUser);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public ResponseResult login(@RequestBody UserBasicInfo userBasicInfo) throws JsonProcessingException {

        if (!LoginDataCheck(userBasicInfo)) {
            return ResponseResult.error("邮箱和密码不能为空");
        }
        UserInfo user = new UserInfo();
        //非管理员
        if (!loginRoot(userBasicInfo)) {
            user = userService.getUserByEmail(userBasicInfo.getUserEmail());
            if (user == null) {
                return ResponseResult.error("邮箱未注册");
            }
            if (!user.getPassword().equals(userBasicInfo.getPassword())) {
                return ResponseResult.error("密码错误");
            }
        } else {
            user.setId("root:" + UUID.randomUUID());
            user.setUserEmail(userBasicInfo.getUserEmail());

        }

        ClaimsContent claimsContent = new ClaimsContent(user.getId(), user.getUserEmail(), System.currentTimeMillis(), user.getIsAccountNonExpired());
        String claimsContentJson = new ObjectMapper().writeValueAsString(claimsContent);
        Map<String, Object> map = new HashMap<>();
        map.put("claimsContent", claimsContentJson);
        String token = jwtUtil.generateToken(user.getId(), map);
        redisUtil.setDbIndex(0);
        redisUtil.set(user.getId(), token);
        List<Permission> permissionByEmail = new ArrayList<>();
        //非超级管理员，查找它的权限
        if (!loginRoot(userBasicInfo)) {
            permissionByEmail = userService.getPermissionByEmail(user.getUserEmail());
        }
        //超级管理员，赋予所有权限
        else {
            permissionByEmail = securityService.getAllPermission();
        }
        List<String> permissionCodeList = permissionByEmail.stream().map(Permission::getPermissionCode).collect(Collectors.toList());
        redisUtil.setDbIndex(2);
        redisUtil.set(user.getUserEmail(), new ObjectMapper().writeValueAsString(permissionCodeList));
        return ResponseResult.success(token);
    }

    private Boolean LoginDataCheck(UserBasicInfo userInfo) {
        if (!StringUtils.hasText(userInfo.getUserEmail()) || !StringUtils.hasText(userInfo.getPassword())) {
            return false;
        }
        return true;
    }

    protected boolean loginRoot(UserBasicInfo userBasicInfo) {
        if (!rootEmail.equals(userBasicInfo.getUserEmail()) || !rootPassword.equals(userBasicInfo.getPassword())) {
            return false;
        }
        return true;
    }
}
