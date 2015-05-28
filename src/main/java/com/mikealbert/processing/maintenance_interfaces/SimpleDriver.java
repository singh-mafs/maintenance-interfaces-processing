package com.mikealbert.processing.maintenance_interfaces;

import org.apache.camel.spring.Main;
import org.springframework.beans.factory.BeanDefinitionStoreException;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;

public class SimpleDriver {
	
	private static MalLogger logger = MalLoggerFactory.getLogger(SimpleDriver.class);
	
	public static void main(String[] args){
		try {
			logger.info("starting Camel with SimpleDriver.\n");
			Main main = new Main();
			main.enableHangupSupport();
			main.run(args);
			logger.info("Camel has started. Use ctrl + c to terminate the JVM.\n");
		} catch (BeanDefinitionStoreException ex){
			logger.warning("WARN: META-INF likely not setup as Spring would like : " + ex.getMessage());
			logger.info("Camel has started. Use ctrl + c to terminate the JVM.\n");
		} catch (Exception e) {
			logger.error(e, "Error occured while running SimpleDriver main ");
		}
	}
}
