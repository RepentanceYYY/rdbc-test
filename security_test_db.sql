/*
 Navicat Premium Data Transfer

 Source Server         : JVM-docker-alice
 Source Server Type    : MySQL
 Source Server Version : 80405
 Source Host           : 192.168.58.134:3308
 Source Schema         : security_test_db

 Target Server Type    : MySQL
 Target Server Version : 80405
 File Encoding         : 65001

 Date: 19/05/2025 15:37:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `request_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `permission_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `permission_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `is_delete` bit(1) NULL DEFAULT NULL,
  `create_timestamp` bigint NULL DEFAULT NULL,
  `delete_timestamp` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1f68cdf0-4f5e-4bd8-b487-71d06c575670', '/security/assignRoles', '给用户分配角色', 'security:assignRoles', b'0', 1747590465003, NULL);
INSERT INTO `permission` VALUES ('6a104568-2a15-47f9-b268-239130becc11', '/getUserAllRole/{userId}', '获取所有角色', 'security:getUserAllRole', b'0', 1747468963368, 0);
INSERT INTO `permission` VALUES ('6a104568-2a15-47f9-b268-239130becc12', '/security/addPermission', '添加权限', 'security:addPermission', b'0', 1747468963368, 0);
INSERT INTO `permission` VALUES ('6a104568-2a15-47f9-b268-239130becc13', '/security/getUserAllRole/{userId}', '查询用户所有权限', 'security:getUserAllRole', b'0', 1747468963368, 0);
INSERT INTO `permission` VALUES ('6a104568-2a15-47f9-b268-239130becc14', '/security/addRole', '添加角色', 'security:addRole', b'0', 1747468963368, 0);

-- ----------------------------
-- Table structure for role_info
-- ----------------------------
DROP TABLE IF EXISTS `role_info`;
CREATE TABLE `role_info`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `create_timestamp` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_info
-- ----------------------------
INSERT INTO `role_info` VALUES ('52ba8a74-0c8b-4265-8432-02c0a35316ff', '研发人员', '测试简介', 1747590348983);
INSERT INTO `role_info` VALUES ('58b3e6b5-edf0-4917-ab31-95aa5d3b6743', '财务人员', '没有简介', 1747639261223);
INSERT INTO `role_info` VALUES ('a0cba441-0f9c-467e-a4a9-8ef4b22e9997', '普通用户', '限制业务范围内操作', 1747468877682);

-- ----------------------------
-- Table structure for role_permission_link
-- ----------------------------
DROP TABLE IF EXISTS `role_permission_link`;
CREATE TABLE `role_permission_link`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `role_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `permission_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission_link
-- ----------------------------
INSERT INTO `role_permission_link` VALUES ('4133aa71-d631-45b2-9fc0-e09a2c1b03f2', 'a0cba441-0f9c-467e-a4a9-8ef4b22e9997', '6a104568-2a15-47f9-b268-239130becc11');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '用户id',
  `user_email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '用户邮箱',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL COMMENT '用户名',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL COMMENT '用户真实名称',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL COMMENT '用户密码',
  `create_timestamp` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `isAccountNonExpired` tinyint NULL DEFAULT NULL COMMENT '账号是否未过期',
  `is_account_non_locked` tinyint NULL DEFAULT NULL COMMENT '账号是否未被锁定',
  `is_credentials_nonexpired` tinyint NULL DEFAULT NULL COMMENT '凭证（密码）是否未过期',
  `is_enable` tinyint NULL DEFAULT NULL COMMENT '	账号是否启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('7d6ba277-b79b-492a-961f-03cc192351dd', '2380404256@qq.com', NULL, NULL, '123456789', 1747468801, 1, 1, NULL, 1);
INSERT INTO `user_info` VALUES ('85fcadcc-bb46-4372-85fa-179b8c221e1b', '1031421863@qq.com', NULL, NULL, '123456789', 1747468272, 1, 1, 1, 1);

-- ----------------------------
-- Table structure for user_role_link
-- ----------------------------
DROP TABLE IF EXISTS `user_role_link`;
CREATE TABLE `user_role_link`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL,
  `role_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role_link
-- ----------------------------
INSERT INTO `user_role_link` VALUES ('5b08f064-cb6d-4e36-9e98-68917442ef33', '85fcadcc-bb46-4372-85fa-179b8c221e1b', 'a0cba441-0f9c-467e-a4a9-8ef4b22e9997');

SET FOREIGN_KEY_CHECKS = 1;
