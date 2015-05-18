package org.aksw.openqa.component.service.impl;

import org.aksw.openqa.MaxSizeHashMap;

public class Cache {
	
	MaxSizeHashMap<Object, Object> objects;

	public Cache(int capacity) throws Exception {
		if(capacity < 1)
			throw new Exception("Invalid capacity value:" + capacity);
		this.objects = new MaxSizeHashMap<Object, Object>(capacity);
	}
	
	public synchronized void setCapacity(int capacity) {
		objects.setCapacity(capacity);
	}
	
	public synchronized Object get(Object key) {
		return objects.get(key);
	}

	public synchronized void put(Object key, Object value) {
		objects.put(key, value);
	}

}
