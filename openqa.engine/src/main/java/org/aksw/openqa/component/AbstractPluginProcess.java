package org.aksw.openqa.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.providers.impl.ServiceProvider;
import org.apache.log4j.Logger;

public abstract class AbstractPluginProcess extends AbstractPlugin implements IPluginProcess {
	
	private static Logger logger = Logger.getLogger(AbstractPluginProcess.class);
	
	public AbstractPluginProcess(Map<String, Object> params) {
		super(params);
	}
	
	public java.util.List<? extends IResult> process(java.util.List<? extends IParams> params, ServiceProvider serviceProvider, IContext context) throws Exception {
		List<IResult> totalResults = new ArrayList<IResult>();
		for(IParams param : params) {
			try {
				List<? extends IResult> partialResults = process(param, serviceProvider, context);
				if(partialResults != null)
					totalResults.addAll(partialResults);
			} catch (Exception e) {
				logger.error("Error processing token.", e);
			}
		}
		return totalResults;
	}
	
	public boolean canProcess(List<? extends IParams> params) {
		// verify if can process all the params,		
		for(IParams param : params) {
			// if there is any param in the list that can not be processed, return false
			if(!canProcess(param))
				return false;
		}
		return true;
	}
}
