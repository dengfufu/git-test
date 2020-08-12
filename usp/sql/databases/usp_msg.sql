-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 10.34.12.185    Database: usp_msg
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
-- Table structure for table `mail_send_2002`
--

DROP TABLE IF EXISTS `mail_send_2002`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mail_send_2002` (
  `mailid` bigint(20) NOT NULL COMMENT '邮件ID',
  `receivers` varchar(200) COLLATE utf8mb4_bin NOT NULL COMMENT '收件人列表',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `title` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(1000) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '邮件内容',
  `contentfile` bigint(20) NOT NULL DEFAULT 0 COMMENT '邮件内容文件ID',
  `attachfiles` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '附件文件ID列表',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `status` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `returncode` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回码',
  `returnmsg` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回消息',
  PRIMARY KEY (`mailid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='邮件发送记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `push_bulletin`
--

DROP TABLE IF EXISTS `push_bulletin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `push_bulletin` (
  `msgid` bigint(20) NOT NULL COMMENT '消息ID',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `title` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `sendstatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  PRIMARY KEY (`msgid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `push_msg_1909`
--

DROP TABLE IF EXISTS `push_msg_1909`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `push_msg_1909` (
  `msgid` bigint(20) NOT NULL COMMENT '消息ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `title` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `sendstatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `receivestatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '接收状态',
  `receivetime` datetime DEFAULT NULL COMMENT '已接收时间',
  `weburl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网页链接',
  `appurl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'APP链接',
  `wechaturl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '微信链接',
  PRIMARY KEY (`msgid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='单点消息记录表'
 PARTITION BY LINEAR HASH (`touser`)
PARTITIONS 32;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `push_msg_1910`
--

DROP TABLE IF EXISTS `push_msg_1910`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `push_msg_1910` (
  `msgid` bigint(20) NOT NULL COMMENT '消息ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `title` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `sendstatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `receivestatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '接收状态',
  `receivetime` datetime DEFAULT NULL COMMENT '已接收时间',
  `weburl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网页链接',
  `appurl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'APP链接',
  `wechaturl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '微信链接',
  PRIMARY KEY (`msgid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='单点消息记录表'
 PARTITION BY LINEAR HASH (`touser`)
PARTITIONS 32;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `push_msg_1911`
--

DROP TABLE IF EXISTS `push_msg_1911`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `push_msg_1911` (
  `msgid` bigint(20) NOT NULL COMMENT '消息ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `title` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `sendstatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `receivestatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '接收状态',
  `receivetime` datetime DEFAULT NULL COMMENT '已接收时间',
  `weburl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网页链接',
  `appurl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'APP链接',
  `wechaturl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '微信链接',
  PRIMARY KEY (`msgid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='单点消息记录表'
 PARTITION BY LINEAR HASH (`touser`)
PARTITIONS 32;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `push_msg_1912`
--

DROP TABLE IF EXISTS `push_msg_1912`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `push_msg_1912` (
  `msgid` bigint(20) NOT NULL COMMENT '消息ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `title` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `sendstatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `receivestatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '接收状态',
  `receivetime` datetime DEFAULT NULL COMMENT '已接收时间',
  `weburl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网页链接',
  `appurl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'APP链接',
  `wechaturl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '微信链接',
  PRIMARY KEY (`msgid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='单点消息记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `push_msg_2001`
--

DROP TABLE IF EXISTS `push_msg_2001`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `push_msg_2001` (
  `msgid` bigint(20) NOT NULL COMMENT '消息ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `title` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(1000) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `sendstatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `receivestatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '接收状态',
  `receivetime` datetime DEFAULT NULL COMMENT '已接收时间',
  `weburl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网页链接',
  `appurl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'APP链接',
  `wechaturl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '微信链接',
  PRIMARY KEY (`msgid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='单点消息记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `push_msg_2002`
--

DROP TABLE IF EXISTS `push_msg_2002`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `push_msg_2002` (
  `msgid` bigint(20) NOT NULL COMMENT '消息ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `title` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(1000) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `sendstatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `receivestatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '接收状态',
  `receivetime` datetime DEFAULT NULL COMMENT '已接收时间',
  `weburl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网页链接',
  `appurl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'APP链接',
  `wechaturl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '微信链接',
  PRIMARY KEY (`msgid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='单点消息记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `push_msg_200213`
--

DROP TABLE IF EXISTS `push_msg_200213`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `push_msg_200213` (
  `msgid` bigint(20) NOT NULL COMMENT '消息ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `title` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(1000) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `sendstatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `receivestatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '接收状态',
  `receivetime` datetime DEFAULT NULL COMMENT '已接收时间',
  `weburl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网页链接',
  `appurl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'APP链接',
  `wechaturl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '微信链接',
  PRIMARY KEY (`msgid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='单点消息记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `push_msg_200214`
--

DROP TABLE IF EXISTS `push_msg_200214`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `push_msg_200214` (
  `msgid` bigint(20) NOT NULL COMMENT '消息ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `title` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(1000) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `sendstatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `receivestatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '接收状态',
  `receivetime` datetime DEFAULT NULL COMMENT '已接收时间',
  `weburl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网页链接',
  `appurl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'APP链接',
  `wechaturl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '微信链接',
  PRIMARY KEY (`msgid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='单点消息记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `push_msg_2003`
--

DROP TABLE IF EXISTS `push_msg_2003`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `push_msg_2003` (
  `msgid` bigint(20) NOT NULL COMMENT '消息ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `title` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(1000) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `sendstatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `receivestatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '接收状态',
  `receivetime` datetime DEFAULT NULL COMMENT '已接收时间',
  `weburl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网页链接',
  `appurl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'APP链接',
  `wechaturl` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '微信链接',
  PRIMARY KEY (`msgid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='单点消息记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `push_notice`
--

DROP TABLE IF EXISTS `push_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `push_notice` (
  `msgid` bigint(20) NOT NULL COMMENT '消息ID',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `tousers` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '目标用户列表',
  `title` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `sendstatus` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  PRIMARY KEY (`msgid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='群发通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `push_template`
--

DROP TABLE IF EXISTS `push_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `push_template` (
  `tplid` int(11) NOT NULL COMMENT '模板ID',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `tplname` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '模板名',
  `title` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '模板标题',
  `content` varchar(1000) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '模板内容',
  `adduserid` bigint(20) NOT NULL DEFAULT 0 COMMENT '添加用户ID',
  `addtime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '添加时间',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '1' COMMENT '是否有效 1=有效 2=无效',
  PRIMARY KEY (`tplid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='消息模板表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sms_send_1908`
--

DROP TABLE IF EXISTS `sms_send_1908`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sms_send_1908` (
  `smsid` bigint(20) NOT NULL COMMENT '短信ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `smstype` smallint(6) NOT NULL DEFAULT 0 COMMENT '短信类型',
  `content` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `status` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `returncode` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回码',
  `returnmsg` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回消息',
  PRIMARY KEY (`smsid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='短信发送记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sms_send_1909`
--

DROP TABLE IF EXISTS `sms_send_1909`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sms_send_1909` (
  `smsid` bigint(20) NOT NULL COMMENT '短信ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `smstype` smallint(6) NOT NULL DEFAULT 0 COMMENT '短信类型',
  `content` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `status` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `returncode` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回码',
  `returnmsg` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回消息',
  PRIMARY KEY (`smsid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='短信发送记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sms_send_2001`
--

DROP TABLE IF EXISTS `sms_send_2001`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sms_send_2001` (
  `smsid` bigint(20) NOT NULL COMMENT '短信ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `smstype` smallint(6) NOT NULL DEFAULT 0 COMMENT '短信类型',
  `content` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `status` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `returncode` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回码',
  `returnmsg` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回消息',
  PRIMARY KEY (`smsid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='短信发送记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sms_send_2002`
--

DROP TABLE IF EXISTS `sms_send_2002`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sms_send_2002` (
  `smsid` bigint(20) NOT NULL COMMENT '短信ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `smstype` smallint(6) NOT NULL DEFAULT 0 COMMENT '短信类型',
  `content` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `status` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `returncode` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回码',
  `returnmsg` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回消息',
  PRIMARY KEY (`smsid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='短信发送记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sms_send_200213`
--

DROP TABLE IF EXISTS `sms_send_200213`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sms_send_200213` (
  `smsid` bigint(20) NOT NULL COMMENT '短信ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `smstype` smallint(6) NOT NULL DEFAULT 0 COMMENT '短信类型',
  `content` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `status` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `returncode` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回码',
  `returnmsg` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回消息',
  PRIMARY KEY (`smsid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='短信发送记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sms_send_200214`
--

DROP TABLE IF EXISTS `sms_send_200214`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sms_send_200214` (
  `smsid` bigint(20) NOT NULL COMMENT '短信ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `smstype` smallint(6) NOT NULL DEFAULT 0 COMMENT '短信类型',
  `content` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `status` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `returncode` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回码',
  `returnmsg` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回消息',
  PRIMARY KEY (`smsid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='短信发送记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sms_send_2003`
--

DROP TABLE IF EXISTS `sms_send_2003`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sms_send_2003` (
  `smsid` bigint(20) NOT NULL COMMENT '短信ID',
  `touser` bigint(20) NOT NULL COMMENT '目标用户',
  `appid` int(11) NOT NULL DEFAULT 0 COMMENT '应用ID',
  `smstype` smallint(6) NOT NULL DEFAULT 0 COMMENT '短信类型',
  `content` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '通知内容',
  `fixedtime` datetime DEFAULT NULL COMMENT '指定推送时间',
  `senttime` datetime DEFAULT NULL COMMENT '已发送时间',
  `status` smallint(6) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `receiptid` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '回执ID',
  `returncode` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回码',
  `returnmsg` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '返回消息',
  PRIMARY KEY (`smsid`,`touser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='短信发送记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'usp_msg'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-18 15:17:04
