package org.aksw.openqa.component.context;

import org.aksw.openqa.component.IPlugin;

public interface IContext extends IPlugin {

	public static final String REQUEST_HOST = "REQUEST_HOST";
	public static final String REQUEST_IP = "REQUEST_IP";
	public static final String REQUEST_USER = "REQUEST_USER";
	public static final String REQUEST = "REQUEST";
	
	String getInputQuery();	

}
