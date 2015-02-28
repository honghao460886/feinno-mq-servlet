/**
 * Project Name:feinno-mq-servlet
 * File Name:AbstractService.java
 * Package Name:com.feinno.rocketmq.service
 * Date:Feb 26, 20154:55:50 PM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
*/

package com.feinno.rocketmq.monitor.service;

import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;

/**
 * ClassName:AbstractService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     Feb 26, 2015 4:55:50 PM <br/>
 * @author   honghao
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public abstract class AbstractMonitorService {
    protected DefaultMQAdminExt getDefaultMQAdminExt() {
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();
        defaultMQAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
        return defaultMQAdminExt;
    }
}

