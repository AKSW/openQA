package org.aksw.openqa.component.providers.impl;

import java.util.List;

import org.aksw.openqa.component.PluginProcessProvider;
import org.aksw.openqa.component.answerformulation.IRetriever;
import org.aksw.openqa.component.answerformulation.IRetrieverFactory;

public class RetrieverProvider extends PluginProcessProvider<IRetriever, IRetrieverFactory> {
	public RetrieverProvider(List<? extends ClassLoader> classLoaders, ServiceProvider serviceProvider) {
		super(IRetrieverFactory.class, classLoaders, serviceProvider);
	}
	
	public RetrieverProvider() {	
	}
}
