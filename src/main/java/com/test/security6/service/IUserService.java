package com.test.security6.service;


import java.util.ArrayList;
import java.util.List;

import com.test.security6.entity.db.Permission;
import com.test.security6.entity.db.UserInfo;

public interface IUserService {
    List<UserInfo> queryByConditions(UserInfo userInfo);
    UserInfo addUserInfo(UserInfo userInfo);
    UserInfo updateUserInfo(UserInfo userInfo);
    UserInfo deleteUserInfo(UserInfo userInfo);
    UserInfo getUserByEmail(String email);
    List<Permission> getPermissionByEmail(String email);
}
