package org.aksw.openqa.component.providers.impl;

import java.util.List;

import org.aksw.openqa.component.PluginProcessProvider;
import org.aksw.openqa.component.answerformulation.IResolver;
import org.aksw.openqa.component.answerformulation.IResolverFactory;

public class ResolverProvider extends PluginProcessProvider<IResolver, IResolverFactory> {
	public ResolverProvider(List<? extends ClassLoader> classLoaders, ServiceProvider serviceProvider) {
		super(IResolverFactory.class, classLoaders, serviceProvider);
	}
	
	public ResolverProvider() {		
	}	
}
