package org.aksw.openqa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.aksw.openqa.component.IComponent;
import org.aksw.openqa.component.IProcess;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.object.Params;
import org.aksw.openqa.component.providers.impl.ContextProvider;
import org.aksw.openqa.component.providers.impl.InterpreterProvider;
import org.aksw.openqa.component.providers.impl.RetrieverProvider;
import org.aksw.openqa.component.providers.impl.ServiceProvider;
import org.aksw.openqa.component.providers.impl.SynthesizerProvider;
import org.aksw.openqa.main.ProcessResult;
import org.aksw.openqa.main.QueryResult;
import org.aksw.openqa.manager.plugin.PluginManager;
import org.apache.log4j.Logger;

public class QAEngineProcessor implements IComponent {
	
	private static Logger logger = Logger.getLogger(QAEngineProcessor.class);
	
	private ContextProvider contextProvider;
	private ServiceProvider serviceProvider;
	private InterpreterProvider interpreterProvider;
	private RetrieverProvider retrieverProvider;
	private SynthesizerProvider synthesizerProvider;
	
	public QAEngineProcessor(PluginManager pluginManager) {
		this.contextProvider = (ContextProvider) pluginManager.getProvider(ContextProvider.class);
		this.serviceProvider = (ServiceProvider) pluginManager.getProvider(ServiceProvider.class);
		this.interpreterProvider = (InterpreterProvider) pluginManager.getProvider(InterpreterProvider.class);
		this.retrieverProvider = (RetrieverProvider) pluginManager.getProvider(RetrieverProvider.class);
		this.synthesizerProvider =  (SynthesizerProvider) pluginManager.getProvider(SynthesizerProvider.class);
	}
	
	public QAEngineProcessor(ContextProvider contextProvider, ServiceProvider serviceProvider, 
			InterpreterProvider interpreterProvider, RetrieverProvider retrieverProvider, 
			SynthesizerProvider synthesizerProvider) {
		this.contextProvider = contextProvider;
		this.serviceProvider = serviceProvider;
		this.interpreterProvider = interpreterProvider;
		this.retrieverProvider = retrieverProvider;
		this.synthesizerProvider = synthesizerProvider;
	}
	
	public QueryResult process(String query) throws Exception {
		List<IParams> params = new ArrayList<IParams>();
		Params param = new Params();
		param.setParam(Properties.Literal.TEXT, query);
		params.add(param);
		return process(params);
	}
	
	protected QueryResult process(List<IParams> params) throws Exception {
		IContext context = contextProvider.get(IContext.class);
		Date start = new Date();
		QueryResult queryResult = process(params, serviceProvider, context);
		Date end = new Date();		
		queryResult.setRuntime(end.getTime() - start.getTime());
		logger.debug("Answer formulation Runtime(ms) " + queryResult.getRuntime());
		return queryResult;
	}
	
	protected ProcessResult executeProcess(List<? extends IParams> params, IProcess process, ServiceProvider services, IContext context) throws Exception {
		Date start = new Date();
		Throwable throwable = null;
		List<? extends IResult> results = null;
		try {
			results = process.process(params, services, context);
		} catch (Throwable t) {
			throwable = t;
		}
		Date end = new Date();		
		ProcessResult processResult = new ProcessResult();
		processResult.setOutput(results);
		processResult.setRuntime(end.getTime() - start.getTime()); // setting the runtime of the process
		processResult.setInput(params);
		processResult.setException(throwable);
		
		return processResult;
	}

	private QueryResult process(List<? extends IParams> params,
			ServiceProvider services, IContext context) throws Exception {		
		
		QueryResult result = new QueryResult();
		result.setComponentSource(this);
		result.setInput(params);
		
		logger.debug("Interpreting");
		// Interpreting
		ProcessResult processResult = executeProcess(params, interpreterProvider, services, context);
		List<? extends IResult> interpretations = processResult.getOutput();
		logger.debug("Number of interpretations: " + interpretations.size());
		logger.debug("Interpreting runtime: " + processResult.getRuntime());
		// set Interpreting result
		result.setParam(QueryResult.Attr.INTERPRETING_RESULT, processResult);
		
		logger.debug("Retrieving");
		// Retrieving
		processResult = executeProcess(interpretations, retrieverProvider, services, context);
		List<? extends IResult> retrievingResults = processResult.getOutput();
		logger.debug("Number of retrieved results: " + retrievingResults.size());
		logger.debug("Retrieval runtime: " + processResult.getRuntime());
		// set Retrieving result
		result.setParam(QueryResult.Attr.RETRIEVAL_RESULT, processResult);
		
		logger.debug("Synthesizing");
		// Synthesizing
		processResult = executeProcess(retrievingResults, synthesizerProvider, services, context);
		List<? extends IResult> synthesisResults = processResult.getOutput();
		logger.debug("Number of synthesis: " + synthesisResults.size());
		logger.debug("Synthesis runtime: " + processResult.getRuntime());
		// set Synthesizer result
		result.setParam(QueryResult.Attr.SYNTHESIS_RESULT, processResult);
		
		result.setOutput(synthesisResults);
		
		return result;
	}
}
