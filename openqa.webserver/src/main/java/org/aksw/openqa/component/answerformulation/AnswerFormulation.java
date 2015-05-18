package org.aksw.openqa.component.answerformulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.object.Params;
import org.aksw.openqa.component.providers.impl.InterpreterProvider;
import org.aksw.openqa.component.providers.impl.ResolverProvider;
import org.aksw.openqa.component.providers.impl.RetrieverProvider;
import org.aksw.openqa.component.providers.impl.ServiceProvider;
import org.aksw.openqa.component.providers.impl.SynthesizerProvider;
import org.aksw.openqa.manager.plugin.PluginManager;
import org.apache.log4j.Logger;

public class AnswerFormulation {
	
	private static Logger logger = Logger.getLogger(AnswerFormulation.class);

	public List<? extends IResult> process(boolean skipRetriever, Map<String, Object> params,
			PluginManager pluginManager,
			IContext context) throws Exception {

		ServiceProvider serviceProvider = (ServiceProvider) pluginManager.getProvider(ServiceProvider.class);
		Params token = new Params();
		
		logger.info("Parameterizing");
		
		// param set
		for(Entry<String, Object> entry : params.entrySet())
			token.setParam(entry.getKey(), entry.getValue());
		
		logger.debug("Interpreting");
		InterpreterProvider interpreterProvider = (InterpreterProvider) pluginManager.getProvider(InterpreterProvider.class);
		List<IParams> arguments = new ArrayList<IParams>();
		arguments.add(token);
		List<? extends IResult> interpretations = interpreterProvider.process(arguments, serviceProvider, context);
		logger.debug("Number of interpretations: " + interpretations.size());

		if(skipRetriever)
			return interpretations;
		
		logger.debug("Retrieving ");
		RetrieverProvider retrieverProvider = (RetrieverProvider) pluginManager.getProvider(RetrieverProvider.class);
		List<? extends IResult> retrieverResults = retrieverProvider.process(interpretations, serviceProvider, context);	
		logger.debug("Number of retrieved results: " + retrieverResults.size());
		
		logger.debug("Synthesizing");
		SynthesizerProvider synthesizerProvider = (SynthesizerProvider) pluginManager.getProvider(SynthesizerProvider.class);
		List<? extends IResult> synthesis = synthesizerProvider.process(retrieverResults, serviceProvider, context);
		logger.debug("Number of synthesis: " + synthesis.size());
		
		logger.debug("Resolving");
		ResolverProvider resolverProvider = (ResolverProvider) pluginManager.getProvider(ResolverProvider.class);
		List<? extends IResult> resolved = resolverProvider.process(synthesis, serviceProvider, context);
		logger.debug("Number of resolved results: " + resolved.size());
			
		return resolved;
	}
}
