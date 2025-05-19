package com.test.security6.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * token属性
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimsContent {
    private String userId;
    private String userEmail;
    private Long timestamp;
    private Boolean isAccountNonExpired;
}
