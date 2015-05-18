package org.aksw.openqa.component;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.aksw.openqa.component.object.AbstractObject;
import org.aksw.openqa.util.PropertyLoaderUtil;
import org.apache.log4j.Logger;

public abstract class AbstractPlugin extends AbstractObject implements IPlugin {
	
	private static Logger logger = Logger.getLogger(AbstractPlugin.class);
	
	private static final String DEFAULT_VALUE = "undefined";
	private static final String DEFAULT_META_PATH = "/META-INF/";
	private static final String DEFAULT_INFO_SUFFIX = ".inf";
	private static final String DEFAULT_INI_SUFFIX = ".ini";
	
	private static final String AUTHOR_PROPERTY = "author";
	private static final String VERSION_PROPERTY = "version";
	private static final String LICENSE_PROPERTY = "license";
	private static final String LABEL_PROPERTY = "label";
	private static final String CONTACT_PROPERTY = "contact";
	private static final String WEBSITE_PROPERTY = "website";
	private static final String INPUT_PROPERTY = "input";
	private static final String OUTPUT_PROPERTY = "output";
	private static final String DESCRIPTION_PROPERTY = "description";
	private static final String API_PROPERTY = "API";
	
	private String author = DEFAULT_VALUE;
	private String description = DEFAULT_VALUE;
	private String contact = DEFAULT_VALUE;
	private String license = DEFAULT_VALUE;
	private String version = DEFAULT_VALUE;
	private String label = DEFAULT_VALUE;
	private String website = DEFAULT_VALUE;
	private String api = DEFAULT_VALUE;
	private String output = DEFAULT_VALUE;
	private String input = DEFAULT_VALUE;
	
	boolean isActive;
	
	public AbstractPlugin() {		
	}
	
	public AbstractPlugin(Map<String, Object> params) {
		init(this.getClass(), params);
	}
	
	protected AbstractPlugin(Class<? extends IPlugin> clazz, Map<String, Object> params) {
		init(clazz, params);
	}
	
	private void init(Class<? extends IPlugin> clazz, Map<String, Object> params) {
		Properties prop = new Properties();
		InputStream inStream = clazz.getResourceAsStream(DEFAULT_META_PATH + clazz.getName() + DEFAULT_INFO_SUFFIX); // loading component info file
		try {
			if(inStream != null) {
				prop.load(inStream);
				author = prop.getProperty(AUTHOR_PROPERTY, DEFAULT_VALUE);
				version = prop.getProperty(VERSION_PROPERTY, DEFAULT_VALUE);
				license = prop.getProperty(LICENSE_PROPERTY, DEFAULT_VALUE);
				label = prop.getProperty(LABEL_PROPERTY, DEFAULT_VALUE);
				contact = prop.getProperty(CONTACT_PROPERTY, DEFAULT_VALUE);
				description = prop.getProperty(DESCRIPTION_PROPERTY, DEFAULT_VALUE);
				website = prop.getProperty(WEBSITE_PROPERTY, DEFAULT_VALUE);
				api = prop.getProperty(API_PROPERTY, DEFAULT_VALUE);
				input = prop.getProperty(INPUT_PROPERTY, DEFAULT_VALUE);
				output = prop.getProperty(OUTPUT_PROPERTY, DEFAULT_VALUE);				
			}
		} catch (Exception e) {
			logger.error("Error initializing component description properties:" + clazz.getName(), e);
		}
		
		Map<String, Object> properties = PropertyLoaderUtil.getProperties(clazz, DEFAULT_META_PATH + clazz.getName() + DEFAULT_INI_SUFFIX);
		if(properties != null)
			setProperties(properties); // setting the default property params (ini file) on jar file
		
		String className = clazz.getName();
		String classPath = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
		classPath = classPath.substring(0, classPath.lastIndexOf("/"));
		// try to load properties from plug-in local directory			
		properties = PropertyLoaderUtil.getPropertiesFromFile(classPath + "/" + className + DEFAULT_INI_SUFFIX);		
		if(properties != null)
			setProperties(properties); // overriding settings by user class path params (ini file)
		
		if(params != null)
			setProperties(params); // overriding settings by constructor params
	}
	
	public String getVersion() {
		return version;
	}

	public String getLabel() {
		return label;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}	

	public String getAuthor() {
		return author;
	}
	
	public String getWebsite() {
		return website;
	}
	
	public String getAPI() {
		return api;
	}
	
	public String getInput() {
		return input;
	}
	
	public String getOutput() {
		return output;
	}

	public String getContact() {
		return contact;
	}

	public String getDescription() {
		return description;
	}

	public String getLicense() {
		return license;
	}
	
	public String getId() {
		return getLabel() + " " + getVersion();
	}
		
}
