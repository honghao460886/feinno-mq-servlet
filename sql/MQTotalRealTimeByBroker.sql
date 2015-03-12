/*
 Navicat Premium Data Transfer

 Source Server         : my_mysql
 Source Server Type    : MySQL
 Source Server Version : 50623
 Source Host           : 10.10.40.81
 Source Database       : MQMonDB

 Target Server Type    : MySQL
 Target Server Version : 50623
 File Encoding         : utf-8

 Date: 03/11/2015 14:19:46 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `MQTotalByBroker`
-- ----------------------------
DROP TABLE IF EXISTS `MQTotalRealTimeByBroker`;
CREATE TABLE `MQTotalRealTimeByBroker` (
  `Date` datetime NOT NULL COMMENT '日期，格式:20000101',
  `NameServerAddr` varchar(22) NOT NULL COMMENT '服务发现server地址',
  `ClusterName` varchar(32) NOT NULL COMMENT '集群名称',
  `BrokerID` int(2) NOT NULL COMMENT 'Broker集群ID',
  `InTotal` bigint(20) NOT NULL DEFAULT '0' COMMENT '当日生产消息总数量',
  `OutTotal` bigint(20) NOT NULL DEFAULT '0' COMMENT '当日生产消息总数量',
  PRIMARY KEY (`Date`,`ClusterName`,`BrokerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='一天生产消息、消费消息的总量';

SET FOREIGN_KEY_CHECKS = 1;
