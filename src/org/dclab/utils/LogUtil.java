package org.dclab.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {

	public static final Logger LOG = LogManager.getLogger(LogUtil.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub


		LOG.debug("hello");	
		LOG.warn("tracing ...");
		LOG.info("Info...");
		LOG.error("ems", "echo: world");
		System.err.println("finish log");
	}

}
