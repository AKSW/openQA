package org.aksw.openqa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;

import org.aksw.openqa.component.IPlugin;
import org.aksw.openqa.component.IProvider;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.object.Params;
import org.aksw.openqa.component.providers.impl.InterpreterProvider;
import org.aksw.openqa.component.providers.impl.RenderProvider;
import org.aksw.openqa.component.providers.impl.ResolverProvider;
import org.aksw.openqa.component.providers.impl.RetrieverProvider;
import org.aksw.openqa.component.providers.impl.ServiceProvider;
import org.aksw.openqa.component.providers.impl.SynthesizerProvider;
import org.aksw.openqa.component.ui.render.InfoGraphRender;
import org.aksw.openqa.component.ui.render.InfoNode;
import org.apache.log4j.Logger;

public class OpenQA {
	
	private static Logger logger = Logger.getLogger(OpenQA.class);
	
	public void process(Map<String, Object> processParams,	 
			Map<Class<? extends IProvider<? extends IPlugin>>, 
					IProvider<? extends IPlugin>> providers,
			IContext context,
			ServletOutputStream out) throws Exception {

		// render
		InfoNode node = new InfoNode();
		try {
			// increasing number of queries
			int numberOfQueries = Status.getNumberOfQueries() + 1;
			Status.setNumberOfQueries(numberOfQueries);
			
			ServiceProvider serviceProvider = (ServiceProvider) providers.get(ServiceProvider.class);
			Params token = new Params();
			
			logger.info("Parameterizing");
			
			// param set
			for(Entry<String, Object> entry : processParams.entrySet())
				token.setParam(entry.getKey(), entry.getValue());
			
			List<IParams> params = new ArrayList<IParams>();
			params.add(token);
			
			logger.debug("Interpreting");
			// Input Interpreter
			InterpreterProvider interpreterProvider = (InterpreterProvider) providers.get(InterpreterProvider.class);
			List<? extends IParams> interpretations = interpreterProvider.process(params, serviceProvider, context);
			logger.debug("Number of interpretations: " + interpretations.size());
			
			logger.debug("Retrieving ");
			// Processing
			RetrieverProvider retrieverProvider = (RetrieverProvider) providers.get(RetrieverProvider.class);
			List<? extends IParams> consultResults = retrieverProvider.process(interpretations, serviceProvider, context);	
			logger.debug("Number of consult results: " + consultResults.size());
			
			logger.debug("Synthesizing");
			// Synthesizing
			SynthesizerProvider synthesizerProvider = (SynthesizerProvider) providers.get(SynthesizerProvider.class);
			List<? extends IParams> syntheses = synthesizerProvider.process(consultResults, serviceProvider, context);
			logger.debug("Number of syntheses: " + syntheses.size());
			
			logger.debug("Resolving");
			// Resolving
			ResolverProvider resolverProvider = (ResolverProvider) providers.get(ResolverProvider.class);
			List<? extends IResult> resolved = resolverProvider.process(syntheses, serviceProvider, context);
			logger.debug("Number of resolved results: " + resolved.size());
			
			if(resolved.size() == 0) {
				// increasing number of queries without result
				int numberOfQueriesWithoutResult = Status.getQueriesWithourResult() + 1;
				Status.setQueriesWithourResult(numberOfQueriesWithoutResult);
			}
			logger.debug("Rendering result");
			for(IResult result : resolved) {
				InfoNode childNode = new InfoNode();
				childNode.setResult(result);
				node.addInfo(childNode);
			}
		} catch (Exception e) {
			int numberOfErrors = Status.getNumberOfErrors() + 1;
			Status.setNumberOfErrors(numberOfErrors);
			logger.error("An error occurred during the input process.", e);
		}
		
		// getting the main render
		RenderProvider renderProvider = (RenderProvider) providers.get(RenderProvider.class);
		InfoGraphRender mainRender = renderProvider.getRootRender();
		if(mainRender == null)
			throw new Exception("The interface should have at least one Main Render Active");
//		mainRender.render(renderProvider, node, runnerParams, out);
	}

}
