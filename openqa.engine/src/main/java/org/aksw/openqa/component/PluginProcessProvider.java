package org.aksw.openqa.component;

import java.util.ArrayList;
import java.util.List;

import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.object.Result;
import org.aksw.openqa.component.providers.impl.ServiceProvider;
import org.apache.log4j.Logger;

public class PluginProcessProvider<C extends IPluginProcess, F extends IPluginFactorySpi<C>> extends AbstractPluginProvider<C, F> implements IProcess {
	
	private static Logger logger = Logger.getLogger(PluginProcessProvider.class);
	
	public PluginProcessProvider(Class<F> clazz, List<? extends ClassLoader> classLoaders, ServiceProvider serviceProvider) {
		super(clazz, classLoaders, serviceProvider);
	}
	
	public PluginProcessProvider() {
	}
	
	protected List<IResult> process(List<? extends IParams> arguments, List<C> components, ServiceProvider serviceProvider, IContext context) {
    	List<IResult> results = new ArrayList<IResult>();
    	boolean processed = false;
        for (C component : components) {
        	// if the component is activated and can process the argument
        	List<? extends IParams> canProcessArguments = getArguments(arguments, component);
			if(component.isActive() && canProcessArguments != null) {
				processed = true;
				try {
					results.addAll(component.process(canProcessArguments, serviceProvider, context)); // process the token
				} catch (Exception e) {
					logger.error("ProcessProvider exception", e);
				} catch (Error e) {
					logger.error("ProcessProvider error", e);
				}
			}
		}
    	// if is no processed, return the token themselves
    	if(!processed) {
    		for(IParams token : arguments) {
	    		Result result = new Result(token.getParameters(), token, this);
	    		results.add(result); // return
    		}
    	}
        return results;
    }
	
	public List<? extends IParams> getArguments(List<? extends IParams> arguments, C component) {
		List<IParams> componentArguments = null;
		for(IParams argument: arguments) {
			if(component.canProcess(argument)) {
				if(componentArguments == null)
					componentArguments = new ArrayList<IParams>();
				componentArguments.add(argument);
			}
		}
		return componentArguments;
	}
	
	public List<IResult> process(List<? extends IParams> arguments, ServiceProvider services, IContext context) throws Exception {
		return process(arguments, list(), services, context);
	}
}
