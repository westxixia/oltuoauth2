SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_sys_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_resource`;
CREATE TABLE `t_sys_resource` (
  `ID` varchar(36) NOT NULL COMMENT '资源节点主键|||UUID',
  `PARENT_RESOURCE_ID` varchar(36) NOT NULL DEFAULT '0' COMMENT '父资源节点|||UUID',
  `RESOURCE_URL` varchar(200) NOT NULL COMMENT '资源地址',
  `RESOURCE_NAME` varchar(60) NOT NULL COMMENT '资源名称',
  `RESOURCE_TYPE` varchar(2) NOT NULL COMMENT '资源类型',
  `RESOURCE_PERMISSION` varchar(200) DEFAULT NULL COMMENT '资源权限',
  `RESOURCE_INTRO` varchar(12000) DEFAULT NULL COMMENT '资源简介',
  `NAVIGATE_URL` varchar(200) DEFAULT NULL COMMENT '导航URL',
  `BUSINESS_URL` varchar(200) DEFAULT NULL COMMENT '业务URL',
  `OTHER_URL` varchar(200) DEFAULT NULL COMMENT '其他URL',
  `USE_FUNCTION` varchar(200) DEFAULT NULL COMMENT '调用函数',
  `RESOURCE_IMG` varchar(36) DEFAULT NULL COMMENT '资源图标|||文件信息主键',
  `RESOURCE_KEY` varchar(60) DEFAULT NULL COMMENT '资源值',
  `SHOW_ORDER` int(4) unsigned DEFAULT NULL COMMENT '显示顺序',
  `ISMENU` varchar(1) DEFAULT NULL COMMENT '菜单区分',
  `SYSTEM_RESERVE1` varchar(300) DEFAULT NULL COMMENT '系统预留1',
  `SYSTEM_RESERVE2` varchar(300) DEFAULT NULL COMMENT '系统预留2',
  `SYSTEM_RESERVE3` varchar(300) DEFAULT NULL COMMENT '系统预留3',
  `SYSTEM_RESERVE4` decimal(10,4) DEFAULT NULL COMMENT '系统预留4',
  `SYSTEM_RESERVE5` date DEFAULT NULL COMMENT '系统预留5',
  `DEL_FLG` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除区分|||0:有效 1：无效(逻辑删除)',
  `SYS_REG_TMSP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `SYS_REG_USR_CD` varchar(32) NOT NULL COMMENT '登录者',
  `SYS_UPD_TMSP` timestamp NOT NULL DEFAULT '2001-01-01 00:00:00' COMMENT '更新时间',
  `SYS_UPD_USR_CD` varchar(32) NOT NULL COMMENT '更新者',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源信息';

