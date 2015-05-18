package org.aksw.openqa.component.providers.impl;

import java.util.List;

import org.aksw.openqa.component.AbstractPluginProvider;
import org.aksw.openqa.component.service.IService;
import org.aksw.openqa.component.service.IServiceFactory;

public class ServiceProvider extends AbstractPluginProvider<IService, IServiceFactory> {
	public ServiceProvider(List<? extends ClassLoader> classLoaders) {
		super(IServiceFactory.class, classLoaders, null);
	}
	
	public ServiceProvider() {
	}
}
