/**
 * Project Name:feinno-mq-servlet
 * File Name:MQTotalJson.java
 * Package Name:com.feinno.rocketmq.monitor.data
 * Date:Mar 12, 20155:32:27 PM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
*/

package com.feinno.rocketmq.monitor.data;

import java.util.Date;

/**
 * ClassName:MQTotalJson <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     Mar 12, 2015 5:32:27 PM <br/>
 * @author   honghao
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class MQTotalRet {

    private Date date = new Date();   // yyyy-mm-dd
    private String clusterName = "";
    private String nameServerAddr = "";
    private int brokerId;
    private long inTotal = 0l;
    private long outTotal = 0l;
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

