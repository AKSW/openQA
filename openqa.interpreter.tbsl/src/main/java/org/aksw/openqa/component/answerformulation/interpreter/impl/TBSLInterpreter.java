package org.aksw.openqa.component.answerformulation.interpreter.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.aksw.autosparql.commons.knowledgebase.RemoteKnowledgebase;
import org.aksw.autosparql.tbsl.algorithm.learning.TBSL;
import org.aksw.autosparql.tbsl.algorithm.learning.TemplateInstantiation;
import org.aksw.openqa.Properties;
import org.aksw.openqa.component.answerformulation.AbstractInterpreter;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.object.Result;
import org.aksw.openqa.component.providers.impl.ServiceProvider;
import org.aksw.openqa.component.service.impl.Cache;
import org.aksw.openqa.component.service.impl.DefaultCacheService;
import org.aksw.openqa.util.SPARQLUtil;
import org.aksw.rdfindex.HierarchicalIndex;
import org.aksw.rdfindex.Index;
import org.aksw.rdfindex.Indices;
import org.aksw.rdfindex.SOLRIndex;
import org.apache.log4j.Logger;
import org.dllearner.kb.sparql.SparqlEndpoint;

public class TBSLInterpreter extends AbstractInterpreter {
	
	private static Logger logger = Logger.getLogger(TBSLInterpreter.class);
	
	//Component params
	public final static String END_POINT_PARAM = "END_POINT_PARAM";
	public final static String SOLR_SERVER_PARAM = "SOLR_SERVER_PARAM";
	public final static String DEFAULT_GRAPH_PARAM = "DEFAULT_GRAPH_PARAM";
	
	//Cache params
	public final static String CACHE_CONTEXT = "TBSL";	

	//Defining default Component params
	{
		setParam(END_POINT_PARAM, "http://linkedspending.aksw.org/sparql");
		setParam(SOLR_SERVER_PARAM, "http://linkedspending.aksw.org/solr/en_");
		//setParam(SOLR_SERVER_PARAM, "http://solr.aksw.org/en_");
		setParam(DEFAULT_GRAPH_PARAM, "http://dbpedia.org");
	}
	
	private TBSL tbsl;
	
	public TBSLInterpreter(Map<String, Object> params) {
		super(params);
	}
	
	@Override
	public boolean canProcess(IParams param) {
		return param.contains(Properties.Literal.TEXT);
	}

	@Override
	public List<? extends IResult> process(IParams param, ServiceProvider serviceProvider, IContext context) throws Exception {
		String query = (String) param.getParam(Properties.Literal.TEXT);
		
		DefaultCacheService cacheService = (DefaultCacheService) serviceProvider.get(DefaultCacheService.class);
		Cache cache = cacheService.get(CACHE_CONTEXT);
		
		List<IResult> results = new ArrayList<IResult>();
		String sparql = (String) cache.get(query);
		if(sparql == null) {
			TemplateInstantiation ti;
			ti = tbsl.answerQuestion(query);
			sparql = ti.getQuery();
			cache.put(query, sparql);
		}
		
		if(SPARQLUtil.isGeneric(sparql)) {
			logger.warn("TBSL generates a generic query."); 
			return results;
		}
		
		Result r = new Result(param, this);
		r.setParam(Properties.SPARQL, sparql);
				
		results.add(r);
		return results;
	}
	
	@Override
	public void setProperties(Map<String, Object> entries) {
		if(isActive()) {
			String knowledgeBase = (String) entries.get(END_POINT_PARAM);
			String defaultGraph = (String) entries.get(END_POINT_PARAM);
			String solrServer = (String) entries.get(SOLR_SERVER_PARAM);
			tbsl = newTBSLInstance(knowledgeBase, defaultGraph, solrServer);
		}
		super.setProperties(entries);
	}
	
	@Override
	public void setActive(boolean active) {		
		if(active) {
			String knowledgeBase = (String) getParam(END_POINT_PARAM);
			String defaultGraph = (String) getParam(END_POINT_PARAM);
			String solrServer = (String) getParam(SOLR_SERVER_PARAM);
			tbsl = newTBSLInstance(knowledgeBase, defaultGraph, solrServer);
		} else
			tbsl = null;
		super.setActive(active);
	}
	
	private static TBSL newTBSLInstance(String knowledgeBase, String knowledgeGraph, String solrServer) {		
		RemoteKnowledgebase rkbase = new RemoteKnowledgebase(newDbpediaEndpoint(knowledgeBase, knowledgeGraph), "dbpedia", "DBpedia", newSolrIndices(solrServer));
		TBSL tbsl = new TBSL(rkbase, new String[]{"tbsl/lexicon/english.lex"});
		return tbsl;
	}
	
	private static SparqlEndpoint newDbpediaEndpoint(String endPointURL, String defautlGraph)
	{
		SparqlEndpoint endPoint = null;
		try{
			endPoint = new SparqlEndpoint(new URL(endPointURL),Collections.<String>singletonList(defautlGraph), Collections.<String>emptyList());
		}
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		return endPoint;
	}
	
	private static Indices newSolrIndices(String solrServerURL) {
		SOLRIndex resourcesIndex = new SOLRIndex(solrServerURL+"dbpedia_resources");		
		SOLRIndex classesIndex = new SOLRIndex(solrServerURL+"dbpedia_classes");
		SOLRIndex noBoaDataPropertiesIndex = new SOLRIndex(solrServerURL+"dbpedia_data_properties");
		SOLRIndex noBoaObjectPropertiesIndex = new SOLRIndex(solrServerURL+"dbpedia_data_properties");
		for(SOLRIndex index: new SOLRIndex[] {resourcesIndex,classesIndex,noBoaObjectPropertiesIndex,noBoaDataPropertiesIndex})
		{index.setPrimarySearchField("label");}
		SOLRIndex boaIndex = new SOLRIndex(solrServerURL+"boa", "nlr-no-var");
		boaIndex.setSortField("boa-score");
		try {
			boaIndex.getResources("test");
		} catch (Error e) {
			logger.error("Error initializing the class.", e);
		} catch (Exception e) {
			logger.error("Error initializing the class.", e);
		}
		Index dataPropertiesIndex = new HierarchicalIndex(noBoaDataPropertiesIndex,boaIndex);
		Index objectPropertiesIndex = new HierarchicalIndex(noBoaObjectPropertiesIndex,boaIndex);
		Indices dbpediaIndices = new Indices(resourcesIndex,classesIndex,objectPropertiesIndex,dataPropertiesIndex);
		return dbpediaIndices;
	}
}
