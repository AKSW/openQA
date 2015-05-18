package org.aksw.openqa.component;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.apache.log4j.Logger;

public abstract class AbstractPluginFactory<C extends IPlugin> implements IPluginFactorySpi<C>{

	private static Logger logger = Logger.getLogger(AbstractPluginProcess.class);
	
	protected C create(Class<? extends C> clazz, Map<String, Object> params) {
		C instance = null;
		try {
			Constructor<? extends C> constructor = clazz.getConstructor(Map.class);
			instance = constructor.newInstance(params);
		} catch (Exception e) {
			logger.error("Error initializing ComponentFactory: " + clazz.getCanonicalName(), e);
		}
		return instance;
	}
	
	protected C create(Class<? extends C> clazz) {
		C instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			logger.error("Error initializing ComponentFactory: " + clazz.getCanonicalName(), e);
		}
		return instance;
	}
}
