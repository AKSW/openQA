package org.aksw.openqa.component.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.aksw.openqa.component.service.AbstractService;

public class DefaultCacheService extends AbstractService {
	
	Map<String, Cache> caches = new HashMap<String, Cache>();
	public static final String MAX_CAPACITY = "MAX_CAPACITY";
	int capacity = 100;
	
	// DEFAULT PARAMS
	{
		setParam(MAX_CAPACITY, capacity);
	}
	
	public DefaultCacheService(Map<String, Object> params) {
		super(params);
	}
	
	@Override
	public void setParam(String param, Object o) {
		if(param.equals(MAX_CAPACITY)) {
			int capacity = Integer.parseInt(o.toString());
			for(Cache cache : caches.values()) {
				cache.setCapacity(capacity);
			}
		}
		super.setParam(param, o);
	}

	public Cache get(String cacheContext) throws Exception {
		Cache cache = caches.get(cacheContext);
		if(cache == null) {
			cache = new Cache(capacity);
			caches.put(cacheContext, cache);
		}	
		return cache;
	}

}
