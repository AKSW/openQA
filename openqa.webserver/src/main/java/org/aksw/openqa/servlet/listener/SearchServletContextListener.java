package org.aksw.openqa.servlet.listener;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.aksw.openqa.SystemVariables;
import org.aksw.openqa.manager.log.ConsoleAppender;
import org.aksw.openqa.manager.log.ConsoleLog;
import org.aksw.openqa.manager.plugin.PluginManager;
import org.apache.log4j.Logger;

public class SearchServletContextListener implements javax.servlet.ServletContextListener {
 
	private static Logger logger = Logger.getLogger(SearchServletContextListener.class);
	
    public void contextInitialized(ServletContextEvent event) {
    	logger.info("openQA is being initialized");
    	
    	ConsoleAppender appender = new ConsoleAppender();
    	Logger.getRootLogger().addAppender(appender);
    	
		ServletContext context = event.getServletContext();
		String contextPath = context.getRealPath(File.separator);
		ClassLoader contextClassLoader = context.getClassLoader();
    	PluginManager pluginManager = new PluginManager(contextPath + "/plugins", contextClassLoader);
    	pluginManager.setActive(true);
    	SystemVariables.getInstance().setParam(SystemVariables.PLUGIN_MANAGER, pluginManager);
    	
        logger.info("openQA is initialized");
    }
 
    public void contextDestroyed(ServletContextEvent event) {
    	logger.info("openQA is being shutting down");
    	PluginManager pluginManager = (PluginManager) SystemVariables.getInstance().getParam(SystemVariables.PLUGIN_MANAGER);
    	pluginManager.setActive(false);
    	ConsoleLog.getInstance().shutdown();
    }
}