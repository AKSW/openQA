package org.aksw.openqa.component.providers.impl;

import java.util.List;

import org.aksw.openqa.component.PluginProcessProvider;
import org.aksw.openqa.component.answerformulation.ISynthesizer;
import org.aksw.openqa.component.answerformulation.ISynthesizerFactory;

public class SynthesizerProvider extends PluginProcessProvider<ISynthesizer, ISynthesizerFactory> {
	public SynthesizerProvider(List<? extends ClassLoader> classLoaders, ServiceProvider serviceProvider) {
		super(ISynthesizerFactory.class, classLoaders, serviceProvider);
	}
	
	public SynthesizerProvider() {		
	}
}
