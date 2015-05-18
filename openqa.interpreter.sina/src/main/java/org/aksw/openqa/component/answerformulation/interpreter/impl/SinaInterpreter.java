package org.aksw.openqa.component.answerformulation.interpreter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aksw.openqa.Properties;
import org.aksw.openqa.component.answerformulation.AbstractInterpreter;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.object.Result;
import org.aksw.openqa.component.providers.impl.ServiceProvider;
import org.aksw.openqa.component.service.impl.Cache;
import org.aksw.openqa.component.service.impl.DefaultCacheService;

import HMMQuerySegmentation.Constants;

import com.Layout.ConnectionClass;

public class SinaInterpreter extends AbstractInterpreter {
	
	// Component params
	public final static String END_POINT_PARAM = "END_POINT";
	public final static String DEFAULT_GRAPH_PARAM = "DEFAULT_GRAPH";
	
	// static variables
	public final static String CACHE_CONTEXT = "SINA";
	
	public SinaInterpreter(Map<String, Object> params) {
		super(params);
	}
	
	@Override
	public boolean canProcess(IParams token) {
		String q = (String) token.getParam(Properties.Literal.TEXT);
		return q != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends IResult> process(IParams token, ServiceProvider serviceProvider, IContext context) throws Exception {		
		DefaultCacheService cacheFactory = serviceProvider.get(DefaultCacheService.class);
		Cache cache = cacheFactory.get(CACHE_CONTEXT);
		
		String query = (String) token.getParam(Properties.Literal.TEXT);
		
		List<String> queryList;
		
		queryList = (List<String>) cache.get(query);
		
		if(queryList == null) {
			ConnectionClass c=new ConnectionClass();
			c.setSearchQuery(query);
			c.runquery();
			queryList = c.getSPARQLquery_list();
			
			cache.put(query, queryList);
		}
		
		List<IResult> results = new ArrayList<IResult>();
		for(String sparql : queryList) {
			Result r = new Result();
			r.setParam(Properties.SPARQL, sparql);
			results.add(r);
		}
		return results;
	}
	
	@Override
	public void setProperties(Map<String, Object> params) {
		String endPoint = (String) params.get(END_POINT_PARAM);
		Constants.SPARQLEndPoint = endPoint;
		Model.Constants.SPARQLEndPoint = endPoint;
		
		String defaultGraph = (String) params.get(DEFAULT_GRAPH_PARAM);
		Constants.DefaultGraphString = defaultGraph;
		Model.Constants.DefaultGraphString = defaultGraph;
		super.setProperties(params); // saving parameters into the Interpreter
	}	
}
