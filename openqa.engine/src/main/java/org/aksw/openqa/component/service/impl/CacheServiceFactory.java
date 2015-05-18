package org.aksw.openqa.component.service.impl;

import java.util.Map;

import org.aksw.openqa.component.service.AbstractServiceFactory;
import org.aksw.openqa.component.service.IService;

public class CacheServiceFactory extends AbstractServiceFactory {
	@Override
	public IService create(Map<String, Object> params) {
		return create(DefaultCacheService.class, params);
	}
}
