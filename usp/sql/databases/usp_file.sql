-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 10.34.12.185    Database: usp_file
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
-- Table structure for table `file_info`
--

DROP TABLE IF EXISTS `file_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_info` (
  `fileid` bigint(20) NOT NULL COMMENT '文件ID',
  `appid` int(11) NOT NULL COMMENT '应用ID',
  `biztype` smallint(6) NOT NULL DEFAULT 0 COMMENT '业务类别',
  `format` smallint(6) NOT NULL DEFAULT 0 COMMENT '文件格式 1=图片 2=视频 3=音频 4=文档 0=未知',
  `path` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件路径',
  `filename` varchar(100) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '原文件名',
  `size` bigint(20) NOT NULL COMMENT '文件大小',
  `width` smallint(6) NOT NULL DEFAULT 0 COMMENT '宽',
  `height` smallint(6) NOT NULL DEFAULT 0 COMMENT '高',
  `duration` smallint(6) NOT NULL DEFAULT 0 COMMENT '时长',
  `adduserid` bigint(20) NOT NULL COMMENT '上传用户ID',
  `addtime` datetime NOT NULL DEFAULT current_timestamp() COMMENT '上传时间',
  PRIMARY KEY (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='文件信息表'
 PARTITION BY LINEAR HASH (`fileid`)
PARTITIONS 64;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file_temporary`
--

DROP TABLE IF EXISTS `file_temporary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_temporary` (
  `fileid` bigint(20) NOT NULL COMMENT '文件ID',
  `addtime` datetime DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'usp_file'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-18 15:16:56
