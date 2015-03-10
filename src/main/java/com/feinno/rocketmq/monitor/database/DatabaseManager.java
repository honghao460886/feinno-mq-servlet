/**
 * Project Name:feinno-mq-servlet
 * File Name:DataLandingOper.java
 * Package Name:com.feinno.rocketmq.monitor.common
 * Date:Mar 5, 201511:32:32 AM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
 */

package com.feinno.rocketmq.monitor.database;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


/**
 * ClassName:DataLandingOper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Mar 5, 2015 11:32:32 AM <br/>
 * 
 * @author honghao
 * @version
 * @since JDK 1.7
 * @see
 */
public class DatabaseManager {

    private static Database db = null;
    private static Properties prop = null;
    private static String dbName = "";

    static {
        prop = new Properties();
        InputStream in;
        try {
            dbName = "MQMonDB";
            in = new BufferedInputStream(new FileInputStream("MQMonDB.properties"));
            prop.load(in);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public static Database getDatabase() throws Exception {
        if (db == null) {
            db = new Database(dbName, prop);
        }
        return db;
    }
}
