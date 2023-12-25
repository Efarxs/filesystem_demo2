/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : 127.0.0.1:8889
 Source Schema         : hm_filesystem

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 08/12/2023 20:07:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `filename` varchar(50) DEFAULT NULL COMMENT '文件名',
  `size` bigint(20) DEFAULT NULL COMMENT '文件大小',
  `hash` char(50) DEFAULT NULL COMMENT '文件hash值',
  `author` int(11) DEFAULT NULL COMMENT '上传者用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='文件表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` char(50) DEFAULT NULL COMMENT '用户名',
  `password` char(50) DEFAULT NULL COMMENT '密码',
  `is_vip` tinyint(1) DEFAULT '0' COMMENT '是否VIP 0否 1是 默认0',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Table structure for sys_user_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_file`;
CREATE TABLE `sys_user_file` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `file_id` int(11) NOT NULL COMMENT '关联文件ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户文件关联表';

SET FOREIGN_KEY_CHECKS = 1;
