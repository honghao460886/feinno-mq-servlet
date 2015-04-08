/**
 * Project Name:feinno-mq-servlet
 * File Name:BrokerService.java
 * Package Name:com.feinno.rocketmq.monitor.service
 * Date:Feb 28, 20155:31:01 PM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
 */

package com.feinno.rocketmq.monitor.service;

import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.feinno.rocketmq.monitor.common.WMQAdminExtHelper;


/**
 * ClassName:BrokerService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Feb 28, 2015 5:31:01 PM <br/>
 * 
 * @author honghao
 * @version
 * @since JDK 1.7
 * @see
 */
@Path("/broker")
public class BrokerMonitorService extends AbstractMonitorService {

    private final static Logger LOGGER = LoggerFactory.getLogger(BrokerMonitorService.class);
    @Context
    HttpServletRequest request;
    @GET
    @Path("/status/{namesrvaddr}/{brokeraddr}/")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String getClusterList(@PathParam("namesrvaddr") String namesrvaddr,
            @PathParam("brokeraddr") String brokeraddr) {
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
        try {
            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
            defaultMQAdminExt.start();
            TreeMap<String, String> map = new TreeMap<String, String>();
            WMQAdminExtHelper.queryBrokerStatus(map, defaultMQAdminExt, brokeraddr);
            return JSON.toJSONString(map);
        }
        catch (Exception ex) {
            LOGGER.error("BrokerMonitorService.getClusterList error :{}", ex);
            return "";
        }
        finally {
            defaultMQAdminExt.shutdown();
        }
    }
    
    @GET
    @Path("/config/update/{namesrvaddr}/{clustername}/{key}/{value}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String updateBrokerConfig(@PathParam("namesrvaddr") String namesrvaddr, @PathParam("clustername") String clustername, @PathParam("key") String key, @PathParam("value") String value) {
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
        try {
            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
            defaultMQAdminExt.start();
            WMQAdminExtHelper.updateBrokerConfig(defaultMQAdminExt, clustername, key, value);
            return "sucess";
        }
        catch (Exception ex) {
            LOGGER.error("BrokerMonitorService.updateBrokerConfig error :{}", ex);
            return "";
        }
        finally {
            defaultMQAdminExt.shutdown();
        }
    }
    
    @GET
    @Path("/config/update/{namesrvaddr}/{brokeraddr}/{key}/{value}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String updateBrokerConfigByBrokerAddr(@PathParam("namesrvaddr") String namesrvaddr, @PathParam("brokeraddr") String brokeraddr, @PathParam("key") String key, @PathParam("value") String value) {
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
        try {
            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
            defaultMQAdminExt.start();
            Properties properties = new Properties();
            properties.put(key, value);
            defaultMQAdminExt.updateBrokerConfig(brokeraddr, properties);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("update broker config success, %s", brokeraddr));
            }
            return "sucess";
        }
        catch (Exception ex) {
            LOGGER.error("BrokerMonitorService.updateBrokerConfigByBrokerAddr error :{}", ex);
            return "";
        }
        finally {
            defaultMQAdminExt.shutdown();
        }
    }
}
