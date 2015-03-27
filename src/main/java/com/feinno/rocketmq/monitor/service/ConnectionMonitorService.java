/**
 * Project Name:feinno-mq-servlet
 * File Name:ConnectionMonitorService.java
 * Package Name:com.feinno.rocketmq.monitor.service
 * Date:Mar 27, 20159:59:43 AM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
*/

package com.feinno.rocketmq.monitor.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.feinno.rocketmq.monitor.common.WMQAdminExtHelper;

/**
 * ClassName:ConnectionMonitorService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     Mar 27, 2015 9:59:43 AM <br/>
 * @author   honghao
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Path("/connection")
public class ConnectionMonitorService extends AbstractMonitorService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConnectionMonitorService.class);
    @GET
    @Path("/consumer/{namesrvaddr}/{group}/")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String consumerConnection(@PathParam("namesrvaddr") String namesrvaddr, @PathParam("group") String group) {
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
        try {
            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
            defaultMQAdminExt.start();
            List<Map<String, String>> list = WMQAdminExtHelper.examineConsumerConnectionInfo(defaultMQAdminExt, group);
            return JSON.toJSONString(list);
        } catch (Exception ex) {
            LOGGER.error("ConnectionMonitorService.consumerConnection error :{}", ex);
            return "";
        } finally {
            defaultMQAdminExt.shutdown();
        }
    }
    
//    @GET
//    @Path("/producer/{namesrvaddr}/{topic}/{group}/")
//    @Produces(value = MediaType.APPLICATION_JSON)
//    public String producerConnection(@PathParam("namesrvaddr") String namesrvaddr, @PathParam("topic") String topic, @PathParam("group") String group) {
//        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
//        try {
//            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
//            defaultMQAdminExt.start();
//            WMQAdminExtHelper.examineProducerConnectionInfo(defaultMQAdminExt, topic, group);
//            return JSON.toJSONString(list);
//        } catch (Exception ex) {
//            LOGGER.error("ConnectionMonitorService.producerConnection error :{}", ex);
//            return "";
//        } finally {
//            defaultMQAdminExt.shutdown();
//        }
//    }
}

