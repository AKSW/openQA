package org.aksw.openqa.component.object;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractObject implements IObject {
	
	private Map<String, Object> params = new HashMap<String, Object>();

	public boolean contains(String propertyId) {
		return params.containsKey(propertyId);
	}
	
	public boolean contains(String propertyId, Class<?> clazz) {
		return ((params.containsKey(propertyId)) && (getParam(propertyId, clazz) != null));
	}
	
	public Object getParam(String propertyId) {
		return params.get(propertyId);
	}
	
	public <T> T getParam(String propertyId, Class<T> clazz) {
		Object property = params.get(propertyId);
		if(property != null && clazz.isAssignableFrom(property.getClass())) {
			@SuppressWarnings("unchecked")
			T tProperty = (T) property;
			return tProperty;
		}
		return null;
	}
	
	public void setParam(String propertyId, Object o) {
		params.put(propertyId, o);
	}
	
	public void setProperties(Map<String, Object> entries) {
		if(entries != null) {
			for(Entry<String, Object> entry : entries.entrySet()) {
				params.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public Map<String, Object> getParameters() {
		return params;
	}
}
