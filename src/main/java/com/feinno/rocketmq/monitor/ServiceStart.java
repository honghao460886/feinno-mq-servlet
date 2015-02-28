/**
 * Project Name:feinno-mq-servlet
 * File Name:ServiceStart.java
 * Package Name:com.feinno.rocketmq.service
 * Date:Feb 26, 20155:18:09 PM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
 */

package com.feinno.rocketmq.monitor;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.sun.jersey.spi.container.servlet.ServletContainer;


/**
 * ClassName:ServiceStart <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Feb 26, 2015 5:18:09 PM <br/>
 * 
 * @author honghao
 * @version
 * @since JDK 1.7
 * @see
 */
public class ServiceStart {
    public static void main(String[] args) throws Exception {
        int port = 8081;
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        ServletHolder sh = new ServletHolder(ServletContainer.class);
        sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass",
            "com.sun.jersey.api.core.PackagesResourceConfig");
        sh.setInitParameter("com.sun.jersey.config.property.packages", "com.feinno.rocketmq.monitor.service");
        context.addServlet(sh, "/*");
        server.start();
    }
    
    
}