-- ----------------------------
--  Table structure for `t_sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role` (
  `ID` varchar(36) NOT NULL COMMENT '角色主键',
  `ROLE_NAME` varchar(60) NOT NULL COMMENT '角色名称',
  `ROLE_STATUS` varchar(3) NOT NULL COMMENT '角色状态|||共通CODE(10002)',
  `ROLE_INTRO` varchar(12000) DEFAULT NULL COMMENT '角色简介',
  `SYSTEM_RESERVE1` varchar(300) DEFAULT NULL COMMENT '系统预留1',
  `SYSTEM_RESERVE2` varchar(300) DEFAULT NULL COMMENT '系统预留2',
  `SYSTEM_RESERVE3` varchar(300) DEFAULT NULL COMMENT '系统预留3',
  `SYSTEM_RESERVE4` decimal(10,4) DEFAULT NULL COMMENT '系统预留4',
  `SYSTEM_RESERVE5` date DEFAULT NULL COMMENT '系统预留5',
  `DEL_FLG` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除区分|||0:有效 1：无效(逻辑删除)',
  `SYS_REG_TMSP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `SYS_REG_USR_CD` varchar(32) NOT NULL COMMENT '登录者',
  `SYS_UPD_TMSP` timestamp NOT NULL DEFAULT '2001-01-01 00:00:00' COMMENT '更新时间',
  `SYS_UPD_USR_CD` varchar(32) NOT NULL COMMENT '更新者',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息';

-- ----------------------------
--  Table structure for `t_sys_role_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_resource`;
CREATE TABLE `t_sys_role_resource` (
  `ID` varchar(36) NOT NULL COMMENT '角色资源关联主键|||UUID',
  `ROLE_ID` varchar(36) NOT NULL COMMENT '角色主键',
  `RESOURCE_ID` varchar(36) NOT NULL COMMENT '资源节点主键|||UUID',
  `SYSTEM_RESERVE1` varchar(300) DEFAULT NULL COMMENT '系统预留1',
  `SYSTEM_RESERVE2` varchar(300) DEFAULT NULL COMMENT '系统预留2',
  `SYSTEM_RESERVE3` varchar(300) DEFAULT NULL COMMENT '系统预留3',
  `SYSTEM_RESERVE4` decimal(10,4) DEFAULT NULL COMMENT '系统预留4',
  `SYSTEM_RESERVE5` date DEFAULT NULL COMMENT '系统预留5',
  `DEL_FLG` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除区分|||0:有效 1：无效(逻辑删除)',
  `SYS_REG_TMSP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `SYS_REG_USR_CD` varchar(32) NOT NULL COMMENT '登录者',
  `SYS_UPD_TMSP` timestamp NOT NULL DEFAULT '2001-01-01 00:00:00' COMMENT '更新时间',
  `SYS_UPD_USR_CD` varchar(32) NOT NULL COMMENT '更新者',
  PRIMARY KEY (`ID`),
  KEY `FK_T_SYS_ROLE_T_SYS_ROLE_RESOURCE` (`ROLE_ID`),
  KEY `FK_T_SYS_RESOURCE_T_SYS_ROLE_RESOURCE` (`RESOURCE_ID`),
  CONSTRAINT `FK_T_SYS_RESOURCE_T_SYS_ROLE_RESOURCE` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `t_sys_resource` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_T_SYS_ROLE_T_SYS_ROLE_RESOURCE` FOREIGN KEY (`ROLE_ID`) REFERENCES `t_sys_role` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色资源关联';

-- ----------------------------
--  Table structure for `t_sys_org`
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_org`;
CREATE TABLE `t_sys_org` (
  `ID` varchar(36) NOT NULL COMMENT '组织信息主键|||UUID',
  `PARENT_ORG_ID` varchar(36) NOT NULL DEFAULT '0' COMMENT '父组织信息主键|||UUID',
  `ORG_NAME` varchar(90) NOT NULL COMMENT '组织名称',
  `BIZ_REG_NUMBER` varchar(60) DEFAULT NULL COMMENT '工商注册号',
  `LEGAL_REPRESENTATIVE` varchar(30) DEFAULT NULL COMMENT '法定代表人',
  `ORG_NATURE` varchar(3) DEFAULT NULL COMMENT '组织性质|||共通CODE(10005)',
  `COUNTRY_ID` varchar(36) DEFAULT NULL COMMENT '国家主键',
  `PROVINCE_ID` varchar(36) DEFAULT NULL COMMENT '省份主键',
  `CITY_ID` varchar(36) DEFAULT NULL COMMENT '城市主键',
  `AREA_ID` varchar(36) DEFAULT NULL COMMENT '区域主键',
  `ORG_ADDRESS` varchar(300) DEFAULT NULL COMMENT '组织地址',
  `ORG_LATITUDE` decimal(9,6) unsigned DEFAULT NULL COMMENT '组织纬度',
  `ORG_LONGITUDE` decimal(9,6) unsigned DEFAULT NULL COMMENT '组织经度',
  `ORG_ADCODE` varchar(36) DEFAULT NULL COMMENT '组织城市代码',
  `SYSTEM_RESERVE1` varchar(300) DEFAULT NULL COMMENT '系统预留1',
  `SYSTEM_RESERVE2` varchar(300) DEFAULT NULL COMMENT '系统预留2',
  `SYSTEM_RESERVE3` varchar(300) DEFAULT NULL COMMENT '系统预留3',
  `SYSTEM_RESERVE4` decimal(10,4) DEFAULT NULL COMMENT '系统预留4',
  `SYSTEM_RESERVE5` date DEFAULT NULL COMMENT '系统预留5',
  `DEL_FLG` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除区分|||0:有效 1：无效(逻辑删除)',
  `SYS_REG_TMSP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `SYS_REG_USR_CD` varchar(32) NOT NULL COMMENT '登录者',
  `SYS_UPD_TMSP` timestamp NOT NULL DEFAULT '2001-01-01 00:00:00' COMMENT '更新时间',
  `SYS_UPD_USR_CD` varchar(32) NOT NULL COMMENT '更新者',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织信息';

-- ----------------------------
--  Table structure for `t_sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user` (
  `ID` varchar(36) NOT NULL COMMENT '用户主键|||UUID',
  `USER_NAME` varchar(30) NOT NULL COMMENT '登录名',
  `USER_PSW` varchar(32) NOT NULL COMMENT '密码',
  `FULL_NAME` varchar(30) NOT NULL COMMENT '姓名',
  `MOBILE_PHONE` varchar(30) DEFAULT NULL COMMENT '手机号码',
  `EMAIL` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `STATUS_TYPE` varchar(3) NOT NULL COMMENT '是否锁定|||共通CODE(10002)',
  `SYSTEM_RESERVE1` varchar(300) DEFAULT NULL COMMENT '系统预留1',
  `SYSTEM_RESERVE2` varchar(300) DEFAULT NULL COMMENT '系统预留2',
  `SYSTEM_RESERVE3` varchar(300) DEFAULT NULL COMMENT '系统预留3',
  `SYSTEM_RESERVE4` decimal(10,4) DEFAULT NULL COMMENT '系统预留4',
  `SYSTEM_RESERVE5` date DEFAULT NULL COMMENT '系统预留5',
  `DEL_FLG` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除区分|||0:有效 1：无效(逻辑删除)',
  `SYS_REG_TMSP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `SYS_REG_USR_CD` varchar(32) NOT NULL COMMENT '登录者',
  `SYS_UPD_TMSP` timestamp NOT NULL DEFAULT '2001-01-01 00:00:00' COMMENT '更新时间',
  `SYS_UPD_USR_CD` varchar(32) NOT NULL COMMENT '更新者',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息';

-- ----------------------------
--  Table structure for `t_sys_org_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_org_user`;
CREATE TABLE `t_sys_org_user` (
  `ID` varchar(36) NOT NULL COMMENT '组织用户主键|||UUID',
  `ORG_ID` varchar(36) NOT NULL COMMENT '组织信息主键|||UUID',
  `USER_ID` varchar(36) NOT NULL COMMENT '用户主键|||UUID',
  `SYSTEM_RESERVE1` varchar(300) DEFAULT NULL COMMENT '系统预留1',
  `SYSTEM_RESERVE2` varchar(300) DEFAULT NULL COMMENT '系统预留2',
  `SYSTEM_RESERVE3` varchar(300) DEFAULT NULL COMMENT '系统预留3',
  `SYSTEM_RESERVE4` decimal(10,4) DEFAULT NULL COMMENT '系统预留4',
  `SYSTEM_RESERVE5` date DEFAULT NULL COMMENT '系统预留5',
  `DEL_FLG` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除区分|||0:有效 1：无效(逻辑删除)',
  `SYS_REG_TMSP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `SYS_REG_USR_CD` varchar(32) NOT NULL COMMENT '登录者',
  `SYS_UPD_TMSP` timestamp NOT NULL DEFAULT '2001-01-01 00:00:00' COMMENT '更新时间',
  `SYS_UPD_USR_CD` varchar(32) NOT NULL COMMENT '更新者',
  PRIMARY KEY (`ID`),
  KEY `FK_T_SYS_USER_T_SYS_ORG_USER` (`USER_ID`),
  KEY `FK_T_SYS_ORG_T_SYS_ORG_USER` (`ORG_ID`),
  CONSTRAINT `FK_T_SYS_ORG_T_SYS_ORG_USER` FOREIGN KEY (`ORG_ID`) REFERENCES `t_sys_org` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_T_SYS_USER_T_SYS_ORG_USER` FOREIGN KEY (`USER_ID`) REFERENCES `t_sys_user` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织用户信息';

-- ----------------------------
--  Table structure for `t_sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_role`;
CREATE TABLE `t_sys_user_role` (
  `ID` varchar(36) NOT NULL COMMENT '用户角色关联主键|||UUID',
  `USER_ID` varchar(36) NOT NULL COMMENT '用户主键|||UUID',
  `ROLE_ID` varchar(36) NOT NULL COMMENT '角色主键',
  `SYSTEM_RESERVE1` varchar(300) DEFAULT NULL COMMENT '系统预留1',
  `SYSTEM_RESERVE2` varchar(300) DEFAULT NULL COMMENT '系统预留2',
  `SYSTEM_RESERVE3` varchar(300) DEFAULT NULL COMMENT '系统预留3',
  `SYSTEM_RESERVE4` decimal(10,4) DEFAULT NULL COMMENT '系统预留4',
  `SYSTEM_RESERVE5` date DEFAULT NULL COMMENT '系统预留5',
  `DEL_FLG` varchar(1) NOT NULL DEFAULT '0' COMMENT '删除区分|||0:有效 1：无效(逻辑删除)',
  `SYS_REG_TMSP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `SYS_REG_USR_CD` varchar(32) NOT NULL COMMENT '登录者',
  `SYS_UPD_TMSP` timestamp NOT NULL DEFAULT '2001-01-01 00:00:00' COMMENT '更新时间',
  `SYS_UPD_USR_CD` varchar(32) NOT NULL COMMENT '更新者',
  PRIMARY KEY (`ID`),
  KEY `FK_T_SYS_USER_T_SYS_USER_ROLE` (`USER_ID`),
  KEY `FK_T_SYS_ROLE_T_SYS_USER_ROLE` (`ROLE_ID`),
  CONSTRAINT `FK_T_SYS_ROLE_T_SYS_USER_R` FOREIGN KEY (`ROLE_ID`) REFERENCES `t_sys_role` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_T_SYS_USER_T_SYS_USER_R` FOREIGN KEY (`USER_ID`) REFERENCES `t_sys_user` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联';

SET FOREIGN_KEY_CHECKS = 1;