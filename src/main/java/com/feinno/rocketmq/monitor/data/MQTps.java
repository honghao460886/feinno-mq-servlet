/**
 * Project Name:feinno-mq-servlet
 * File Name:MQTps.java
 * Package Name:com.feinno.rocketmq.monitor.data
 * Date:Mar 9, 201511:49:23 AM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
 */

package com.feinno.rocketmq.monitor.data;

import java.util.Date;


/**
 * ClassName:MQTps <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Mar 9, 2015 11:49:23 AM <br/>
 * 
 * @author honghao
 * @version
 * @since JDK 1.7
 * @see
 */
public class MQTps {

    private Date date = new Date();
    private String clusterName = "";
    private String nameServerAddr = "";
    private int brokerId;
    private long inTps = 0l;
    private long outTps = 0l;


    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
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


    public long getInTps() {
        return inTps;
    }


    public void setInTps(long inTps) {
        this.inTps = inTps;
    }


    public long getOutTps() {
        return outTps;
    }


    public void setOutTps(long outTps) {
        this.outTps = outTps;
    }
}
