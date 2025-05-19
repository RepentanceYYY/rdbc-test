package com.test.security6.entity.db;


import lombok.Data;

import java.io.Serializable;

@Data
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String requestUrl;

    private String permissionName;

    private String permissionCode;

    private Boolean isDelete;

    private Long createTimestamp;

    private Long deleteTimestamp;
}
