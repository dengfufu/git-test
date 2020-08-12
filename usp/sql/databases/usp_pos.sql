-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 10.34.12.185    Database: usp_pos
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
-- Table structure for table `pos_last`
--

DROP TABLE IF EXISTS `pos_last`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_last` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  `client` smallint(6) NOT NULL COMMENT '终端类型 1=Android，2=IOS，3=微信',
  PRIMARY KEY (`userid`),
  KEY `uas_pos_last_postime_IDX` (`postime`) USING BTREE,
  KEY `uas_pos_last_lon_IDX` (`lon`) USING BTREE,
  KEY `uas_pos_last_lat_IDX` (`lat`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户最近位置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200201`
--

DROP TABLE IF EXISTS `pos_track_200201`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200201` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200201_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200201_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200202`
--

DROP TABLE IF EXISTS `pos_track_200202`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200202` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200202_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200202_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200203`
--

DROP TABLE IF EXISTS `pos_track_200203`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200203` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200203_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200203_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200204`
--

DROP TABLE IF EXISTS `pos_track_200204`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200204` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200204_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200204_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200205`
--

DROP TABLE IF EXISTS `pos_track_200205`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200205` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200205_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200205_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200206`
--

DROP TABLE IF EXISTS `pos_track_200206`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200206` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200206_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200206_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200207`
--

DROP TABLE IF EXISTS `pos_track_200207`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200207` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200206_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200206_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200208`
--

DROP TABLE IF EXISTS `pos_track_200208`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200208` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200206_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200206_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200209`
--

DROP TABLE IF EXISTS `pos_track_200209`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200209` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200209_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200209_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200210`
--

DROP TABLE IF EXISTS `pos_track_200210`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200210` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,7) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200210_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200210_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200211`
--

DROP TABLE IF EXISTS `pos_track_200211`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200211` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200211_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200211_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200212`
--

DROP TABLE IF EXISTS `pos_track_200212`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200212` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200212_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200212_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200213`
--

DROP TABLE IF EXISTS `pos_track_200213`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200213` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200213_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200213_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200214`
--

DROP TABLE IF EXISTS `pos_track_200214`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200214` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200214_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200214_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200215`
--

DROP TABLE IF EXISTS `pos_track_200215`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200215` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200215_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200215_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200216`
--

DROP TABLE IF EXISTS `pos_track_200216`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200216` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200216_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200216_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200217`
--

DROP TABLE IF EXISTS `pos_track_200217`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200217` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200217_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200217_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200218`
--

DROP TABLE IF EXISTS `pos_track_200218`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200218` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200218_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200218_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200219`
--

DROP TABLE IF EXISTS `pos_track_200219`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200219` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200219_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200219_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200220`
--

DROP TABLE IF EXISTS `pos_track_200220`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200220` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200220_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200220_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200221`
--

DROP TABLE IF EXISTS `pos_track_200221`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200221` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,6) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200221_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200221_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200222`
--

DROP TABLE IF EXISTS `pos_track_200222`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200222` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,7) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200222_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200222_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pos_track_200223`
--

DROP TABLE IF EXISTS `pos_track_200223`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pos_track_200223` (
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `postime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '定位时间',
  `lon` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '经度',
  `lat` decimal(10,6) NOT NULL DEFAULT 0.000000 COMMENT '纬度',
  `client` smallint(6) NOT NULL DEFAULT 0 COMMENT '终端类型 1=Android，2=IOS，3=微信',
  `poshour` smallint(6) NOT NULL DEFAULT 0 COMMENT '小时区间，0 - 23',
  `addr` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址信息',
  `bdlon` decimal(10,6) NOT NULL COMMENT '百度坐标系经度',
  `bdlat` decimal(10,8) NOT NULL COMMENT '百度坐标系纬度',
  `radius` double DEFAULT NULL COMMENT '定位精度',
  KEY `pos_track_200223_userid_IDX` (`userid`) USING BTREE,
  KEY `pos_track_200223_postime_IDX` (`postime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户轨迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'usp_pos'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-18 15:17:09
