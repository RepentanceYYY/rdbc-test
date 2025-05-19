package com.test.security6.controller;

import com.test.security6.comm.ResponseResult;
import com.test.security6.entity.db.UserInfo;
import com.test.security6.entity.db.UserRoleLink;
import com.test.security6.entity.db.RoleInfo;
import com.test.security6.entity.db.Permission;
import com.test.security6.entity.db.RolePermissionLink;
import com.test.security6.entity.dto.UserRoleDto;
import com.test.security6.service.ISecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/security")
@Tag(name = "安全控制器", description = "包括用户分配角色，角色分配权限")
public class SecurityController {
    @Autowired
    private ISecurityService securityService;

    @Operation(summary = "添加角色")
    @PostMapping("/addRole")
    @PreAuthorize("hasAnyAuthority('security:addRole')")
    public RoleInfo addRole(@RequestBody RoleInfo roleInfo) {
        RoleInfo result = securityService.addRoleInfo(roleInfo);
        return result;
    }

    @Operation(summary = "查询所有角色")
    @GetMapping("/getAllRole")
    @PreAuthorize("hasAnyAuthority('security:getUserAllRole')")
    public ResponseResult getAllRole() {
        List<RoleInfo> result = securityService.getAllRole();
        return ResponseResult.success(result);
    }

    @Operation(summary = "给用户分配角色")
    @PostMapping("/assignRoles")
    public UserRoleLink addRoleToUser(@RequestBody UserRoleLink userRoleLink) {
        UserRoleLink result = securityService.addRoleToUser(userRoleLink);
        return result;
    }

    @Operation(summary = "添加权限")
    @PostMapping("/addPermission")
    @PreAuthorize("hasAnyAuthority('security:addPermission')")
    public Permission addPermission(@RequestBody Permission permission) {
        Permission result = securityService.addPermission(permission);
        return result;
    }

    @Operation(summary = "给角色分配权限")
    @PostMapping("/assignPermission")
    public RolePermissionLink assignPermission(@RequestBody RolePermissionLink rolePermissionLink) {
        RolePermissionLink result = securityService.addPermissionToRole(rolePermissionLink);
        return result;
    }

    @GetMapping("/getUserAllRole/{userId}")
    @Operation(summary = "查询用户所有角色")
    @PreAuthorize("hasAnyAuthority('security:getUserAllRole')")
    public List<UserRoleDto> getUserWithRole(@PathVariable("userId") String userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        List<UserRoleDto> userAllRole = securityService.getUserAllRole(userInfo);
        return userAllRole;
    }
}
