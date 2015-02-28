/**
 * Project Name:feinno-mq-servlet
 * File Name:WTopicInfo.java
 * Package Name:com.feinno.rocketmq.monitor.topic.bean
 * Date:Feb 28, 201511:40:56 AM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
 */

package com.feinno.rocketmq.monitor.topic.bean;

/**
 * ClassName:WTopicInfo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Feb 28, 2015 11:40:56 AM <br/>
 * 
 * @author honghao
 * @version
 * @since JDK 1.7
 * @see
 */
public class WTopicInfo {
    private String clusterName = "";
    private String topicName = "";
    private String consumerGroup = "";


    public String getClusterName() {
        return clusterName;
    }


    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }


    public String getTopicName() {
        return topicName;
    }


    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }


    public String getConsumerGroup() {
        return consumerGroup;
    }


    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

}
