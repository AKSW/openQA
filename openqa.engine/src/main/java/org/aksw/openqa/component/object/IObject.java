package org.aksw.openqa.component.object;

import java.util.Map;

public interface IObject {
	public Object getParam(String property);
	public <T> T getParam(String property, Class<T> clazz);
	public void setParam(String attr, Object value);
	
	public Map<String, Object> getParameters();
	public boolean contains(String property);
	public boolean contains(String property, Class<?> clazz);

}
