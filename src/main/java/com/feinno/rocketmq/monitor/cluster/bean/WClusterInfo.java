/**
 * Project Name:feinno-mq-servlet
 * File Name:ClusterInfo.java
 * Package Name:com.feinno.rocketmq.cluster.bean
 * Date:Feb 27, 20154:38:25 PM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
 */

package com.feinno.rocketmq.monitor.cluster.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName:ClusterInfo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Feb 27, 2015 4:38:25 PM <br/>
 * 
 * @author honghao
 * @version
 * @since JDK 1.7
 * @see
 */
public class WClusterInfo {
    
    private String clusterName = "";
    private String brokerName = "";
    private String brokerId = "";
    private String brokerAddr = "";
    private String brokerVersion = "";
    private String inTps = "";
    private String outTps = "";
    private String inTotalYest = "";
    private String outTotalYest = "";
    private String inTotalToday = "";
    private String outTotalToday = "";


    public String getClusterName() {
        return clusterName;
    }


    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }


    public String getBrokerName() {
        return brokerName;
    }


    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }


    public String getBrokerId() {
        return brokerId;
    }


    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }


    public String getBrokerAddr() {
        return brokerAddr;
    }


    public void setBrokerAddr(String brokerAddr) {
        this.brokerAddr = brokerAddr;
    }


    public String getBrokerVersion() {
        return brokerVersion;
    }


    public void setBrokerVersion(String brokerVersion) {
        this.brokerVersion = brokerVersion;
    }


    public String getInTps() {
        return inTps;
    }


    public void setInTps(String inTps) {
        this.inTps = inTps;
    }


    public String getOutTps() {
        return outTps;
    }


    public void setOutTps(String outTps) {
        this.outTps = outTps;
    }


    public String getInTotalYest() {
        return inTotalYest;
    }


    public void setInTotalYest(String inTotalYest) {
        this.inTotalYest = inTotalYest;
    }


    public String getOutTotalYest() {
        return outTotalYest;
    }


    public void setOutTotalYest(String outTotalYest) {
        this.outTotalYest = outTotalYest;
    }


    public String getInTotalToday() {
        return inTotalToday;
    }


    public void setInTotalToday(String inTotalToday) {
        this.inTotalToday = inTotalToday;
    }


    public String getOutTotalToday() {
        return outTotalToday;
    }


    public void setOutTotalToday(String outTotalToday) {
        this.outTotalToday = outTotalToday;
    }

}
