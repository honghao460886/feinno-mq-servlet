/**
 * Project Name:feinno-mq-servlet
 * File Name:MQTotal.java
 * Package Name:com.feinno.rocketmq.monitor.common
 * Date:Mar 9, 201510:29:20 AM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
 */

package com.feinno.rocketmq.monitor.data;

import java.util.Date;


/**
 * ClassName:MQTotal <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Mar 9, 2015 10:29:20 AM <br/>
 * 
 * @author honghao
 * @version
 * @since JDK 1.7
 * @see
 */
public class MQTotal {

    private Date yest = new Date();   // yyyy-mm-dd
    private Date today = new Date();   // yyyy-mm-dd
    private String clusterName = "";
    private String nameServerAddr = "";
    private int brokerId;
    private long inTotalYest = 0l;
    private long outTotalYest = 0l;
    private long inTotalToday = 0l;
    private long outTotalToday = 0l;
    
    public Date getYest() {
        return yest;
    }
    public void setYest(Date yest) {
        this.yest = yest;
    }
    public Date getToday() {
        return today;
    }
    public void setToday(Date today) {
        this.today = today;
    }
    public String getClusterName() {
        return clusterName;
    }
    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
    public String getNameServerAddr() {
        return nameServerAddr;
    }
    public void setNameServerAddr(String nameServerAddr) {
        this.nameServerAddr = nameServerAddr;
    }
    public int getBrokerId() {
        return brokerId;
    }
    public void setBrokerId(int brokerId) {
        this.brokerId = brokerId;
    }
    public long getInTotalYest() {
        return inTotalYest;
    }
    public void setInTotalYest(long inTotalYest) {
        this.inTotalYest = inTotalYest;
    }
    public long getOutTotalYest() {
        return outTotalYest;
    }
    public void setOutTotalYest(long outTotalYest) {
        this.outTotalYest = outTotalYest;
    }
    public long getInTotalToday() {
        return inTotalToday;
    }
    public void setInTotalToday(long inTotalToday) {
        this.inTotalToday = inTotalToday;
    }
    public long getOutTotalToday() {
        return outTotalToday;
    }
    public void setOutTotalToday(long outTotalToday) {
        this.outTotalToday = outTotalToday;
    }
    
}
