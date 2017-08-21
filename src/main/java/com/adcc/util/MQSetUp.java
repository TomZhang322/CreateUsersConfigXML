package com.adcc.util;


import com.adcc.ags.utility.mq.configuration.MQConfigurationFactory;
import com.adcc.ags.utility.mq.transfer.MQConnectionPoolFactory;

public class MQSetUp {
    public static void setUp() throws Exception {
        String host = PropertyUtil.getProperty("config/createxml.properties","Host");
        String port = PropertyUtil.getProperty("config/createxml.properties","Port");
        String qm = PropertyUtil.getProperty("config/createxml.properties","QueueManager");
        String channel = PropertyUtil.getProperty("config/createxml.properties","Channel");
    	
        MQConfigurationFactory.getInstance().getConfiguration("AMQS").setHost(host);
        MQConfigurationFactory.getInstance().getConfiguration("AMQS").setPort(Integer.parseInt(port));
        MQConfigurationFactory.getInstance().getConfiguration("AMQS").setQueueManager(qm);
        MQConfigurationFactory.getInstance().getConfiguration("AMQS").setChannel(channel);
        MQConnectionPoolFactory.getInstance().getConnectionPool("AMQS").initConnectionManager();
    }
    
    public static void main(String[] args) {
    	try {
			setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
