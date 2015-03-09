package com.feinno.rocketmq.monitor.database;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * ClassName: C3p0PoolAdapter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: Mar 5, 2015 4:12:42 PM <br/>
 *
 * @author honghao
 * @version 
 * @since JDK 1.7
 */
public class C3p0PoolAdapter extends ConnectionPoolAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(C3p0PoolAdapter.class);


    C3p0PoolAdapter(Properties configs, DBConnectionPoolType type) throws Exception {
        super(configs, type);
        /*
         * 在外界没有配置的情况下，预设一些C3P0的参数
         */
        if (!configs.containsKey("acquireIncrement"))
            configs.setProperty("acquireIncrement", "5");
        if (!configs.containsKey("acquireRetryDelay"))
            configs.setProperty("acquireRetryDelay", "500");
        if (!configs.containsKey("checkoutTimeout"))
            configs.setProperty("checkoutTimeout", "10000");
        if (!configs.containsKey("idleConnectionTestPeriod"))
            configs.setProperty("idleConnectionTestPeriod", "60");
        if (!configs.containsKey("initialPoolSize"))
            configs.setProperty("initialPoolSize", "10");
        if (!configs.containsKey("minPoolSize"))
            configs.setProperty("minPoolSize", "10");
        if (!configs.containsKey("maxPoolSize"))
            configs.setProperty("maxPoolSize", "10");
        if (!configs.containsKey("maxIdleTime"))
            configs.setProperty("maxIdleTime", "30");
        // if(!configs.containsKey("automaticTestTable"))
        // configs.setProperty("automaticTestTable", "c3p0Test");

        dataSource = new ComboPooledDataSource();
        // 通过反射动态设定数据源属性
        Class clazz = ComboPooledDataSource.class;
        Method[] methods = null;
        String key = null;
        String val = null;
        String methodName = null;
        Enumeration eunm = configs.propertyNames();
        while (eunm.hasMoreElements()) {
            key = (String) eunm.nextElement();
            val = configs.getProperty(key);
            methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
            methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    try {
                        if (method.getParameterTypes()[0].getName() == "int") {
                            method.invoke(dataSource, Integer.parseInt(val));
                        }
                        else if (method.getParameterTypes()[0].getName() == "long") {
                            method.invoke(dataSource, Long.parseLong(val));
                        }
                        else if (method.getParameterTypes()[0].getName() == "float") {
                            method.invoke(dataSource, Float.parseFloat(val));
                        }
                        else if (method.getParameterTypes()[0].getName() == "double") {
                            method.invoke(dataSource, Double.parseDouble(val));
                        }
                        else if (method.getParameterTypes()[0].getName() == "java.lang.String") {
                            method.invoke(dataSource, val);
                        }
                        else {
                            method.invoke(dataSource, val);
                        }
                    }
                    catch (Exception e) {
                        LOGGER.error("init database error{}", e);
                        throw e;
                    }
                    break;
                }
            }
        }
    }
}
