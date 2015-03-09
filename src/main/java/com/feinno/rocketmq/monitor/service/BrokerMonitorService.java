/**
 * Project Name:feinno-mq-servlet
 * File Name:BrokerService.java
 * Package Name:com.feinno.rocketmq.monitor.service
 * Date:Feb 28, 20155:31:01 PM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
 */

package com.feinno.rocketmq.monitor.service;

import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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

    @GET
    @Path("/status/{namesrvaddr}/{brokeraddr}/")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String getClusterList(@PathParam("namesrvaddr") String namesrvaddr,
            @PathParam("brokeraddr") String brokeraddr) throws Exception {
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
        try {
            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
            defaultMQAdminExt.start();
            TreeMap<String, String> map = new TreeMap<String, String>();
            WMQAdminExtHelper.queryBrokerStatus(map, defaultMQAdminExt, brokeraddr);
            return JSON.toJSONString(map);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            defaultMQAdminExt.shutdown();
        }
    }
}
