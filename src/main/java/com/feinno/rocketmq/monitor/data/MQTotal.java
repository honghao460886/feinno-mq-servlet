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

    private Date date = new Date();   // yyyy-mm-dd
    private String clusterName = "";
    private String nameServerAddr = "";
    private int brokerId;
    private long inTotal = 0l;
    private long outTotal = 0l;


    public String getClusterName() {
        return clusterName;
    }


    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }


    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
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


    public long getInTotal() {
        return inTotal;
    }


    public void setInTotal(long inTotal) {
        this.inTotal = inTotal;
    }


    public long getOutTotal() {
        return outTotal;
    }


    public void setOutTotal(long outTotal) {
        this.outTotal = outTotal;
    }

}
