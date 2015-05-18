package org.aksw.openqa.view;

import java.util.HashMap;
import java.util.Map;

public class StaticComponentStateManager {
	
	private static StaticComponentStateManager instance = null;
	private Map<String, String> components = new HashMap<String, String>();
	
	public static StaticComponentStateManager getInstance() {
		if(instance == null)
			instance = new StaticComponentStateManager();
		return instance;
	}
	
	public String getStatus(String componentId) {
		return components.get(componentId);
	}
	
	public void setState(String componentId, String state) {
		components.put(componentId, state);
	}
}
