package com.test.security6.service.impl;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.test.security6.entity.db.RoleInfo;
import com.test.security6.entity.db.UserRoleLink;
import com.test.security6.entity.db.UserInfo;
import com.test.security6.entity.db.RolePermissionLink;
import com.test.security6.entity.db.Permission;
import com.test.security6.entity.dto.UserRoleDto;
import com.test.security6.mapper.*;
import com.test.security6.service.ISecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SecurityServiceImpl implements ISecurityService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleLinkMapper userRoleLinkMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionLinkMapper rolePermissionLinkMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public RoleInfo addRoleInfo(RoleInfo roleInfo) {
        roleInfo.setId(UUID.randomUUID().toString());
        roleInfo.setCreateTimestamp(System.currentTimeMillis());
        int insert = roleMapper.insert(roleInfo);
        if (insert > 0) {
            return roleInfo;
        } else {
            return null;
        }
    }

    @Override
    public RoleInfo updateRoleInfo(RoleInfo roleInfo) {
        return null;
    }

    @Override
    public RoleInfo deleteRoleInfo(RoleInfo roleInfo) {
        return null;
    }

    @Override
    public ArrayList<RoleInfo> getRoleInfos(ArrayList<RoleInfo> roleInfoList) {
        return null;
    }

    @Override
    public UserRoleLink addRoleToUser(UserRoleLink userRoleLink) {
        userRoleLink.setId(UUID.randomUUID().toString());
        int insert = userRoleLinkMapper.insert(userRoleLink);
        if (insert > 0) {
            return userRoleLink;
        } else {
            return null;
        }
    }

    @Override
    public Permission addPermission(Permission permission) {
        permission.setId(UUID.randomUUID().toString());
        permission.setPermissionCode(permission.getPermissionCode());
        permission.setIsDelete(false);
        permission.setCreateTimestamp(System.currentTimeMillis());
        int insert = permissionMapper.insert(permission);
        if (insert > 0) {
            return permission;
        } else {
            return null;
        }
    }

    @Override
    public Permission updatePermission(Permission permission) {
        return null;
    }

    @Override
    public Permission deletePermission(Permission permission) {
        return null;
    }

    @Override
    public RolePermissionLink addPermissionToRole(RolePermissionLink rolePermissionLink) {
        rolePermissionLink.setId(UUID.randomUUID().toString());
        int insert = rolePermissionLinkMapper.insert(rolePermissionLink);
        if (insert > 0) {
            return rolePermissionLink;
        } else {
            return null;
        }
    }

    @Override
    public RolePermissionLink deletePermissionToRole(RolePermissionLink rolePermissionLink) {
        return null;
    }

    @Override
    public List<RoleInfo> getAllRole() {
        List<RoleInfo> roleInfos = roleMapper.selectList(null);
        return roleInfos;
    }

    @Override
    public List<UserRoleDto> getUserAllRole(UserInfo userInfo) {
        MPJLambdaWrapper<UserInfo> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAs(UserInfo::getId, UserRoleDto::getUserId)
                .selectAs(UserInfo::getUserName, UserRoleDto::getUserName)
                .selectAs(RoleInfo::getId, UserRoleDto::getRoleId)
                .selectAs(RoleInfo::getRoleName, UserRoleDto::getRoleName)
                .leftJoin(UserRoleLink.class, on -> on.eq(UserRoleLink::getUserId, UserInfo::getId))
                .leftJoin(RoleInfo.class, on -> on.eq(RoleInfo::getId, UserRoleLink::getRoleId))
                .eq(UserInfo::getId, userInfo.getId());
        List<UserRoleDto> result = userMapper.selectJoinList(UserRoleDto.class, wrapper);
        return result;
    }

    @Override
    public List<Permission> getAllPermission() {
        MPJLambdaWrapper<Permission> wrapper = new MPJLambdaWrapper<>(Permission.class);
        wrapper.selectAll();
        List<Permission> permissions = permissionMapper.selectJoinList(wrapper);
        return permissions;
    }
}
