package com.test.security6.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.test.security6.entity.db.Permission;
import com.test.security6.entity.db.RolePermissionLink;
import com.test.security6.entity.db.UserInfo;
import com.test.security6.entity.db.UserRoleLink;
import com.test.security6.mapper.UserMapper;
import com.test.security6.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    private final UserMapper userMapper;
    UserServiceImpl(UserMapper userMapper){
        this.userMapper = userMapper;
    }
    @Override
    public List<UserInfo> queryByConditions(UserInfo userInfo) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userInfo.getId() !=null,UserInfo::getId,userInfo.getId());
        wrapper.eq(userInfo.getUserName() !=null,UserInfo::getUserName,userInfo.getUserName());
        wrapper.eq(userInfo.getUserEmail() !=null,UserInfo::getUserEmail,userInfo.getUserEmail());
        wrapper.eq(userInfo.getRealName() !=null,UserInfo::getRealName,userInfo.getRealName());
        wrapper.eq(userInfo.getPassword() !=null,UserInfo::getPassword,userInfo.getPassword());
        wrapper.eq(userInfo.getCreateTimestamp() !=null,UserInfo::getCreateTimestamp,userInfo.getCreateTimestamp());
        List<UserInfo> userInfos = userMapper.selectList(wrapper);
        return userInfos.isEmpty()?new ArrayList<>():userInfos;
    }

    @Override
    public UserInfo addUserInfo(UserInfo userInfo) {
        userInfo.setId(UUID.randomUUID().toString());
        userInfo.setCreateTimestamp(System.currentTimeMillis()/1000);
        userInfo.setIsEnable(true);
        userInfo.setIsAccountNonExpired(true);
        userInfo.setIsAccountNonLocked(true);
        int result = userMapper.insert(userInfo);
        return result>0?userInfo:null;
    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) {
        return null;
    }

    @Override
    public UserInfo deleteUserInfo(UserInfo userInfo) {
        return null;
    }

    @Override
    public UserInfo getUserByEmail(String email) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserEmail(email);
        List<UserInfo> userInfos = queryByConditions(userInfo);
        return userInfos.isEmpty()?null:userInfos.get(0);
    }

    @Override
    public List<Permission> getPermissionByEmail(String email) {
        MPJLambdaWrapper<UserInfo> wrapper = new MPJLambdaWrapper<>();
         wrapper.leftJoin(UserRoleLink.class,UserRoleLink::getUserId,UserInfo::getId)
                 .leftJoin(RolePermissionLink.class,RolePermissionLink::getRoleId,UserRoleLink::getRoleId)
                 .leftJoin(Permission.class,Permission::getId,RolePermissionLink::getPermissionId)
                .eq(UserInfo::getUserEmail,email).selectAll(Permission.class);
        List<Permission> permissions = userMapper.selectJoinList(Permission.class,wrapper);
        return permissions;
    }
}
