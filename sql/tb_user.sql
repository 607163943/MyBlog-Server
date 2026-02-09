/*
 Navicat Premium Data Transfer

 Source Server         : centos7-mysql
 Source Server Type    : MySQL
 Source Server Version : 90500
 Source Host           : 192.168.150.101:3306
 Source Schema         : my_blog_db

 Target Server Type    : MySQL
 Target Server Version : 90500
 File Encoding         : 65001

 Date: 20/01/2026 19:36:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密后的密码（hash）',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '显示昵称',
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0正常；1禁用',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_user_email`(`email` ASC) USING BTREE,
  INDEX `idx_user_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表（管理员账户）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '607163943', NULL, NULL, 0, '2026-01-20 18:46:38', '2026-01-01 07:14:43', '2026-01-16 13:26:05');

SET FOREIGN_KEY_CHECKS = 1;
