package org.aksw.openqa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyLoaderUtil {
	
	private static Logger logger = Logger.getLogger(PropertyLoaderUtil.class);
	
	public static Map<String, Object> getProperties(Class<?> clazz, String filePath) {
		Map<String, Object> params = null;
		InputStream inStream;
		try {
			inStream = clazz.getResourceAsStream(filePath); // looking for component params file directory
			if(inStream != null)
				params = loadParams(inStream); // loading properties
		} catch (Exception e) {
			logger.error("Error initializing component params:" + clazz.getName(), e);
		}
		return params;
	}
	
	public static Map<String, Object> getPropertiesFromFile(String filePath) {
		Map<String, Object> params = null;
		InputStream inStream;
		try {
			File f = new File(filePath);
			if(f.exists()) {
				inStream = new FileInputStream(filePath); // looking for component params file directory
				if(inStream != null)
					params = loadParams(inStream); // loading properties
			}
		} catch (Exception e) {
			logger.error("Error loading params:" + filePath, e);
		}
		return params;
	}

	protected static Map<String, Object> loadParams(InputStream is) throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
 		Properties prop = new Properties();
		if(is != null) {
			prop.load(is);
			for(Object key : prop.keySet()) {
				params.put((String) key, prop.get(key));
			}				
		}
		return params;
	}
}
