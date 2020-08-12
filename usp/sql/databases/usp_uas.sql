-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 10.34.12.185    Database: usp_uas
-- ------------------------------------------------------
-- Server version	5.5.5-10.2.25-MariaDB-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_right`
--

DROP TABLE IF EXISTS `sys_right`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_right` (
  `right_id` bigint(20) NOT NULL COMMENT '权限编号',
  `right_name` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '权限名称',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父权限编号',
  `app_id` int(11) NOT NULL DEFAULT 0 COMMENT '应用编号',
  `sys_type` smallint(6) NOT NULL DEFAULT 0 COMMENT '系统权限 1=是',
  `service_demander` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '服务委托方 Y=是 N=否',
  `service_provider` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '服务提供商 Y=是 N=否',
  `device_user` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备使用商 Y=是 N=否',
  `cloud_manager` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '平台管理 Y=是 N=否',
  PRIMARY KEY (`right_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_right_url`
--

DROP TABLE IF EXISTS `sys_right_url`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_right_url` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `right_type` int(11) NOT NULL DEFAULT 0 COMMENT '权限类型 1=公共权限',
  `uri` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '权限对应的请求URI',
  `path_method` varchar(10) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '请求方法',
  `right_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '权限ID',
  `description` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='权限映射表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL COMMENT '主键',
  `corp_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '企业编号',
  `role_name` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '角色名称',
  `sys_type` smallint(6) NOT NULL DEFAULT 0 COMMENT '系统类型，1=系统角色',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '是否可用',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operator_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '操作时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_role_app`
--

DROP TABLE IF EXISTS `sys_role_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_app` (
  `role_id` bigint(20) NOT NULL COMMENT '角色编号',
  `app_id` int(11) NOT NULL COMMENT '应用编号',
  PRIMARY KEY (`role_id`,`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色应用表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_role_right`
--

DROP TABLE IF EXISTS `sys_role_right`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_right` (
  `role_id` bigint(20) NOT NULL COMMENT '角色编号',
  `right_id` bigint(20) NOT NULL COMMENT '权限编号',
  PRIMARY KEY (`role_id`,`right_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_role_user`
--

DROP TABLE IF EXISTS `sys_role_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_user` (
  `role_id` bigint(20) NOT NULL COMMENT '角色编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户编号',
  PRIMARY KEY (`role_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色人员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_tenant`
--

DROP TABLE IF EXISTS `sys_tenant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tenant` (
  `corp_id` bigint(20) NOT NULL COMMENT '企业ID',
  `service_demander` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '服务委托方 Y=是 N=否',
  `service_provider` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '服务提供商 Y=是 N=否',
  `device_user` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备使用商 Y=是 N=否',
  `cloud_manager` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '平台管理 Y=是 N=否',
  `operator` bigint(20) DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`corp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='租户设置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_cfg_area`
--

DROP TABLE IF EXISTS `uas_cfg_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_cfg_area` (
  `code` varchar(6) COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(20) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='行政区划表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_cfg_industry`
--

DROP TABLE IF EXISTS `uas_cfg_industry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_cfg_industry` (
  `code` varchar(1) COLLATE utf8mb4_bin NOT NULL COMMENT '门类代码',
  `name` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '类别名称',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='国家行业分类标准表(门类)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_corp_admin`
--

DROP TABLE IF EXISTS `uas_corp_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_corp_admin` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `corpid` bigint(20) NOT NULL COMMENT '企业ID',
  `granttime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '授权时间',
  PRIMARY KEY (`userid`,`corpid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='企业管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_corp_apply`
--

DROP TABLE IF EXISTS `uas_corp_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_corp_apply` (
  `corpid` bigint(20) NOT NULL COMMENT '企业ID',
  `txid` bigint(20) NOT NULL COMMENT '交易号',
  `applyuserid` bigint(20) NOT NULL DEFAULT 0 COMMENT '申请人用户ID',
  `username` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '姓名',
  `status` smallint(6) NOT NULL COMMENT '状态 1=申请 2=通过 3=拒绝',
  `applynote` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '申请描述',
  `applytime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '申请时间',
  `inviteuserid` bigint(20) NOT NULL DEFAULT 0 COMMENT '邀请人用户ID',
  `checkuserid` bigint(20) NOT NULL DEFAULT 0 COMMENT '审核用户ID',
  `checktime` datetime DEFAULT NULL COMMENT '审核时间',
  `checknote` varchar(18) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '审核意见',
  PRIMARY KEY (`corpid`,`txid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='加入企业申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_corp_oper`
--

DROP TABLE IF EXISTS `uas_corp_oper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_corp_oper` (
  `txid` bigint(20) NOT NULL COMMENT '交易号',
  `corpid` bigint(20) NOT NULL COMMENT '企业ID',
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `appid` int(11) NOT NULL COMMENT '应用ID',
  `opertype` smallint(6) NOT NULL DEFAULT 0 COMMENT '操作类型',
  `details` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '操作详情',
  `opertime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '操作时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  PRIMARY KEY (`txid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='企业变更跟踪表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_corp_reg`
--

DROP TABLE IF EXISTS `uas_corp_reg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_corp_reg` (
  `corpid` bigint(20) NOT NULL COMMENT '企业ID',
  `txid` bigint(20) NOT NULL DEFAULT 0 COMMENT '交易号',
  `corpname` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '企业名称',
  `shortname` varchar(8) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '企业简称',
  `province` varchar(2) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '省份代码',
  `city` varchar(4) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '地市代码',
  `district` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '区县代码',
  `address` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '详细地址',
  `telephone` varchar(30) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '联系电话',
  `industry` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '所属行业',
  `business` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '主营业务',
  `staffqty` int(11) NOT NULL DEFAULT 0 COMMENT '员工数量',
  `website` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方网址',
  `passwd` varchar(60) COLLATE utf8mb4_bin NOT NULL DEFAULT '''' COMMENT '管理密码',
  `logoimg` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Logo文件ID',
  `reguserid` bigint(20) NOT NULL DEFAULT 0 COMMENT '注册用户ID',
  `regtime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '注册时间',
  `owneruserid` bigint(20) NOT NULL DEFAULT 0 COMMENT '所有者用户ID',
  `alipaylogonid` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '企业支付宝账户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  PRIMARY KEY (`corpid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='企业注册表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_corp_user`
--

DROP TABLE IF EXISTS `uas_corp_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_corp_user` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `corpid` bigint(20) NOT NULL COMMENT '企业ID',
  `addtime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '加入时间',
  PRIMARY KEY (`userid`,`corpid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='企业员工表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_corp_verify`
--

DROP TABLE IF EXISTS `uas_corp_verify`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_corp_verify` (
  `corpid` bigint(20) NOT NULL COMMENT '企业ID',
  `laridcard` varchar(18) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '法定代表人身份证号',
  `licensefileid` bigint(20) NOT NULL DEFAULT 0 COMMENT '营业执照文件ID',
  `creditcode` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '统一社会信用代码',
  `larname` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '法定代表人姓名',
  `founddate` date DEFAULT NULL COMMENT '成立日期',
  `expiredate` date DEFAULT NULL COMMENT '有效日期',
  `province` varchar(2) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '省份代码',
  `city` varchar(4) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '地市代码',
  `address` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册地址',
  PRIMARY KEY (`corpid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='企业认证表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_corp_verify_app`
--

DROP TABLE IF EXISTS `uas_corp_verify_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_corp_verify_app` (
  `corpid` bigint(20) NOT NULL COMMENT '企业ID',
  `txid` bigint(20) NOT NULL COMMENT '交易号',
  `status` smallint(6) NOT NULL COMMENT '状态 1=申请 2=通过 3=拒绝',
  `laridcard` varchar(18) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '法定代表人身份证号',
  `licensefileid` bigint(20) NOT NULL DEFAULT 0 COMMENT '营业执照文件ID',
  `applyidcardimg` bigint(20) NOT NULL DEFAULT 0 COMMENT '申请人身份证照片',
  `applyvideo` bigint(20) NOT NULL DEFAULT 0 COMMENT '申请人视频',
  `videocheckcode` varchar(4) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '视频校验码',
  `applyuserid` bigint(20) NOT NULL DEFAULT 0 COMMENT '申请人用户ID',
  `applytime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '申请时间',
  `applyidcardnum` varchar(18) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '申请人身份证号',
  `creditcode` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '统一社会信用代码',
  `larname` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '法定代表人姓名',
  `founddate` date DEFAULT NULL COMMENT '成立日期',
  `expiredate` date DEFAULT NULL COMMENT '有效日期',
  `province` varchar(2) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '省份代码',
  `city` varchar(4) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '地市代码',
  `address` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册地址',
  `checkuserid` bigint(20) NOT NULL DEFAULT 0 COMMENT '审核用户ID',
  `checktime` datetime DEFAULT NULL COMMENT '审核时间',
  `checknote` varchar(18) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '审核意见',
  PRIMARY KEY (`corpid`,`txid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='企业认证申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_link_friend`
--

DROP TABLE IF EXISTS `uas_link_friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_link_friend` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `friendid` bigint(20) NOT NULL COMMENT '好友用户ID',
  `notename` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '好友备注名',
  `notes` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '好友描述',
  `labels` varchar(30) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '好友标签列表',
  PRIMARY KEY (`userid`,`friendid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='好友信息表'
 PARTITION BY LINEAR HASH (`userid`)
PARTITIONS 16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_link_friend_apply`
--

DROP TABLE IF EXISTS `uas_link_friend_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_link_friend_apply` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `friendid` bigint(20) NOT NULL COMMENT '好友用户ID',
  `txid` bigint(20) NOT NULL COMMENT '交易号',
  `status` smallint(6) NOT NULL COMMENT '状态 1=申请 2=通过 3=拒绝',
  `applynote` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '申请描述',
  `applytime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '申请时间',
  `checktime` datetime DEFAULT NULL COMMENT '审核时间',
  `checknote` varchar(50) COLLATE utf8mb4_bin DEFAULT '' COMMENT '审核意见',
  PRIMARY KEY (`userid`,`friendid`,`txid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='好友申请表'
 PARTITION BY LINEAR HASH (`userid`)
PARTITIONS 32;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_link_friend_del`
--

DROP TABLE IF EXISTS `uas_link_friend_del`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_link_friend_del` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `friendid` bigint(20) NOT NULL COMMENT '好友用户ID',
  `deletetime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '删除时间',
  `txid` bigint(20) NOT NULL COMMENT '交易号',
  `notename` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '好友备注名',
  `notes` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '好友描述',
  `labels` varchar(30) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '好友标签列表',
  PRIMARY KEY (`userid`,`friendid`,`deletetime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='删除好友信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_link_label`
--

DROP TABLE IF EXISTS `uas_link_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_link_label` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `labels` varchar(200) COLLATE utf8mb4_bin NOT NULL COMMENT '好友标签列表',
  `regtime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '注册时间',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='好友标签定义表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_user_addr`
--

DROP TABLE IF EXISTS `uas_user_addr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_user_addr` (
  `addrid` bigint(20) NOT NULL COMMENT '地址ID',
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `addtime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '添加时间',
  `isdefault` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '是否默认地址，1=是',
  `detail` varchar(30) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '详细地址',
  `province` varchar(2) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '省份代码',
  `city` varchar(4) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '地市代码',
  `district` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '区县代码',
  PRIMARY KEY (`userid`,`addrid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户地址表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_user_alipay`
--

DROP TABLE IF EXISTS `uas_user_alipay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_user_alipay` (
  `alipayuserid` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '支付宝userid',
  `alipaylogonid` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '支付宝logonid',
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `addtime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '绑定时间',
  PRIMARY KEY (`alipayuserid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户支付宝账号表'
 PARTITION BY LINEAR KEY ()
PARTITIONS 11;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_user_app`
--

DROP TABLE IF EXISTS `uas_user_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_user_app` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `appid` int(11) NOT NULL COMMENT '应用ID',
  `regtime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '注册时间',
  PRIMARY KEY (`userid`,`appid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户应用关联表'
 PARTITION BY LINEAR HASH (`userid`)
PARTITIONS 16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_user_device`
--

DROP TABLE IF EXISTS `uas_user_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_user_device` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `deviceid` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '设备串号',
  `devicetype` smallint(6) NOT NULL DEFAULT 0 COMMENT '设备类型',
  `osversion` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '操作系统版本',
  `addtime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '注册时间',
  PRIMARY KEY (`userid`,`deviceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户登录设备表'
 PARTITION BY LINEAR HASH (`userid`)
PARTITIONS 32;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_user_info`
--

DROP TABLE IF EXISTS `uas_user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_user_info` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `mobile` varchar(12) COLLATE utf8mb4_bin NOT NULL COMMENT '手机号',
  `nickname` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '昵称',
  `status` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '1' COMMENT '1=正常 2=失效',
  `regtime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '注册时间',
  `sex` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '1=男 2=女 空=不知道',
  `country` varchar(3) COLLATE utf8mb4_bin NOT NULL DEFAULT 'CHN' COMMENT '国家',
  `province` varchar(2) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '省份代码',
  `city` varchar(4) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '地市代码',
  `district` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '区县代码',
  `faceimg` bigint(20) NOT NULL DEFAULT 0 COMMENT '小头像文件ID',
  `faceimgbig` bigint(20) NOT NULL DEFAULT 0 COMMENT '大头像文件ID',
  `verified` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '是否实名认证 1=是',
  `signature` varchar(30) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '个性签名',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户基本表'
 PARTITION BY LINEAR HASH (`userid`)
PARTITIONS 32;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_user_logonid`
--

DROP TABLE IF EXISTS `uas_user_logonid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_user_logonid` (
  `logonid` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '登录名',
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`logonid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户登录名表'
 PARTITION BY LINEAR KEY ()
PARTITIONS 11;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_user_mobile`
--

DROP TABLE IF EXISTS `uas_user_mobile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_user_mobile` (
  `mobile` varchar(12) COLLATE utf8mb4_bin NOT NULL COMMENT '手机号',
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户手机号表'
 PARTITION BY LINEAR KEY ()
PARTITIONS 17;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_user_mobile_invalid`
--

DROP TABLE IF EXISTS `uas_user_mobile_invalid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_user_mobile_invalid` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `mobile` varchar(12) COLLATE utf8mb4_bin NOT NULL COMMENT '手机号',
  `voidtime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '失效时间',
  PRIMARY KEY (`userid`,`mobile`,`voidtime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='失效手机号表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_user_oper`
--

DROP TABLE IF EXISTS `uas_user_oper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_user_oper` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `txid` bigint(20) NOT NULL COMMENT '交易号',
  `appid` int(11) NOT NULL COMMENT '应用ID',
  `opertype` smallint(6) NOT NULL DEFAULT 0 COMMENT '操作类型',
  `details` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '操作详情',
  `opertime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '操作时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  PRIMARY KEY (`userid`,`txid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户变更跟踪表'
 PARTITION BY LINEAR HASH (`userid`)
PARTITIONS 32;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_user_real`
--

DROP TABLE IF EXISTS `uas_user_real`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_user_real` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `username` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '姓名',
  `idcard` varchar(18) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '身份证号',
  `verified` int(11) NOT NULL DEFAULT 0 COMMENT '1=已认证',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户实名表'
 PARTITION BY LINEAR HASH (`userid`)
PARTITIONS 8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_user_safe`
--

DROP TABLE IF EXISTS `uas_user_safe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_user_safe` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `passwd` varchar(60) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '密码',
  `logonid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '登录名',
  `email` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '电子邮箱',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户安全表'
 PARTITION BY LINEAR HASH (`userid`)
PARTITIONS 16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_user_wx`
--

DROP TABLE IF EXISTS `uas_user_wx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_user_wx` (
  `openid` varchar(30) COLLATE utf8mb4_bin NOT NULL COMMENT '微信openid',
  `unionid` varchar(30) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '微信unionid',
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `addtime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '绑定时间',
  PRIMARY KEY (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户微信账号表'
 PARTITION BY LINEAR KEY ()
PARTITIONS 17;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_wal_balance`
--

DROP TABLE IF EXISTS `uas_wal_balance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_wal_balance` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `ccy` varchar(3) COLLATE utf8mb4_bin NOT NULL DEFAULT 'CNY' COMMENT '币种',
  `balance` decimal(14,4) NOT NULL DEFAULT 0.0000 COMMENT '余额',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='钱包余额表'
 PARTITION BY LINEAR HASH (`userid`)
PARTITIONS 16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uas_wal_detail`
--

DROP TABLE IF EXISTS `uas_wal_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uas_wal_detail` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `txid` bigint(20) NOT NULL COMMENT '交易号',
  `txtype` smallint(6) NOT NULL COMMENT '交易类型',
  `ccy` varchar(3) COLLATE utf8mb4_bin NOT NULL DEFAULT 'CNY' COMMENT '币种',
  `amt` decimal(14,4) NOT NULL DEFAULT 0.0000 COMMENT '交易金额',
  `balance` decimal(14,4) NOT NULL DEFAULT 0.0000 COMMENT '钱包余额',
  `addtime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '交易时间',
  PRIMARY KEY (`userid`,`txid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='钱包明细表'
 PARTITION BY LINEAR HASH (`userid`)
PARTITIONS 16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'usp_uas'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-18 15:17:16
