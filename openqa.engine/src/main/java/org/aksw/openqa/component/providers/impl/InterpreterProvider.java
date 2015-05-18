package org.aksw.openqa.component.providers.impl;
import java.util.List;

import org.aksw.openqa.component.PluginProcessProvider;
import org.aksw.openqa.component.answerformulation.Interpreter;
import org.aksw.openqa.component.answerformulation.IInterpreterFactory;
 
public class InterpreterProvider extends PluginProcessProvider<Interpreter, IInterpreterFactory> {
    public InterpreterProvider(List<? extends ClassLoader> classLoaders, ServiceProvider serviceProvider) {
		super(IInterpreterFactory.class, classLoaders, serviceProvider);
	}
    
    public InterpreterProvider() {    	
    }
}