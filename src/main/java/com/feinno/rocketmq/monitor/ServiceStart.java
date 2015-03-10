/**
 * Project Name:feinno-mq-servlet
 * File Name:ServiceStart.java
 * Package Name:com.feinno.rocketmq.service
 * Date:Feb 26, 20155:18:09 PM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
 */

package com.feinno.rocketmq.monitor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

import com.feinno.rocketmq.monitor.data.DataCollectionService;
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
    
    private static Logger LOGGER; 
    public static void main(String[] args) throws Exception {
        buildCommandlineOptions(args);
    }

    private static void buildCommandlineOptions(String[] args) throws ParseException, Exception {
        CommandLine commandLine = null;
        Options options = new Options();
        Option opt = new Option("p", "port", true, "jetty port");
        options.addOption(opt);
        commandLine = new PosixParser().parse(options, args);
        if (commandLine.hasOption('p')) {
            int port = Integer.parseInt(commandLine.getOptionValue('p'));
            System.out.println("initialize logback......");
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            configurator.doConfigure("logback.xml");
            LOGGER = LoggerFactory.getLogger(ServiceStart.class);
            LOGGER.error("initialize logback finished......");
            // jetty server start
            startJettyServer(port);
            LOGGER.error("startJettyServer start finished, http port is : {}", port);
            // data collection service start
            DataCollectionService.start();
            LOGGER.error("DataCollectionService start finished.");
        }
        else {
            System.err.println("please input jetty port");
            System.exit(-1);
        }
    }


    private static void startJettyServer(int port) throws Exception {
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
