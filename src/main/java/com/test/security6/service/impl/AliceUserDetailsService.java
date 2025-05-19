package com.test.security6.service.impl;

import com.test.security6.entity.LoginUser;
import com.test.security6.entity.db.Permission;
import com.test.security6.entity.db.UserInfo;
import com.test.security6.service.IUserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AliceUserDetailsService implements UserDetailsService {

    AliceUserDetailsService(IUserService userService){
        this.userService = userService;
    }
    private final IUserService userService;
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        if(userEmail == null || userEmail.trim().isEmpty()){
            throw new UsernameNotFoundException("邮箱地址不能为空");
        }
        UserInfo user = userService.getUserByEmail(userEmail);
        if(user == null){
            throw new UsernameNotFoundException("邮箱未注册");
        }
        List<Permission> permissionByEmail = userService.getPermissionByEmail(userEmail);
        List<SimpleGrantedAuthority> authorities  =(permissionByEmail==null || permissionByEmail.isEmpty())?
                null: permissionByEmail.stream().map(permission -> new SimpleGrantedAuthority(permission.getPermissionCode()))
                .collect(Collectors.toList());
        return new LoginUser(user.getId(),user.getUserEmail(),user.getPassword(),user.getIsAccountNonExpired(),
                user.getIsAccountNonLocked(),user.getIsCredentialsNonExpired(),user.getIsEnable(),authorities);
    }
}
