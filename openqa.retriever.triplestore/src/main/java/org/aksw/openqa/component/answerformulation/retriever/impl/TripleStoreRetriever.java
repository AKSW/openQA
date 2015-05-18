package org.aksw.openqa.component.answerformulation.retriever.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.aksw.openqa.Properties;
import org.aksw.openqa.component.answerformulation.AbstractRetriever;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.object.Result;
import org.aksw.openqa.component.providers.impl.ServiceProvider;
import org.aksw.openqa.util.SPARQLUtil;
import org.apache.log4j.Logger;

import com.hp.hpl.jena.datatypes.BaseDatatype.TypedValue;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementPathBlock;

public class TripleStoreRetriever extends AbstractRetriever {
	
	private static Logger logger = Logger.getLogger(TripleStoreRetriever.class);
	
	// token 
	public static final String END_POINT_PARAM = "END_POINT";
	public static final String DEFAULT_GRAPH_PARAM = "DEFAULT_GRAPH";
	
	public TripleStoreRetriever(Map<String, Object> params) {
		super(params);
	}

	@Override
	public boolean canProcess(IParams token) {
		return token.contains(Properties.SPARQL);
	}

	@Override
	public List<? extends IResult> process(IParams param, ServiceProvider serviceProvider, IContext context) {
		String sparqlParam = (String) param.getParam(Properties.SPARQL);
		
		String graph = (String) getParam(DEFAULT_GRAPH_PARAM);
		String endPoint = (String) getParam(END_POINT_PARAM);
		
		return getSPARQLQueryResult(param, sparqlParam, endPoint, graph);
	}
	
	public List<Result> getSPARQLQueryResult(IParams inputParam, String sparqlQuery, String endPoint, String defaultGraphString) {
		List<Result> results = new ArrayList<Result>();
		
		// if there is no ASK and LIMIT placed in query
		if(!sparqlQuery.toLowerCase().contains("ask") && !sparqlQuery.toLowerCase().contains("limit"))
			sparqlQuery= sparqlQuery + " limit 10 "; // add LIMIT
		
		logger.debug("Processing query " + sparqlQuery);
		Query query = QueryFactory.create(sparqlQuery);
		boolean isQueryStar = SPARQLUtil.isSingleEntity(query);
		if(!isQueryStar) {
			QueryEngineHTTP qexec = new QueryEngineHTTP(endPoint, query);
			qexec.setTimeout(30000, 30000);
			qexec.addDefaultGraph(defaultGraphString);
			try {
			    ResultSet rs = qexec.execSelect();
				List<String> headers = rs.getResultVars();
				while(rs.hasNext()) {
			    	QuerySolution  solution=rs.nextSolution();
					for(String p : headers) {
						Result result = new Result(inputParam, this); // setting the input params and source component
						RDFNode node = solution.get(p);
						if(node.isURIResource()) {
							String value = node.toString();
							result.setParam(Properties.URI, value);
						} else if (node.isLiteral()) {
							Object value = node.asLiteral().getValue();
							if(value instanceof Number)
								result.setParam(Properties.Literal.NUMBER, value);
							else if (value instanceof Date)
								result.setParam(Properties.Literal.DATE, value);
							else if(value instanceof Boolean)
								result.setParam(Properties.Literal.BOOLEAN, value);
							else if(value instanceof TypedValue) {
								TypedValue typedValue = (TypedValue) value;
								result.setParam(Properties.Literal.TEXT, typedValue.lexicalValue);
							}
						} else if (node.isResource()) {
							String value = (String) node.asResource().getURI();
							result.setParam(Properties.URI, value);
						}
						results.add(result);
		        	}
				}			
			} catch(Exception e) {
				logger.error("Error executing query: " + sparqlQuery,  e);
			} finally {
				qexec.close();
			}
		} else {
			ElementGroup elementGroup  = (ElementGroup) query.getQueryPattern();
			ElementPathBlock elementTripleBlock = (ElementPathBlock) elementGroup.getElements().get(0);
			String value = elementTripleBlock.getPattern().get(0).getSubject().toString();
			Result result = new Result(inputParam, this); // setting the input params and source component
			result.setParam(Properties.URI, value);
			results.add(result);
		}
		return results;
	}

}
