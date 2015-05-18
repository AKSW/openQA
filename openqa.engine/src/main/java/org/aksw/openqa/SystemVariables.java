package org.aksw.openqa;

import org.aksw.openqa.component.object.AbstractObject;
import org.aksw.openqa.manager.plugin.PluginManager;

public class SystemVariables extends AbstractObject {
	
	public final static String PLUGIN_MANAGER = "PLUGIN_MANAGER";
	
	private static SystemVariables systemVariables;
	
	public static SystemVariables getInstance() {
		if(systemVariables == null)
			systemVariables = new SystemVariables();
		return systemVariables;
	}
	
	public static PluginManager getPluginManager() {
		return ((PluginManager)(SystemVariables.getInstance().getParam(SystemVariables.PLUGIN_MANAGER)));
	}
}
