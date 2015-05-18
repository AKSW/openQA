package org.aksw.openqa.component.ui.render.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.aksw.openqa.Properties;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.providers.impl.RenderProvider;
import org.aksw.openqa.component.ui.render.AbstractInfoGraphRender;
import org.aksw.openqa.component.ui.render.InfoNode;
import org.apache.commons.lang.StringEscapeUtils;

public class ResultRender extends AbstractInfoGraphRender {
	
	{
		setParam("MAIN", null); // setting this render as main render
	}
	
	private final static String startHTML = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"" + 
          "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
		+ "<html xmlns=\"http://www.w3.org/1999/xhtml\""
 	+ "xmlns:h=\"http://java.sun.com/jsf/html\" " 
    + "xmlns:f=\"http://java.sun.com/jsf/core\"  "
    +"xmlns:p=\"http://primefaces.org/ui\"    "
    + " xmlns:ui=\"http://java.sun.com/jsf/facelets\">"
	+"<head>"		
	+	"<meta http-equiv=\"Content-Type\" content=\"text/javascript; charset=UTF-8\"></meta>"
	+	"<title>openQA :)</title>"
	+   "<link type=\"text/css\" rel=\"stylesheet\" href=\"/openQA/javax.faces.resource/theme.css.xhtml?ln=primefaces-aristo\"/>"
	+ 	"<link rel=\"stylesheet\" href=\"resources/css/singleEntity/layout.css\" type=\"text/css\"/>"
	+   "<link rel=\"stylesheet\" href=\"resources/css/singleEntity/singleEntity.bundle.css\" type=\"text/css\"/>"
	+ 	"<link rel=\"stylesheet\" href=\"resources/css/singleEntity/layout_ext.css\" type=\"text/css\"/>"
	+ 	"<link rel=\"stylesheet\" type=\"text/css\" href=\"resources/css/menu.css\"/>"
	+ 	"<link rel=\"stylesheet\" type=\"text/css\" href=\"resources/css/sina.css\"/>"
	+ 	"<script src=\"resources/js/jquery-1.10.2.min.js\"> </script>" 
	+ 	"<script src=\"resources/js/sina-util.js\"> </script>"
	+ "</head>"
	+ "<body style=\"height: 100%; width: 100%;margin:0px;font-family:arial,sans-serif\">"
	+ "<!--START FLOAT MENU-->"
	+	"<!-- Barra superior -->"
	+ "<div style=\"position: relative !important; top: 0px; left: 0px; z-index: 3;\">"
	+   "<div id=\"menu\" class=\"menu\" style=\"height: 41px; font-size: 12px; font-weight: bold; vertical-align: middle; background-color: black;\">"
	+ 		"<div style=\"padding-top: 10px;float:left;\">"
	+			"<a style=\"margin-left: 15px; color:white;text-decoration: none;\" href=\"./\">"
	+			"<span>Search</span>"
	+			"</a>"
	+			"<!--a style=\"margin-left: 15px; text-decoration: none;\" href=\"api.html\">"
	+				"<span>API</span>"
	+			"</a-->"
	+            "<a style=\"margin-left: 15px;text-decoration: none;\" href=\"samples.xhtml\">"
	+            "  <span>Samples</span> "
	+            "</a>"
	+			"<a style=\"margin-left: 15px; text-decoration: none;\" href=\"about.xhtml\">"
	+				"<span>About</span>"
	+			"</a>"
	+			"<!--a style=\"margin-left: 15px; text-decoration: none;\" href=\"team.html\">"
	+				"<span>Team</span>"
	+			"</a-->"
	+			"<!--a style=\"margin-left: 15px; text-decoration: none;\" href=\"publictions.html\">"
	+				"<span>Publications</span>"
	+			"</a-->"
	+			"<a style=\"margin-left: 15px; text-decoration: none;\" target=\"_blank\" href=\"http://aksw.org\">"
	+				"<span>AKSW</span>"
	+			"</a>"
	+		"</div>"
	+ 		"<span>"
	+			"<a class=\"button_p\" style=\"float:right; height: 30px;width: 64px;margin-top:3px;margin-right:15px;text-align: center;vertical-align: middle;\" href=\"admin/manager.xhtml\">"
	+				"<img style=\"margin-top:3px;\" height=\"26\" width=\"26\" src=\"resources/images/settings.png\" alt=\"Manager\" />"
	+			"</a>"
	+		"</span>"
	+	"</div>"
	+ "</div>"
	+ "<div>"
	+		"<span>"
	+			"<div>"
	+				"<div style=\"margin-top:10px;margin-left:10px;\">"
	+					"<a href=\"./\"><div align=\"left\" style=\"background:url(resources/images/logo.png) no-repeat;background-size:75px 40px;height:40px;width:75px;position:absolute;\"></div></a>"
	+					"<input ";
		
