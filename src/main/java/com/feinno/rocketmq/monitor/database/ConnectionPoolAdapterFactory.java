package com.feinno.rocketmq.monitor.database;

import java.util.Properties;

public class ConnectionPoolAdapterFactory {
		
	private static ConnectionPoolAdapterFactory instance;

	private ConnectionPoolAdapterFactory()
	{
		
	}
	
	public synchronized static ConnectionPoolAdapterFactory getInstance()
	{
		if(instance == null)
			instance = new ConnectionPoolAdapterFactory();
		
		return instance;
	}
	
	public ConnectionPoolAdapter getConnectionPoolAdapter(Properties config,DBConnectionPoolType poolType) throws Exception
	{
		ConnectionPoolAdapter adapter = null;
		switch(poolType)
		{
			case  C3p0:
				adapter = new C3p0PoolAdapter(config,poolType);
				break;
			default:
				throw new IllegalArgumentException("unsupported DBConnectionPoolType:"+poolType);
		}
		
		return adapter;
	}
}
