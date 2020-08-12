-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 10.34.12.185    Database: usp_anyfix
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
-- Table structure for table `custom_field`
--

DROP TABLE IF EXISTS `custom_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `custom_field` (
  `field_id` bigint(20) NOT NULL COMMENT '字段ID',
  `field_name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '字段名称',
  `field_type` int(4) NOT NULL DEFAULT 0 COMMENT '字段类型',
  `required` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '是否必填',
  `form_type` int(4) NOT NULL DEFAULT 0 COMMENT '业务表单类型',
  `common` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '是否公共',
  `corp_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '企业id',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`field_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='自定义字段配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_field_data`
--

DROP TABLE IF EXISTS `custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `custom_field_data` (
  `data_id` bigint(20) NOT NULL COMMENT '数据ID',
  `form_type` int(11) NOT NULL DEFAULT 0 COMMENT '业务表单类型',
  `form_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '业务表单ID',
  `field_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '字段ID',
  `field_name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '字段名称',
  `field_type` int(11) NOT NULL DEFAULT 0 COMMENT '字段类型',
  `field_value` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '字段值',
  PRIMARY KEY (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='自定义字段数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_field_data_source`
--

DROP TABLE IF EXISTS `custom_field_data_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `custom_field_data_source` (
  `source_id` bigint(20) NOT NULL COMMENT '数据源id',
  `field_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '自定义字段id',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否可用',
  `source_value` varchar(1000) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '数据源值',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='自定义字段数据源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_reason`
--

DROP TABLE IF EXISTS `custom_reason`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `custom_reason` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '原因标题',
  `custom_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '客户企业编号',
  `reason_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '原因类型 1=客服撤单',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '详细描述',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '是否可用 1正常 2失效 ',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='客户原因表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `demander_custom`
--

DROP TABLE IF EXISTS `demander_custom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `demander_custom` (
  `custom_id` bigint(20) NOT NULL COMMENT '主键',
  `demander_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务委托方企业编号',
  `custom_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户企业编号',
  `custom_corp_name` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '用户企业名称',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '是否可用，Y=可用，N=不可用',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '操作时间',
  PRIMARY KEY (`custom_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='服务委托方与用户企业关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `demander_service`
--

DROP TABLE IF EXISTS `demander_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `demander_service` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `demander_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务委托方企业编号',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商企业编号',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否可用，Y=可用，N=不可用',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='服务委托方与服务商关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_branch`
--

DROP TABLE IF EXISTS `device_branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_branch` (
  `branch_id` bigint(20) NOT NULL COMMENT '设备网点编号',
  `custom_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '客户编号',
  `custom_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '客户企业编号',
  `upper_branch_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '上级网点编号',
  `branch_name` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网点名称',
  `province` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '省份代码',
  `city` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '城市代码',
  `district` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '区县代码',
  `address` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '详细地址',
  `branch_phone` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网点电话',
  `contact_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '联系人编号',
  `contact_name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '联系人姓名',
  `contact_phone` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '联系人电话',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否可用',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '维度',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '操作时间',
  PRIMARY KEY (`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='设备网点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_branch_user`
--

DROP TABLE IF EXISTS `device_branch_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_branch_user` (
  `branch_id` bigint(20) NOT NULL COMMENT '网点编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户编号',
  `add_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '添加时间',
  PRIMARY KEY (`branch_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='设备网点人员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fault_type`
--

DROP TABLE IF EXISTS `fault_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fault_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '故障现象名',
  `demander_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务委托方企业编号',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '是否可用',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='故障现象表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_branch`
--

DROP TABLE IF EXISTS `service_branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_branch` (
  `branch_id` bigint(20) NOT NULL COMMENT '服务网点编号',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商企业编号',
  `branch_name` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网点名称',
  `province` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '省份代码',
  `city` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '城市代码',
  `district` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '区县代码',
  `address` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '详细地址',
  `type` int(11) NOT NULL DEFAULT 0 COMMENT '网点类型',
  `branch_phone` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网点电话',
  `contact_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '联系人编号',
  `contact_name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '联系人姓名',
  `contact_phone` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '联系人电话',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否可用',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '操作时间',
  PRIMARY KEY (`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='服务网点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_branch_user`
--

DROP TABLE IF EXISTS `service_branch_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_branch_user` (
  `branch_id` bigint(20) NOT NULL COMMENT '网点编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户编号',
  `add_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '添加时间',
  PRIMARY KEY (`branch_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='服务网点人员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_evaluate`
--

DROP TABLE IF EXISTS `service_evaluate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_evaluate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '指标编号',
  `name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '指标名称',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商企业编号',
  `scores` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '分值列表    形式为1，2，3，4，5',
  `labels` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标签列表 形式为差，中，优',
  `show_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '显示样式，1为星星，2为列表',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否可用 Y正常 N失效 ',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='服务评价指标表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_evaluate_tag`
--

DROP TABLE IF EXISTS `service_evaluate_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_evaluate_tag` (
  `tag_id` int(8) NOT NULL COMMENT '标签编号',
  `name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '指标名称',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商企业编号',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否可用 Y正常 N失效 ',
  `operate_time` datetime DEFAULT current_timestamp() COMMENT '操作时间',
  `operator` bigint(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='服务评价标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_item`
--

DROP TABLE IF EXISTS `service_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '服务项目名称',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商企业编号',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '详细描述',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '是否可用 1正常 2失效 ',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='服务项目表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_item_fee_rule`
--

DROP TABLE IF EXISTS `service_item_fee_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_item_fee_rule` (
  `rule_id` bigint(20) NOT NULL COMMENT '规则编号',
  `rule_name` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '规则名称',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商编号',
  `service_item_id` bigint(20) NOT NULL COMMENT '服务项目编号',
  `start_date` date DEFAULT NULL COMMENT '适用起始日期',
  `end_date` date DEFAULT NULL COMMENT '适用结束日期',
  `unit_price` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '每单费用',
  `condition_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '条件配置',
  PRIMARY KEY (`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单服务项目结算规则';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_reason`
--

DROP TABLE IF EXISTS `service_reason`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_reason` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '原因标题',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商企业编号',
  `reason_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '原因类型 1=客服退单、2=客服撤回派工、3=工程师拒绝派单、4=工程师退回派工',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '详细描述',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '是否可用 1正常 2失效 ',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='服务商原因表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_remote_way`
--

DROP TABLE IF EXISTS `service_remote_way`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_remote_way` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '处理方式',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商企业编号',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '详细描述',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '是否可用 1正常 2失效 ',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operator` bigint(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='远程处理方式表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settle_branch`
--

DROP TABLE IF EXISTS `settle_branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settle_branch` (
  `settle_id` bigint(20) NOT NULL COMMENT '结算单编号',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商编号',
  `branch_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务网点编号',
  `start_date` date DEFAULT NULL COMMENT '结算起始日期',
  `end_date` date DEFAULT NULL COMMENT '结算截止日期',
  `work_quantity` int(11) NOT NULL DEFAULT 0 COMMENT '结算工单总数',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '结算单状态',
  `note` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '备注',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '结算人员',
  `operate_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '结算时间',
  `result` int(11) NOT NULL DEFAULT 0 COMMENT '对账结果',
  `check_user` bigint(20) NOT NULL DEFAULT 0 COMMENT '对账人员',
  `check_time` datetime DEFAULT NULL COMMENT '对账时间',
  PRIMARY KEY (`settle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='网点结算单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settle_branch_detail`
--

DROP TABLE IF EXISTS `settle_branch_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settle_branch_detail` (
  `work_id` bigint(20) NOT NULL COMMENT '工单编号',
  `settle_id` bigint(20) NOT NULL COMMENT '结算单编号',
  PRIMARY KEY (`work_id`,`settle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='网点结算单明细';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settle_custom`
--

DROP TABLE IF EXISTS `settle_custom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settle_custom` (
  `settle_id` bigint(20) NOT NULL COMMENT '结算单编号',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商编号',
  `custom_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '客户编号',
  `start_date` date DEFAULT NULL COMMENT '结算起始日期',
  `end_date` date DEFAULT NULL COMMENT '结算截止日期',
  `work_quantity` int(11) NOT NULL DEFAULT 0 COMMENT '结算工单总数',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '结算单状态',
  `note` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '备注',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '结算人员',
  `operate_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '结算时间',
  `result` int(11) NOT NULL DEFAULT 0 COMMENT '对账结果',
  `check_user` bigint(20) NOT NULL DEFAULT 0 COMMENT '对账人员',
  `check_time` datetime DEFAULT NULL COMMENT '对账时间',
  PRIMARY KEY (`settle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='客户结算单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settle_custom_detail`
--

DROP TABLE IF EXISTS `settle_custom_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settle_custom_detail` (
  `work_id` bigint(20) NOT NULL COMMENT '工单编号',
  `settle_id` bigint(20) NOT NULL COMMENT '结算单编号',
  PRIMARY KEY (`work_id`,`settle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='客户结算单明细';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settle_demander`
--

DROP TABLE IF EXISTS `settle_demander`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settle_demander` (
  `settle_id` bigint(20) NOT NULL COMMENT '结算单编号',
  `settle_code` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '结算单号',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商编号',
  `demander_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '供应商编号',
  `start_date` date DEFAULT NULL COMMENT '结算起始日期',
  `end_date` date DEFAULT NULL COMMENT '结算截止日期',
  `work_quantity` int(11) NOT NULL DEFAULT 0 COMMENT '结算工单总数',
  `settle_fee` decimal(14,2) NOT NULL DEFAULT 0.00 COMMENT '结算费用',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '结算单状态，1=待核对，2=核对通过，3=核对不通过',
  `note` varchar(400) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '备注',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '结算人员',
  `operate_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '结算时间',
  `check_user` bigint(20) NOT NULL DEFAULT 0 COMMENT '对账人员',
  `check_time` datetime DEFAULT NULL COMMENT '对账时间',
  `check_note` varchar(400) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '核对备注',
  PRIMARY KEY (`settle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='供应商结算单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settle_demander_detail`
--

DROP TABLE IF EXISTS `settle_demander_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settle_demander_detail` (
  `detail_id` bigint(20) NOT NULL COMMENT '明细编号',
  `work_id` bigint(20) NOT NULL COMMENT '工单编号',
  `settle_id` bigint(20) NOT NULL COMMENT '结算单编号',
  PRIMARY KEY (`detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='供应商结算单明细';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settle_staff`
--

DROP TABLE IF EXISTS `settle_staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settle_staff` (
  `settle_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '结算单编号',
  `record_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '结算记录编号',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '人员编号',
  `work_quantity` int(11) NOT NULL DEFAULT 0 COMMENT '结算工单总数',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '结算单状态',
  `result` int(11) NOT NULL DEFAULT 0 COMMENT '对账结果',
  `check_user` bigint(20) NOT NULL DEFAULT 0 COMMENT '对账人员',
  `check_time` datetime DEFAULT NULL COMMENT '对账时间',
  PRIMARY KEY (`settle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='员工结算单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settle_staff_detail`
--

DROP TABLE IF EXISTS `settle_staff_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settle_staff_detail` (
  `work_id` bigint(20) NOT NULL COMMENT '工单编号',
  `settle_id` bigint(20) NOT NULL COMMENT '结算单编号',
  PRIMARY KEY (`work_id`,`settle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='员工结算单明细';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settle_staff_record`
--

DROP TABLE IF EXISTS `settle_staff_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settle_staff_record` (
  `record_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '结算单编号',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商编号',
  `record_name` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '记录名称',
  `start_date` date DEFAULT NULL COMMENT '结算起始日期',
  `end_date` date DEFAULT NULL COMMENT '结算截止日期',
  `note` varchar(400) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '备注',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '结算人员',
  `operate_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '结算时间',
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='员工结算记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `staff_skill`
--

DROP TABLE IF EXISTS `staff_skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff_skill` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `corp_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '企业编号',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户编号',
  `demander_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务委托方企业',
  `work_types` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '工单系统类型多个用“,”隔开',
  `large_class_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备大类',
  `small_class_ids` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备小类，多个用“,”隔开',
  `brand_ids` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备品牌，多个用“,”隔开',
  `model_ids` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备型号，多个用“,”隔开',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工程师技能表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_assign`
--

DROP TABLE IF EXISTS `work_assign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_assign` (
  `assign_id` bigint(20) NOT NULL COMMENT '派单主键',
  `work_id` bigint(20) NOT NULL COMMENT '工单ID',
  `staff_id` bigint(20) NOT NULL COMMENT '客户ID',
  `remark` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '派单备注',
  `assign_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '派单时间',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`assign_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='派单基本表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_assign_engineer`
--

DROP TABLE IF EXISTS `work_assign_engineer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_assign_engineer` (
  `assign_id` bigint(20) NOT NULL COMMENT '派单主键',
  `engineer_id` bigint(20) NOT NULL COMMENT '工程师ID',
  PRIMARY KEY (`assign_id`,`engineer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='派单工程师表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_assign_mode`
--

DROP TABLE IF EXISTS `work_assign_mode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_assign_mode` (
  `id` bigint(20) NOT NULL COMMENT '唯一主键',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商',
  `auto_mode` int(11) NOT NULL DEFAULT 0 COMMENT '自动派单模式，11=派给设备负责工程师、12=派给小组、13=距离优先、14=派给网点所有人',
  `manual_mode` int(11) NOT NULL DEFAULT 0 COMMENT '人工派单模式',
  `assign_rule` bigint(20) NOT NULL DEFAULT 0 COMMENT '自动派单规则',
  `condition_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '条件配置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='派单模式配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_assign_rule`
--

DROP TABLE IF EXISTS `work_assign_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_assign_rule` (
  `id` bigint(20) NOT NULL COMMENT '唯一主键',
  `user_list` varchar(1000) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '人员列表',
  `distance` int(11) NOT NULL DEFAULT 0 COMMENT '派单半径',
  `waiting` int(11) NOT NULL DEFAULT 0 COMMENT '等待认领时间',
  `skilled` int(11) NOT NULL DEFAULT 0 COMMENT '匹配工程师技能',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='自动派单规则';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_basic_fee_rule`
--

DROP TABLE IF EXISTS `work_basic_fee_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_basic_fee_rule` (
  `rule_id` bigint(20) NOT NULL COMMENT '规则编号',
  `rule_name` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '规则名称',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商编号',
  `start_date` date DEFAULT NULL COMMENT '适用起始日期',
  `end_date` date DEFAULT NULL COMMENT '适用结束日期',
  `unit_price` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '每单费用',
  `service_mode` int(11) DEFAULT 0 COMMENT '服务方式，1=现场，2=远程',
  `condition_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '条件配置',
  PRIMARY KEY (`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单基础服务费规则';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_booking`
--

DROP TABLE IF EXISTS `work_booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_booking` (
  `booking_id` bigint(20) NOT NULL COMMENT '预约 ID',
  `work_id` bigint(20) NOT NULL COMMENT '工单 ID',
  `book_time_begin` datetime NOT NULL COMMENT '预约时间开始',
  `book_time_end` datetime NOT NULL COMMENT '预约时间结束',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `operator` bigint(20) NOT NULL COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否有效',
  PRIMARY KEY (`booking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单预约表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_condition`
--

DROP TABLE IF EXISTS `work_condition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_condition` (
  `id` bigint(20) NOT NULL COMMENT '唯一主键',
  `demander_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务委托方',
  `custom_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '客户关系编号',
  `custom_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备客户',
  `work_type` int(11) NOT NULL DEFAULT 0 COMMENT '工单类型',
  `large_class_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备大类',
  `small_class_id` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备小类',
  `brand_id` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备品牌',
  `model_id` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备型号',
  `district` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '行政区划',
  `device_branch` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备网点',
  `device_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='条件配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_deal`
--

DROP TABLE IF EXISTS `work_deal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_deal` (
  `work_id` bigint(20) NOT NULL COMMENT '工单ID',
  `device_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备ID',
  `demander_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务委托方',
  `work_code` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '工单编号',
  `work_status` int(11) NOT NULL DEFAULT 0 COMMENT '工单状态',
  `creator` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商',
  `service_branch` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务网点',
  `dispatch_staff` bigint(20) NOT NULL DEFAULT 0 COMMENT '分配人员',
  `dispatch_time` datetime DEFAULT NULL COMMENT '分配时间',
  `handle_staff` bigint(20) NOT NULL DEFAULT 0 COMMENT '受理人员',
  `handle_time` datetime DEFAULT NULL COMMENT '受理时间',
  `service_mode` int(11) NOT NULL DEFAULT 0 COMMENT '服务方式 1=现场 2=远程',
  `assign_mode` int(11) NOT NULL DEFAULT 0 COMMENT '派单模式',
  `assign_staff` bigint(20) NOT NULL DEFAULT 0 COMMENT '派单人员',
  `assign_time` datetime DEFAULT NULL COMMENT '派单时间',
  `engineer` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务工程师',
  `together_engineers` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '协同工程师，多个用逗号分隔',
  `help_names` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '外部协同人员',
  `accept_time` datetime DEFAULT NULL COMMENT '认领时间',
  `book_time_begin` datetime DEFAULT NULL COMMENT '预约时间开始',
  `book_time_end` datetime DEFAULT NULL COMMENT '预约时间结束',
  `sign_time` datetime DEFAULT NULL COMMENT '签到时间',
  `start_time` datetime DEFAULT NULL COMMENT '服务开始时间',
  `finish_time` datetime DEFAULT NULL COMMENT '服务完成时间',
  `evaluate_time` datetime DEFAULT NULL COMMENT '客户评价时间',
  `demander_check_status` int(11) NOT NULL DEFAULT 1 COMMENT '供应商核对结果，1=待核对，2=核对通过，3=核对不通过',
  `demander_check_note` varchar(400) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '供应商核对备注',
  `demander_check_user` bigint(20) NOT NULL DEFAULT 0 COMMENT '供应商核对人员编号',
  `demander_check_time` datetime DEFAULT NULL COMMENT '供应商核对时间',
  `service_check_status` int(11) NOT NULL DEFAULT 1 COMMENT '服务商审核结果，1=待审核，2=审核通过，3=审核不通过',
  `service_check_note` varchar(400) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '服务商审核备注',
  `service_check_user` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商核对人员编号',
  `service_check_time` datetime DEFAULT NULL COMMENT '服务商核对时间',
  `review_staff` bigint(20) NOT NULL DEFAULT 0 COMMENT '回访人员',
  `review_time` datetime DEFAULT NULL COMMENT '回访时间',
  PRIMARY KEY (`work_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单处理信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_dispatch_service_branch`
--

DROP TABLE IF EXISTS `work_dispatch_service_branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_dispatch_service_branch` (
  `id` bigint(20) NOT NULL COMMENT '唯一主键',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商',
  `service_branch` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务网点',
  `handle_type` int(11) NOT NULL DEFAULT 1 COMMENT '受理方式 1=自动受理，2=手动受理',
  `service_mode` int(11) NOT NULL DEFAULT 0 COMMENT '服务模式',
  `condition_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '条件配置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_dispatch_service_corp`
--

DROP TABLE IF EXISTS `work_dispatch_service_corp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_dispatch_service_corp` (
  `id` bigint(20) NOT NULL COMMENT '唯一主键',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商',
  `condition_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '条件配置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_evaluate`
--

DROP TABLE IF EXISTS `work_evaluate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_evaluate` (
  `work_id` bigint(20) NOT NULL COMMENT '工单ID',
  `remark` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '评价内容',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '操作时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  PRIMARY KEY (`work_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='客户评价表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_evaluate_index`
--

DROP TABLE IF EXISTS `work_evaluate_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_evaluate_index` (
  `work_id` bigint(20) NOT NULL COMMENT '工单ID',
  `evaluate_id` int(8) NOT NULL COMMENT '评价指标',
  `score` int(8) NOT NULL DEFAULT 0 COMMENT '评价值',
  PRIMARY KEY (`work_id`,`evaluate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='客户评价指标表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_evaluate_tag`
--

DROP TABLE IF EXISTS `work_evaluate_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_evaluate_tag` (
  `work_id` bigint(20) NOT NULL COMMENT '工单ID',
  `tag_id` int(8) NOT NULL COMMENT '评价标签',
  PRIMARY KEY (`work_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='客户评价标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_fee`
--

DROP TABLE IF EXISTS `work_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_fee` (
  `work_id` bigint(20) NOT NULL COMMENT '工单ID',
  `travel_fee` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '差旅费',
  `service_item_fee` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '服务项目费用',
  `basic_service_fee` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '工单基础服务费',
  `ware_use_fee` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '换上备件费用',
  `ware_post_fee` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '换下备件邮寄费',
  `other_fee` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '其他费用',
  `other_fee_note` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '其他费用说明',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`work_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单费用表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_finish`
--

DROP TABLE IF EXISTS `work_finish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_finish` (
  `work_id` bigint(20) NOT NULL COMMENT '工单编号，主键',
  `solved` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否解决',
  `description` varchar(500) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '服务情况',
  `start_time` datetime NOT NULL COMMENT '服务开始时间',
  `end_time` datetime NOT NULL COMMENT '服务完成时间',
  `service_item` int(11) NOT NULL DEFAULT 0 COMMENT '服务项目',
  `remote_way` int(11) NOT NULL DEFAULT 0 COMMENT '远程处理方式',
  `files` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '服务完成附件',
  `signature` bigint(20) NOT NULL DEFAULT 0 COMMENT '客户签名图片',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '维度',
  `operator` bigint(20) NOT NULL COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`work_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单服务完成表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_operate`
--

DROP TABLE IF EXISTS `work_operate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_operate` (
  `id` bigint(20) NOT NULL COMMENT '唯一主键',
  `work_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '工单ID',
  `refer_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '关联ID',
  `work_status` int(11) NOT NULL DEFAULT 0 COMMENT '工单状态',
  `operate_type` int(11) NOT NULL DEFAULT 0 COMMENT '处理类型',
  `corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '处理组织',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '处理人',
  `operate_time` datetime NOT NULL COMMENT '处理时间',
  `summary` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '处理描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单操作过程表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_post`
--

DROP TABLE IF EXISTS `work_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_post` (
  `post_id` bigint(20) NOT NULL COMMENT '邮寄单主键',
  `work_id` bigint(20) NOT NULL COMMENT '工单ID',
  `post_way` int(11) NOT NULL DEFAULT 0 COMMENT '邮寄方式,1=快递，2=物流，3=托运',
  `post_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '快递公司',
  `post_corp_name` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '快递公司名称',
  `post_number` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '邮寄单号',
  `postage` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '邮寄费',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单邮寄费';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_request`
--

DROP TABLE IF EXISTS `work_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_request` (
  `work_id` bigint(20) NOT NULL COMMENT '工单ID',
  `relate_work_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '关联工单ID，标记拆单',
  `work_code` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '工单号',
  `check_work_code` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '对账工单号',
  `work_type` int(11) NOT NULL DEFAULT 0 COMMENT '工单类型',
  `source` int(11) NOT NULL DEFAULT 0 COMMENT '请求来源 1=APP，2=微信，3=PC',
  `service_request` varchar(500) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '服务请求',
  `demander_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '委托商',
  `custom_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '客户企业编号',
  `custom_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '客户ID',
  `device_branch` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备网点ID',
  `district` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '行政区划',
  `zone` smallint(6) NOT NULL DEFAULT 0 COMMENT '城郊，1=市区 2=郊县',
  `address` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '详细地址',
  `contact_name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '联系人姓名',
  `contact_phone` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '联系人电话',
  `book_time_begin` datetime DEFAULT NULL COMMENT '预约时间开始',
  `book_time_end` datetime DEFAULT NULL COMMENT '预约时间结束',
  `priority` int(11) NOT NULL DEFAULT 0 COMMENT '优先级',
  `small_class` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备小类',
  `specification` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备规格ID',
  `device_code` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备编号',
  `serial` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '厂商序列号',
  `brand` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备品牌ID',
  `model` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备型号ID',
  `model_name` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备型号名称',
  `warranty` int(11) NOT NULL DEFAULT 0 COMMENT '保修状态',
  `warranty_mode` smallint(6) NOT NULL DEFAULT 0 COMMENT '维保方式，10=整机保 20=单次保',
  `fault_time` datetime DEFAULT NULL COMMENT '故障时间',
  `fault_type` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '故障现象',
  `fault_code` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '故障代码',
  `files` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '工单附件',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '维度',
  `creator` bigint(20) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`work_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单服务请求表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_sign`
--

DROP TABLE IF EXISTS `work_sign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_sign` (
  `sign_id` bigint(20) NOT NULL COMMENT '签到 ID',
  `work_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '工单 ID',
  `sign_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '签到时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `sign_address` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '签到地点',
  `sign_img` bigint(20) NOT NULL DEFAULT 0 COMMENT '签到照片',
  `deviation` int(11) NOT NULL DEFAULT 0 COMMENT '偏差',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '操作时间',
  PRIMARY KEY (`sign_id`,`work_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单签到表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_transfer`
--

DROP TABLE IF EXISTS `work_transfer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_transfer` (
  `id` bigint(20) NOT NULL COMMENT '唯一主键',
  `work_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '工单ID',
  `mode` int(11) NOT NULL DEFAULT 0 COMMENT '流转方式\n1=自动分配\n2=客服分配\n3=自动受理\n4=客服受理\n5=客服转处理\n6=客服退单\n7=客户撤单\n8=派单撤回\n9=工单认领\n10=拒绝派单',
  `refer_form` bigint(20) NOT NULL DEFAULT 0 COMMENT '关联表单ID',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商',
  `service_branch` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商网点',
  `service_mode` int(11) NOT NULL DEFAULT 0 COMMENT '服务方式',
  `reason_id` int(11) NOT NULL DEFAULT 0 COMMENT '原因码',
  `note` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '备注',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '处理人',
  `operate_time` datetime DEFAULT current_timestamp() COMMENT '处理时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单流转表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_type`
--

DROP TABLE IF EXISTS `work_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '类型名称',
  `demander_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务委托方企业编号',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '详细描述',
  `sys_type` int(11) DEFAULT 0 COMMENT '系统类型 1=维修 2=巡检 3=安装 4=咨询 5=投诉 6=其他',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否可用',
  `operator` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_ware_recycle`
--

DROP TABLE IF EXISTS `work_ware_recycle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_ware_recycle` (
  `recycle_id` bigint(20) NOT NULL COMMENT '主键，编号',
  `work_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '工单编号',
  `ware_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '物品编号',
  `model_name` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '备件型号名称',
  `brand_name` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '品牌名称',
  `catalog_name` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '分类名称',
  `ware_serial` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '物品序列号',
  `quantity` int(11) NOT NULL DEFAULT 1 COMMENT '数量',
  `operator` bigint(20) NOT NULL COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`recycle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单回收物品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_ware_used`
--

DROP TABLE IF EXISTS `work_ware_used`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_ware_used` (
  `used_id` bigint(20) NOT NULL COMMENT '主键，编号',
  `work_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '工单编号',
  `ware_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '物品编号',
  `model_name` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '备件型号名称',
  `brand_name` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '品牌名称',
  `catalog_name` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '分类名称',
  `ware_serial` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '物品序列号',
  `quantity` int(11) NOT NULL DEFAULT 1 COMMENT '数量',
  `unit_price` decimal(10,4) NOT NULL DEFAULT 0.0000 COMMENT '单价',
  `tax_rate` decimal(5,4) NOT NULL DEFAULT 0.0000 COMMENT '税率',
  `operator` bigint(20) NOT NULL COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`used_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='工单使用物品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'usp_anyfix'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-18 15:18:09
