package com.test.security6.entity.db;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("role_info")
public class RoleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String roleName;

    private String description;

    private Long createTimestamp;
}
