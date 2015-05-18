package org.aksw.openqa.component.ui.render.impl;

import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.aksw.openqa.Properties;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.providers.impl.RenderProvider;
import org.aksw.openqa.component.ui.render.InfoNode;
import org.apache.commons.lang.StringEscapeUtils;

public class DBpediaSPARQLQueryRender extends BasicInfoGraph {
	
	public DBpediaSPARQLQueryRender (Map<String, Object> params) {
		super(DBpediaSPARQLQueryRender.class, params);
	}
	
	@Override
	public void printContent(RenderProvider render, InfoNode infoNode, IContext context, ServletOutputStream out) throws Exception {
		IResult result = infoNode.getResult();
		String query = (String) result.getParam(Properties.SPARQL);
		String output = "<div style=\"text-align: left;height: 60px;\">";
		output += StringEscapeUtils.escapeHtml(query);
		output += "</div>"; // closing content div
		out.print(output);
	}
	
	@Override
	public void printEnd(RenderProvider render, InfoNode infoNode, IContext context, ServletOutputStream out) throws Exception {
		IResult result = infoNode.getResult();
		String query = (String) result.getParam(Properties.SPARQL);
		String source = StringEscapeUtils.escapeHtml("http://dbpedia.org/sparql?default-graph-uri=http://dbpedia.org&query=" + query);
		if(source != null) {
			out.print("<div style=\"height:10px;\" ><a target=\"_blank\" style=\"float:right;font-size:smaller;\" href=\"" + source + "\"><span>result</span></a></div>");
		}
		out.print(endHTML);
	}
	
	@Override
	public boolean canRender(IContext context, InfoNode infoNode) throws Exception {
		IResult result = infoNode.getResult();
		return result.contains(Properties.SPARQL);
	}
}