	private final static String searchInputQuery = " id=\"searchInput\" onkeypress=\"event.keyCode==13?window.location.href='search?q='+searchInput.value:false;\" onkeyup=\"h.href='search?q='+searchInput.value;r.href='search?q='+searchInput.value+'&amp;content=sparql';\" type=\"text\" style=\"width: 550px;font-size: x-large;height: 40px;position: relative;margin-left: 90px;top: 2px;\" class=\"ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all\">";
	
	private final static String afterQuery = "</div>"
	+			"</div>"
	+		"</span>"
	+		"<div style=\"font-weight:bold;margin-top:10px;\">"
	+			"<!--a style=\"margin-left: 15px; text-decoration: none;color:rgb(162, 150, 150);\" href=\"/main/data.html\">"
	+				"<span>Web</span>"
	+			"</a>"
	+			"<a style=\"margin-left: 15px; text-decoration: none;color:rgb(162, 150, 150);\" href=\"/main/data.html\">"
	+				"<span>DBpedia</span>"
	+			"</a>"
	+			"<a style=\"margin-left: 15px; text-decoration: none;color:rgb(162, 150, 150);\" href=\"/main/data.html\">"
	+				"<span>Life Science</span>"
	+			"</a>"
	+			"<a style=\"margin-left: 15px; text-decoration: none;color:rgb(162, 150, 150);\" href=\"/main/data.html\">"
	+				"<span>Geo</span>"
	+			"</a-->"
	+		"</div>"
	+	"<hr class=\"ui-separator ui-state-default ui-corner-all\">"
	+ "</div>";
		
	private final static String endHTML = "</body>" +
											"</html>";
	
	private String linkHumanQuery = "<a id=\"h\" style=\"margin-left: 10px;\" class=\"button_go\" href=\"search?q=\">Graph Search</a>";	
	private String linkRobotQuery = "<a id=\"r\" style=\"margin-left: 10px;\" class=\"button_robot\" href=\"search?q=&amp;content=sparql\">" + StringEscapeUtils.escapeHtml("I´m a robot") + "</a>";
	
	public ResultRender(Map<String, Object> params) {
		super(ResultRender.class, params);
	}

	@Override
	public void printStart(RenderProvider render, InfoNode result, IContext context, ServletOutputStream out) throws Exception {
		String query = (String) context.getInputQuery();
		String linkHumanQuery = this.linkHumanQuery.replace("search?q=", "search?q=" + query);
		String linkRobotQuery = this.linkRobotQuery.replace("search?q=", "search?q=" + query);
		out.print(startHTML + " value=\"" + query +"\""+ searchInputQuery + linkHumanQuery + linkRobotQuery + afterQuery);
	}
	
	@Override
	protected void printContent(RenderProvider render, InfoNode node, IContext context, ServletOutputStream out)
			throws Exception {
		List<InfoNode> infoNodeList = node.getInfoList();
		if(infoNodeList != null) {
			List<InfoNode> infoCopyNodeList = new ArrayList<InfoNode>(infoNodeList);
			// filtering categories
			for(InfoNode infoNode : infoNodeList) {
				IResult result = infoNode.getResult();
				// checking value
				Object value = result.getParam(Properties.RESOURCE);
				if(value!= null) {
					String stringValue = value.toString();
					String normalizedStringValue = stringValue.toLowerCase();
					if(normalizedStringValue.contains("category:"))
						infoCopyNodeList.remove(infoNode);
				}
			}
			
			if(infoCopyNodeList.size() > 1) {
				for(InfoNode infoNode : infoCopyNodeList) {
					render.render(render, infoNode, context, out);
				}
			} else {
				for(InfoNode infoNode : infoCopyNodeList) {
					DBpediaSingleEntityRender singleEntityRender = new DBpediaSingleEntityRender();
					singleEntityRender.render(render, infoNode, context,  out);
				}
			}
		}
	}
	
	@Override
	public void printEnd(RenderProvider render, InfoNode result, IContext context, ServletOutputStream out) throws Exception {
		out.print(endHTML);
	}

	@Override
	public boolean canRender(IContext context, InfoNode result) throws Exception {
		return true;
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
	public String getDescription() {
		return "This plugin is designed to render the root main search page.";
	}

	@Override
	public String getLicense() {
		return "Apache License 2.0";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getLabel() {
		return "InfographMainRender";
	}
}
