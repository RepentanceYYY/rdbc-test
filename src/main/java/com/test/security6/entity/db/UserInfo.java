package com.test.security6.entity.db;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("user_info")
@EqualsAndHashCode(callSuper = false)
public class UserInfo {
    private static final long serialVersionUID = 1L;

    private String id;

    @TableField("user_email")
    private String userEmail;

    @TableField("user_name")
    private String userName;

    @TableField("real_name")
    private String realName;

    private String password;

    @TableField("create_timestamp")
    private Long createTimestamp;

    @TableField("isAccountNonExpired")
    private Boolean isAccountNonExpired;

    @TableField("is_account_non_locked")
    private Boolean isAccountNonLocked;

    @TableField("is_credentials_nonexpired")
    private Boolean isCredentialsNonExpired;

    @TableField("is_enable")
    private Boolean isEnable;
}
