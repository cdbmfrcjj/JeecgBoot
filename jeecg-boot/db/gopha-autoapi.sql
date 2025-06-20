CREATE database if NOT EXISTS `jeecg-boot` default character set utf8mb4 collate utf8mb4_unicode_ci;
USE `jeecg-boot`;
/*
 Navicat Premium Data Transfer

 Source Server         : 221.207.236.210
 Source Server Type    : MySQL
 Source Server Version : 80019 (8.0.19)
 Source Host           : 221.207.236.210:3399
 Source Schema         : jeecg-boot-380

 Target Server Type    : MySQL
 Target Server Version : 80019 (8.0.19)
 File Encoding         : 65001

 Date: 20/06/2025 10:36:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_autoapi_handle
-- ----------------------------
DROP TABLE IF EXISTS `sys_autoapi_handle`;
CREATE TABLE `sys_autoapi_handle`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `code_text` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '代码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_autoapi_handle
-- ----------------------------
INSERT INTO `sys_autoapi_handle` VALUES ('1924633299238244353', 'admin', '2025-05-20 09:08:31', 'admin', '2025-05-20 09:09:22', 'A01', 'AES加密', '对响应数据进行AES加密', 'package org.jeecg\n\n\nimport org.jeecg.modules.autoapi.service.AfterExecuteHandleService\n\nimport javax.crypto.Cipher\nimport javax.crypto.KeyGenerator\nimport javax.crypto.SecretKey\nimport javax.crypto.spec.SecretKeySpec\nimport javax.servlet.http.HttpServletRequest\nimport javax.servlet.http.HttpServletResponse\nimport java.security.SecureRandom\n\nclass CommonHandleScript implements  AfterExecuteHandleService{\n\n\n	@Override\n	public Object doPress(HttpServletRequest request, HttpServletResponse response,Object result) {\n		try {\n			KeyGenerator kgen = KeyGenerator.getInstance(\"AES\");\n			kgen.init(128, new SecureRandom(\"gopha-secret-key\".getBytes()));\n			SecretKey secretKey = kgen.generateKey();\n			byte[] enCodeFormat = secretKey.getEncoded();\n			SecretKeySpec key = new SecretKeySpec(enCodeFormat, \"AES\");\n			Cipher cipher = Cipher.getInstance(\"AES\");// 创建密码器\n			byte[] byteContent = ((String)result).getBytes(\"utf-8\");\n			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化\n			byte[] afterresult = cipher.doFinal(byteContent);\n			return Base64.getEncoder().encodeToString(afterresult);\n		}catch(Exception e){\n			e.printStackTrace()\n			return \"\";\n		}\n	}\n}');
INSERT INTO `sys_autoapi_handle` VALUES ('1924633370348474369', 'admin', '2025-05-20 09:08:48', NULL, NULL, 'A01', 'base64编码', '对响应数据进行base64编码', 'package org.jeecg\n\n\nimport org.jeecg.modules.autoapi.service.AfterExecuteHandleService\n\nimport javax.servlet.http.HttpServletRequest\nimport javax.servlet.http.HttpServletResponse\n\nclass CommonHandleScript implements  AfterExecuteHandleService{\n\n\n	@Override\n	public Object doPress(HttpServletRequest request, HttpServletResponse response,Object result) {\n		try {\n			return Base64.encoder.encodeToString(((String)result).getBytes());\n		}catch(Exception e){\n			e.printStackTrace()\n			return \"\";\n		}\n	}\n\n\n}');
INSERT INTO `sys_autoapi_handle` VALUES ('1924633452166762497', 'admin', '2025-05-20 09:09:08', NULL, NULL, 'A01', 'JSON转XML', 'JSON转XML', 'package org.jeecg\n\n\nimport groovy.json.JsonOutput\nimport org.jeecg.modules.autoapi.service.AfterExecuteHandleService\nimport org.json.JSONObject\nimport org.json.XML\n\nimport javax.servlet.http.HttpServletRequest\nimport javax.servlet.http.HttpServletResponse\n\nclass CommonHandleScript implements  AfterExecuteHandleService{\n\n\n	@Override\n	public Object doPress(HttpServletRequest request, HttpServletResponse response,Object result) {\n		try {\n			StringBuffer buffer = new StringBuffer();\n			buffer.append(\"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?>\");\n			Map map=new HashMap();\n			map.put(\"item\",result);\n			def jsonOutput = new JsonOutput();\n			def resultJson = jsonOutput.toJson(map);\n			JSONObject jsonObj = new JSONObject(resultJson);\n			String xml=\"<xml>\" + XML.toString(jsonObj) + \"</xml>\";\n      buffer.append(xml);\n   		return buffer.toString();\n		}catch(Exception e){\n			return \"\";\n		}\n	}\n\n\n}');

-- ----------------------------
-- Table structure for sys_autoapi_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_autoapi_log`;
CREATE TABLE `sys_autoapi_log`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口名称',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调用地址',
  `params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '调用参数',
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调用者IP',
  `data` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据内容',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_autoapi_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_autoapi_manage
-- ----------------------------
DROP TABLE IF EXISTS `sys_autoapi_manage`;
CREATE TABLE `sys_autoapi_manage`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `params_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数描述',
  `do_params` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行参数',
  `permission` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份验证',
  `handle_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通用处理',
  `code_text` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '代码',
  `save_log` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行日志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_autoapi_manage
-- ----------------------------
INSERT INTO `sys_autoapi_manage` VALUES ('1922889069536931842', 'admin', '2025-05-15 13:37:34', 'admin', '2025-05-20 11:13:31', 'A01', 'test1', '测试接口', '', '', 'disable', 
'1924633452166762497', 'package org.jeecg.pc\n\nimport com.alibaba.fastjson.JSONArray\nimport org.apache.shiro.SecurityUtils\nimport org.jeecg.common.api.vo.Result\nimport org.jeecg.common.system.vo.LoginUser\nimport org.jeecg.modules.autoapi.service.ExecuteHandleService\nimport org.jeecg.modules.base.service.SqlService\nimport org.springframework.beans.factory.annotation.Autowired\n\nimport javax.servlet.http.HttpServletRequest\nimport javax.servlet.http.HttpServletResponse\nimport java.text.SimpleDateFormat\n\nclass HandleScript_updatePeriodStatus implements  ExecuteHandleService{\n\n    @Autowired\n    SqlService sqlService;\n\n    @Override\n    public Object doPress(HttpServletRequest request, HttpServletResponse response,Map<String,Object> paramsMap) {\n        String sql = \"select *from sys_user\"\n        List list=this.sqlService.selectList(sql);\n        System.out.println(\"2222222\");\n        return Result.OK(0);\n    }\n\n}', 'disable');
INSERT INTO `sys_autoapi_manage` VALUES ('1924636165625597954', 'admin', '2025-05-20 09:19:55', 'admin', '2025-05-20 10:19:18', 'A01', '1', '1', '1', '', 'disable', '1924633299238244353', '温热', 'enable');
INSERT INTO `sys_autoapi_manage` VALUES ('1924651056415506434', 'admin', '2025-05-20 10:19:05', 'admin', '2025-05-20 11:40:58', 'A01', '3', '3', '3', '3', 'enable', NULL, '11', 'enable');

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922868008896372737', '1455100420297859074', '动态接口开发管理', '/gophacore/autoapi/sysAutoapiManageList', 'gophacore/autoapi/SysAutoapiManageList', 1, '', NULL, 1, NULL, '0', 2.10, 0, NULL, 0, 0, 0, 0, NULL, 'admin', '2025-05-15 12:13:53', NULL, NULL, 0, 0, NULL, 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922868232389861378', '1455100420297859074', '动态接口通用处理', '/gophacore/autoapi/sysAutoapiHandleList', 'gophacore/autoapi/SysAutoapiHandleList', 1, '', NULL, 1, NULL, '0', 2.20, 0, NULL, 0, 0, 0, 0, NULL, 'admin', '2025-05-15 12:14:46', 'admin', '2025-05-15 12:15:49', 0, 0, NULL, 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922868690445606914', '1455100420297859074', '动态接口调用日志', '/gophacore/autoapi/sysAutoapiLogList', 'gophacore/autoapi/SysAutoapiLogList', 1, '', NULL, 1, NULL, '0', 2.30, 0, NULL, 0, 0, 0, 0, NULL, 'admin', '2025-05-15 12:16:36', NULL, NULL, 0, 0, NULL, 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922887123178549250', '1922868008896372737', '添加动态接口开发管理', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_manage:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:29:50', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922887245929050113', '1922868008896372737', '编辑动态接口开发管理', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_manage:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:30:20', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922887317534208002', '1922868008896372737', '删除动态接口开发管理', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_manage:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:30:37', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922887398517829634', '1922868008896372737', '批量删除动态接口开发管理', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_manage:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:30:56', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922887476523495425', '1922868008896372737', '导出excel_动态接口开发管理', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_manage:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:31:15', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922887545922449409', '1922868008896372737', '导入excel_动态接口开发管理', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_manage:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:31:31', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922887622384611329', '1922868232389861378', '添加通用处理', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_handle:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:31:49', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922887700209922049', '1922868232389861378', '编辑通用处理', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_handle:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:32:08', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922887786981683201', '1922868232389861378', '删除通用处理', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_handle:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:32:29', 'admin', '2025-05-15 13:33:06', 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922888081128222721', '1922868232389861378', '批量删除通用处理', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_handle:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:33:39', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922888143786930177', '1922868232389861378', '导出excel_通用处理', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_handle:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:33:54', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922888228721586177', '1922868232389861378', '导入excel_通用处理', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_handle:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:34:14', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922888353808314369', '1922868690445606914', '添加数据交互日志', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_log:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:34:44', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922888427254771714', '1922868690445606914', '编辑数据交互日志', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_log:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:35:01', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922888494078423041', '1922868690445606914', '删除数据交互日志', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_log:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:35:17', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922888570486059010', '1922868690445606914', '批量删除数据交互日志', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_log:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:35:35', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922888693156868097', '1922868690445606914', '导出excel_数据交互日志', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_log:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:36:05', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1922888779647610881', '1922868690445606914', '导入excel_数据交互日志', NULL, NULL, 0, NULL, NULL, 2, 'autoapi:sys_autoapi_log:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-05-15 13:36:25', NULL, NULL, 0, 0, '1', 0);


INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1922868796674744322', 'f6817f48af4fb3af11b9e8bf182f618b', '1890213291321749505', NULL, '2025-05-15 12:17:01', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1922868796674744323', 'f6817f48af4fb3af11b9e8bf182f618b', '1922868008896372737', NULL, '2025-05-15 12:17:01', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1922868796674744324', 'f6817f48af4fb3af11b9e8bf182f618b', '1922868232389861378', NULL, '2025-05-15 12:17:01', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1922868796674744325', 'f6817f48af4fb3af11b9e8bf182f618b', '1922868690445606914', NULL, '2025-05-15 12:17:01', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1922888875709755393', 'f6817f48af4fb3af11b9e8bf182f618b', '1922887123178549250', NULL, '2025-05-15 13:36:48', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1922888875709755394', 'f6817f48af4fb3af11b9e8bf182f618b', '1922887245929050113', NULL, '2025-05-15 13:36:48', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1922888875709755395', 'f6817f48af4fb3af11b9e8bf182f618b', '1922887317534208002', NULL, '2025-05-15 13:36:48', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1922888875709755396', 'f6817f48af4fb3af11b9e8bf182f618b', '1922887398517829634', NULL, '2025-05-15 13:36:48', '0:0:0:0:0:0:0:1');
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('1922888875709755397', 'f6817f48af4fb3af11b9e8bf182f618b', '1922887476523495425', NULL, '2025-05-15 13:36:48', '0:0:0:0:0:0:0:1');
