package com.feinno.rocketmq.monitor.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * ClassName: ConnectionPoolAdapter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: Mar 5, 2015 4:12:51 PM <br/>
 *
 * @author honghao
 * @version 
 * @since JDK 1.7
 */
public abstract class ConnectionPoolAdapter {
	
	protected DataSource dataSource;
	protected DBConnectionPoolType poolType = DBConnectionPoolType.C3p0;
	
	ConnectionPoolAdapter(Properties configs,DBConnectionPoolType type) throws Exception
	{
		poolType = type;
	}
	
	public Connection getConnection() throws SQLException 
	{
		return dataSource.getConnection();
	}

}
