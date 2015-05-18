package org.aksw.openqa.component.ui.render.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;

import org.aksw.openqa.Properties;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.providers.impl.RenderProvider;
import org.aksw.openqa.component.ui.render.IDGenerator;
import org.aksw.openqa.component.ui.render.InfoNode;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.hp.hpl.jena.rdf.model.Resource;

public class DBpediaRender extends BasicInfoGraph {	
	
	public DBpediaRender(Map<String, Object> params) {
		super(DBpediaRender.class, params);
	}
	
	private String getResourceLoader(String resource, long labelID, long commentID, long pageID, long imageID) throws IOException, JSONException {		
		String jsonFile = resource.toString().replace("resource", "data");
		
		return "\n<script><!--\n" +
				"(function() {" +
					"var jsonFile = \"" + jsonFile + ".json\";" +
					"jQuery.getJSON(jsonFile, function(data) {" +
					"var h = (data[\"" + resource + "\"][\"http://xmlns.com/foaf/0.1/homepage\"]);" +
					"var d = (data[\"" + resource + "\"][\"http://xmlns.com/foaf/0.1/depiction\"]);" +
					"if(isDefined(h)){document.getElementById(\"page" + pageID + "\").innerHTML = h[0].value;" +
					"document.getElementById(\"page" + pageID + "\").href = h[0].value;}" +
					"if(isDefined(d)){var picture = d[0].value;" +
					//"picture = picture.replace('commons','en');" +
					//"jQuery( \"<img/>\" ).attr( \"src\", picture ).css( \"height\", \"auto\" ).css( \"width\", 50 ).appendTo( \"#image" + imageID + "\" );}" +
					"jQuery( \"<img/>\" ).attr( \"src\", picture ).css( \"height\", \"auto\" ).css( \"width\", 50 ).error(function(){picture = picture.replace('commons','en');jQuery( this ).attr( \"src\", picture );}).appendTo( \"#image" + imageID + "\" );}" +
				"});})();\n//-->" +
			"</script>\n";
	}
	
	public static boolean exists(String URLName){
	    try {
	      HttpURLConnection.setFollowRedirects(false);
	      // note : you may also need
	      //        HttpURLConnection.setInstanceFollowRedirects(false)
	      HttpURLConnection con =
	         (HttpURLConnection) new URL(URLName).openConnection();
	      con.setRequestMethod("HEAD");
	      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	    }
	    catch (Exception e) {
	       e.printStackTrace();
	       return false;
	    }
	  }  
	
