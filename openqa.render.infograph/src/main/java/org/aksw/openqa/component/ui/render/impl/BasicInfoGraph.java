package org.aksw.openqa.component.ui.render.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.aksw.openqa.component.IPlugin;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.providers.impl.RenderProvider;
import org.aksw.openqa.component.ui.render.AbstractInfoGraphRender;
import org.aksw.openqa.component.ui.render.InfoNode;

public abstract class BasicInfoGraph extends AbstractInfoGraphRender {
	
	public final static String startHTML = "\n<div style=\"" 
		    + "background-color: rgb(241, 241, 241);"
		    + "box-shadow:0px 3px 2px rgba(0,0,0,0.25);"
		    + "margin: 10px;padding: 10px;\">\n";
		
	public final static String endHTML = "</div>";

	String color = "rgb(241, 241, 241);";
	String title = null;
	String source = null;
	
	public BasicInfoGraph() {		
	}
	
	public BasicInfoGraph(Class<? extends IPlugin> c, Map<String, Object> params) {
		super(c, params);
	}

	public BasicInfoGraph(String title, Map<String, Object> params) {
		this.title = title;
	}
		
	public BasicInfoGraph(String title, String source, Map<String, Object> params) {
		this.title = title;
		this.source = source;
	}
	
	public BasicInfoGraph(String title, String color, String source, Map<String, Object> params) {
		this.title = title;
		this.color = color;
		this.source = source;
	}
		
	@Override
	protected void printStart(RenderProvider render, InfoNode infoNode, IContext context, ServletOutputStream out) throws Exception {
		out.print(startHTML);
	}
	
	@Override
	protected void printEnd(RenderProvider render, InfoNode infoNode, IContext context, ServletOutputStream out) throws Exception {
//		this.source = infoNode.getResult().getSource();
//		if(source != null) {
//			out.print("<div style=\"height:10px;\" ><a target=\"_blank\" style=\"float:right;font-size:smaller;\" href=\"" + source + "\"><span>" + source + "</span></a></div>");
//		}
		out.print(endHTML);
	}

	@Override
	public boolean canRender(IContext context, InfoNode infoNode) throws Exception {
		List<InfoNode> infoList = infoNode.getInfoList();
		return infoList != null;
	}

	@Override
	protected void printContent(RenderProvider render, InfoNode infoNode, IContext context, ServletOutputStream out)
			throws Exception {
		List<InfoNode> infoList = infoNode.getInfoList();
		for(InfoNode childNode : infoList) {
			render.render(render, childNode, context, out);
		}
	}

	@Override
	public String getAuthor() {
		return "Edgard Marx";
	}

	@Override
	public String getContact() {
		return "http://aksw.org/EdgardMarx";
	}

	@Override
	public String getLicense() {
		return "Apache License 2.0";
	}
}