/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.110.234
 Source Server Type    : MySQL
 Source Server Version : 50137
 Source Host           : 192.168.110.234
 Source Database       : MQMonDB

 Target Server Type    : MySQL
 Target Server Version : 50137
 File Encoding         : utf-8

 Date: 03/05/2015 14:56:09 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `MQTotalByBroker`
-- ----------------------------
DROP TABLE IF EXISTS `MQTotalByBroker`;
CREATE TABLE `MQTotalByBroker` (
  `Date` varchar(8) NOT NULL COMMENT '日期，格式:20000101',
  `NameServerAddr` varchar(22) NOT NULL COMMENT '服务发现server地址',
  `ClusterName` varchar(32) NOT NULL COMMENT '集群名称',
  `BrokerID` int(2) NOT NULL COMMENT 'Broker集群ID',
  `InTotal` double NOT NULL DEFAULT '0' COMMENT '当日生产消息总数量',
  `OutTotal` double NOT NULL DEFAULT '0' COMMENT '当日生产消息总数量',
  PRIMARY KEY (`Date`,`ClusterName`,`BrokerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='一天生产消息、消费消息的总量'


SET FOREIGN_KEY_CHECKS = 1;
