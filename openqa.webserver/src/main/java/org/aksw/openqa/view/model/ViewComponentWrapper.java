package org.aksw.openqa.view.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aksw.openqa.SystemVariables;
import org.aksw.openqa.component.IPlugin;
import org.aksw.openqa.view.StaticComponentStateManager;

public class ViewComponentWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7210207290942911038L;
	
	public final static String ACTIVE  = "ACTIVATED";
	public final static String DEACTIVE  = "DEACTIVATED";
	public final static String ACTIVATING  = "ACTIVATING...";
	public final static String DEACTIVATING  = "DEACTIVATING...";
	
	private List<Entry> params = new ArrayList<Entry>();
	
	private String author;
	private String description;
	private String contact;
	private String label;
	private String version;
	private String license;
	private String id;
	
	public ViewComponentWrapper(IPlugin component) {
		this.label = component.getLabel();
		this.version = component.getVersion();
		
		this.author = component.getAuthor();
		this.description = component.getDescription();
		this.contact = component.getContact();
		this.license = component.getLicense();
		this.id = component.getId();
		
		Map<String, Object> componentParams = component.getParameters();
		if(params != null) {
			for(java.util.Map.Entry<String, Object> entry : componentParams.entrySet())
				params.add(new Entry(entry));
		}
	}
	
	public void setActive(boolean isActive) {
		if(isActive)
			StaticComponentStateManager.getInstance().setState(getId(), ACTIVATING);
		else
			StaticComponentStateManager.getInstance().setState(getId(), DEACTIVATING);
		
		IPlugin component = SystemVariables.getPluginManager().getPlugin(getId());
		Thread thread = new Thread(new ComponentStateChangeRunnable(component, isActive));
		thread.start();		
	}
	
	public boolean isInTrasition() {		
		String status = StaticComponentStateManager.getInstance().getStatus(getId());
		if(status == null)
			return false;
		return status.equals(ACTIVATING) || status.equals(DEACTIVATING);
	}

	public String getAuthor() {
		return author;
	}
	
	public String getContact() {
		return contact;
	}

	public String getDescription() {
		return description;
	}
	
	public List<Entry> getEntries() {
		return params;
	}
	
	public String getId() {
		return id;
	}
	
	public String getStatus() {
		String status = StaticComponentStateManager.getInstance().getStatus(getId());
		if(status != null)
			return status;
		if(isActive())
			return ACTIVE;
		return DEACTIVE;
	}
	
	public void save() {
		HashMap<String, Object> localParams = new HashMap<String, Object>();
		for(Entry param : params) {
			localParams.put(param.getKey(), param.getValue());
		}
		IPlugin component = SystemVariables.getPluginManager().getPlugin(getId());
		component.setProperties(localParams);
	}
	
	public class Entry implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String key;
		String value;
		
		public Entry(java.util.Map.Entry<String, Object> entry) {
			setKey(entry.getKey());
			Object value = entry.getValue();
			if(value!=null)
				setValue(value.toString());
		}

		public String getKey() {
			return key;
		}
		
		public void setKey(String key) {
			this.key = key;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value.toString();
		}
	}
	
	private class ComponentStateChangeRunnable implements Runnable {

		IPlugin targetComponent;
		boolean activatingStatus;

		public ComponentStateChangeRunnable(IPlugin targetComponent, boolean activatingStatus) {
			this.targetComponent = targetComponent;
			this.activatingStatus = activatingStatus;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			targetComponent.setActive(activatingStatus);
			String componentId = targetComponent.getId();
			if(activatingStatus) {
				StaticComponentStateManager.getInstance().setState(componentId, ACTIVE);
			} else {
				StaticComponentStateManager.getInstance().setState(componentId, DEACTIVE);
			}
		}
	}

	public String getVersion() {
		return version;
	}

	public String getLabel() {
		return label;
	}
	
	public String getLicense() {
		return license;
	}
	
	public boolean isActive() {
		IPlugin component = SystemVariables.getPluginManager().getPlugin(getId());
		return component.isActive();
	}
}
