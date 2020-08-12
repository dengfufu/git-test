-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 10.34.12.185    Database: usp_device
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
-- Table structure for table `device_brand`
--

DROP TABLE IF EXISTS `device_brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_brand` (
  `id` bigint(20) NOT NULL COMMENT '品牌ID',
  `logo` bigint(20) NOT NULL DEFAULT 0 COMMENT '品牌logo',
  `name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '品牌名称',
  `short_name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '品牌简称',
  `website` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网址',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否可用',
  `corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '企业ID',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='设备品牌表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_info`
--

DROP TABLE IF EXISTS `device_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_info` (
  `device_id` bigint(20) NOT NULL COMMENT '设备ID',
  `small_class_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备小类ID',
  `sepcification_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备规格ID',
  `brand_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备品牌ID',
  `model_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备型号ID',
  `serial` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '厂商序列号',
  `demander_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务委托方',
  `service_corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务商',
  `factory_date` date DEFAULT NULL COMMENT '出厂日期',
  `purchase_date` date DEFAULT NULL COMMENT '购买日期',
  `warranty_start_date` date DEFAULT NULL COMMENT '保修开始日期',
  `warranty_end_date` date DEFAULT NULL COMMENT '保修结束日期',
  `warranty_status` int(11) NOT NULL DEFAULT 0 COMMENT '保修状态，1=保内 2=保外',
  `warranty_note` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '保修说明',
  `warranty_mode` smallint(6) NOT NULL DEFAULT 0 COMMENT '维保方式，10=整机保 20=单次保',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  `contact_name` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '联系人姓名',
  `contact_phone` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '联系人电话',
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='设备档案基本表'
 PARTITION BY LINEAR HASH (`device_id`)
PARTITIONS 16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_large_class`
--

DROP TABLE IF EXISTS `device_large_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_large_class` (
  `id` bigint(20) NOT NULL COMMENT '大类ID',
  `name` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '大类名称',
  `sort_no` int(11) NOT NULL DEFAULT 0 COMMENT '顺序号',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否可用',
  `corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '企业ID',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='设备大类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_locate`
--

DROP TABLE IF EXISTS `device_locate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_locate` (
  `device_id` bigint(20) NOT NULL COMMENT '设备ID',
  `custom_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '委托方客户关系编号',
  `district` varchar(6) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '行政区划',
  `zone` smallint(6) NOT NULL DEFAULT 0 COMMENT '城郊，1=市区 2=郊县',
  `address` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '详细地址',
  `branch_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备网点',
  `device_code` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备唯一编号，用户企业定义的设备号',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '设备状态，1=运行 2=暂停 3=报废',
  `install_date` date DEFAULT NULL COMMENT '安装日期',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备描述',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '维度',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='设备位置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_model`
--

DROP TABLE IF EXISTS `device_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_model` (
  `id` bigint(20) NOT NULL COMMENT '型号ID',
  `name` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '型号名称',
  `brand_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备品牌',
  `small_class_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备小类',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否可用',
  `corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '企业ID',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='设备型号表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_service`
--

DROP TABLE IF EXISTS `device_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_service` (
  `device_id` bigint(20) NOT NULL COMMENT '设备ID',
  `service_branch` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务网点',
  `work_manager` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务主管',
  `engineer` bigint(20) NOT NULL DEFAULT 0 COMMENT '服务工程师',
  `service_note` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '服务说明',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='设备服务信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_small_class`
--

DROP TABLE IF EXISTS `device_small_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_small_class` (
  `id` bigint(20) NOT NULL COMMENT '小类ID',
  `large_class_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '大类ID',
  `name` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '小类名称',
  `sort_no` int(11) NOT NULL DEFAULT 0 COMMENT '顺序号',
  `description` varchar(200) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否可用',
  `corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '企业ID',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='设备小类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_specification`
--

DROP TABLE IF EXISTS `device_specification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_specification` (
  `id` bigint(20) NOT NULL COMMENT '规格ID',
  `small_class_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '小类ID',
  `name` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '规格名称',
  `enabled` varchar(1) COLLATE utf8mb4_bin NOT NULL DEFAULT 'Y' COMMENT '是否可用',
  `corp` bigint(20) NOT NULL DEFAULT 0 COMMENT '企业ID',
  `operator` bigint(20) NOT NULL DEFAULT 0 COMMENT '操作人',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='设备规格表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'usp_device'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-18 15:16:55