	@Override
	public void printContent(RenderProvider render, InfoNode result, IContext context, ServletOutputStream out) throws Exception {		
		String output = "";
		Entry<String, Object> entry = result.getResult().getParameters().entrySet().iterator().next();
		String type = entry.getKey();
		Object value = (String) entry.getValue();
		long contentID = IDGenerator.getInstance().generateID();
		if(type == Properties.RESOURCE || type == Properties.URI) {
			String resourceURI = StringEscapeUtils.escapeHtml(value.toString());
			source = "http://wikipedia.org/wiki/" + resourceURI.substring(resourceURI.lastIndexOf("/") + 1);
			String jsonFile = resourceURI.toString().replace("resource", "data") + ".json";
			String label = "undefined";
			String comment = "no comment";

			long imageID = IDGenerator.getInstance().generateID();
			long labelID = IDGenerator.getInstance().generateID();
			long pageID = IDGenerator.getInstance().generateID();
			long commentID = IDGenerator.getInstance().generateID();

			try {
				Resource resource = JenaUtils.getResource(resourceURI, "http://dbpedia.org/sparql", "http://dbpedia.org");
				
				label = "undefined";
				org.json.JSONArray labels = null;
				if(JenaUtils.exists(resource,"http://dbpedia.org/property/fullname")) {
					label = JenaUtils.getValue(resource,"http://dbpedia.org/property/fullname");
				} else if(JenaUtils.exists(resource,"http://dbpedia.org/property/name")) {
					label = JenaUtils.getValue(resource,"http://dbpedia.org/property/name");
				} else if(JenaUtils.exists(resource,"http://www.w3.org/2000/01/rdf-schema#label"))
					label = JenaUtils.getValue(resource,"http://www.w3.org/2000/01/rdf-schema#label");
				
//				JSONObject resourceObject = object.getJSONObject(resourceURI);
//				label = "undefined";
//				org.json.JSONArray labels = null;
//				if(resourceObject.has("http://dbpedia.org/property/fullname")) {
//					labels = resourceObject.getJSONArray("http://dbpedia.org/property/fullname");
//				} else if(resourceObject.has("http://dbpedia.org/property/name")) {
//					labels = resourceObject.getJSONArray("http://dbpedia.org/property/name");
//				} else if(resourceObject.has("http://www.w3.org/2000/01/rdf-schema#label"))
//					labels = resourceObject.getJSONArray("http://www.w3.org/2000/01/rdf-schema#label");
				
				if(labels != null)
					label = getLang(labels, "value", "en");				
				
				if(JenaUtils.exists(resource, "http://www.w3.org/2000/01/rdf-schema#comment")) {
					comment = JenaUtils.getValue(resource,"http://www.w3.org/2000/01/rdf-schema#comment");
				}
			} catch (Exception e) {
				label = "Warning";
				comment = "Problem retrieving content.";
			}			
			
			output = "<h3 id=\"label" + labelID + "\" style=\"margin: 5px;\">"+ StringEscapeUtils.escapeHtml(label) +"</h3>";
			
			output +=
			"<div id=\"image" + imageID + "\" style=\"float:left;margin: 0 10 10 10;\"></div>" +
			"<div id=\"content" +  contentID + "\" style=\"text-align: left;height: 60px;\">" +
				"<div id =\"comment" + commentID + "\">" +  StringEscapeUtils.escapeHtml(comment) + "</div>" +
				"<a id =\"page" + pageID + "\" style=\"font-size: small;\" href=\"\" target=\"_blank\"></a>";
			
			output += getResourceLoader(resourceURI, labelID, commentID, pageID, imageID);
			output += "</div>"; // closing content div
		} else if (value != null) {
			output = "<div id=\"content" +  contentID + "\" style=\"text-align: left;height: 60px;font-size: xx-large;\">";
			output +=  value;// resource.asLiteral().getValue();
			output += "</div>"; // closing content div
		}
		
		out.print(output);
	}
	
	@Override
	public boolean canRender(IContext context, InfoNode infoNode)
			throws Exception {
		IResult result = infoNode.getResult();
		return result.contains(Properties.URI) ||
				result.contains(Properties.Literal.NUMBER) ||
				result.contains(Properties.Literal.DATE) ||
				result.contains(Properties.Literal.BOOLEAN) ||
				result.contains(Properties.Literal.TEXT) ||
				result.contains(Properties.RESOURCE);
	}
	
	private String getLang(org.json.JSONArray array, String attribute, String lang) throws JSONException {
		for(int i=0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			if(obj.has("lang")) {
				if(obj.getString("lang").equals(lang))
					return obj.getString(attribute);
			}
		}
		return null;
	}
	
	@Override
	protected void printEnd(RenderProvider render, InfoNode infoNode, IContext context, ServletOutputStream out) throws Exception {
		String resource = (String) infoNode.getResult().getParam(Properties.URI);
		if(resource != null) {
			resource = StringEscapeUtils.escapeHtml(resource);
			String source = "http://wikipedia.org/wiki/" + resource.substring(resource.toString().lastIndexOf("/") + 1);
			if(source != null) {
				out.print("<div style=\"height:10px;\" ><a target=\"_blank\" style=\"float:right;font-size:smaller;\" href=\"" + source + "\"><span>" + source + "</span></a></div>");
			}
		}		
		out.print(endHTML);
	}
}
