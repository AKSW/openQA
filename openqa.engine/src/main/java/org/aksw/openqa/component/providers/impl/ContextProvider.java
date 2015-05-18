package org.aksw.openqa.component.providers.impl;

import java.util.List;

import org.aksw.openqa.component.AbstractPluginProvider;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.context.IContextFactory;

public class ContextProvider extends AbstractPluginProvider<IContext, IContextFactory> {
    public ContextProvider(List<? extends ClassLoader> classLoaders, ServiceProvider serviceProvider) {
		super(IContextFactory.class, classLoaders, serviceProvider);
	}
    
    public ContextProvider() {    	
    }
}
