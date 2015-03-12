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

 Date: 03/11/2015 14:19:52 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `MQTpsByBroker`
-- ----------------------------
DROP TABLE IF EXISTS `MQTpsByBroker`;
CREATE TABLE `MQTpsByBroker` (
  `Date` datetime NOT NULL,
  `NameServerAddr` varchar(22) NOT NULL COMMENT '服务发现server地址',
  `ClusterName` varchar(32) NOT NULL COMMENT '集群名称',
  `BrokerID` int(2) NOT NULL COMMENT 'Broker集群ID',
  `InTPS` int(11) NOT NULL DEFAULT '0' COMMENT '单台Broker下生产并发量TPS',
  `OutTPS` int(11) NOT NULL DEFAULT '0' COMMENT '单台Broker下消费并发量TPS',
  PRIMARY KEY (`Date`,`BrokerID`,`ClusterName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
