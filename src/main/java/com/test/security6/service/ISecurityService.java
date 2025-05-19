package com.test.security6.service;

import com.test.security6.entity.db.*;
import com.test.security6.entity.dto.UserRoleDto;

import java.util.ArrayList;
import java.util.List;

public interface ISecurityService {
    RoleInfo addRoleInfo(RoleInfo roleInfo);
    RoleInfo updateRoleInfo(RoleInfo roleInfo);
    RoleInfo deleteRoleInfo(RoleInfo roleInfo);
    ArrayList<RoleInfo> getRoleInfos(ArrayList<RoleInfo> roleInfoList);
    UserRoleLink addRoleToUser(UserRoleLink userRoleLink);
    Permission addPermission(Permission permission);
    Permission updatePermission(Permission permission);
    Permission deletePermission(Permission permission);
    RolePermissionLink addPermissionToRole(RolePermissionLink rolePermissionLink);
    RolePermissionLink deletePermissionToRole(RolePermissionLink rolePermissionLink);

    List<RoleInfo> getAllRole();
    List<UserRoleDto> getUserAllRole(UserInfo userInfo);

    List<Permission> getAllPermission();
}
