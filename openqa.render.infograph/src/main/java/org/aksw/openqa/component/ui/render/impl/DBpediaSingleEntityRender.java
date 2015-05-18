package org.aksw.openqa.component.ui.render.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.aksw.openqa.Properties;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.providers.impl.RenderProvider;
import org.aksw.openqa.component.ui.render.IDGenerator;
import org.aksw.openqa.component.ui.render.InfoNode;
import org.aksw.openqa.util.JSONUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class DBpediaSingleEntityRender extends BasicInfoGraph {
	
	public DBpediaSingleEntityRender(Map<String, Object> params) {
		super(DBpediaSingleEntityRender.class, params);
	}
	
	public DBpediaSingleEntityRender() {
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
	protected void printStart(RenderProvider render, 
			InfoNode infoNode,
			IContext context,
			ServletOutputStream out)
			throws Exception {
//		// TODO Auto-generated method stub
//		super.printStart(userInputParam, render, infoNode, out);
	}
	
	@Override
	public void printContent(RenderProvider render, InfoNode infoNode, IContext context, ServletOutputStream out) throws Exception {		
		String output = "";
		IResult result = infoNode.getResult();
		String attrValue = null;
		String attrKey = null;
		if(result.contains(Properties.URI)) {
			attrValue = (String) result.getParam(Properties.URI);
			attrKey = Properties.URI;
		} else if (result.contains(Properties.Literal.NUMBER))
			attrValue = (String) result.getParam(Properties.Literal.NUMBER);
		else if (result.contains(Properties.Literal.DATE))
			attrValue = (String) result.getParam(Properties.Literal.DATE);
		else if (result.contains(Properties.Literal.BOOLEAN))
			attrValue = (String) result.getParam(Properties.Literal.BOOLEAN);
		else {
			attrValue = (String) result.getParam(Properties.RESOURCE);
			attrKey = Properties.RESOURCE;
		}
		
		long contentID = IDGenerator.getInstance().generateID();
		if(attrKey == Properties.URI  ||
				attrKey == Properties.RESOURCE) {
			
			attrValue = StringEscapeUtils.escapeHtml(attrValue);
			
			String jsonFile = attrValue.toString().replace("resource", "data") + ".json";
			JSONObject object = JSONUtil.read(jsonFile);
			
			source = "http://wikipedia.org/wiki/" + attrValue.substring(attrValue.lastIndexOf("/") + 1);
			long imageID = IDGenerator.getInstance().generateID();
			long labelID = IDGenerator.getInstance().generateID();
			long pageID = IDGenerator.getInstance().generateID();
			long commentID = IDGenerator.getInstance().generateID();
			
			JSONObject resourceObject = object.getJSONObject(attrValue);
			String label = "undefined";
			org.json.JSONArray labels = null;
			if(resourceObject.has("http://dbpedia.org/property/fullname")) {
				labels = resourceObject.getJSONArray("http://dbpedia.org/property/fullname");
			} else if(resourceObject.has("http://dbpedia.org/property/name")) {
				labels = resourceObject.getJSONArray("http://dbpedia.org/property/name");
			} else if(resourceObject.has("http://www.w3.org/2000/01/rdf-schema#label"))
				labels = resourceObject.getJSONArray("http://www.w3.org/2000/01/rdf-schema#label");
			
			String lat = null;
			String lon = null;
			
			if(resourceObject.has("http://www.w3.org/2003/01/geo/wgs84_pos#lat")) {
				org.json.JSONArray latArray = resourceObject.getJSONArray("http://www.w3.org/2003/01/geo/wgs84_pos#lat");
				lat = getAtt(latArray, "value");
			}
			
			if(resourceObject.has("http://www.w3.org/2003/01/geo/wgs84_pos#long")) {
				org.json.JSONArray lonArray = resourceObject.getJSONArray("http://www.w3.org/2003/01/geo/wgs84_pos#long");
				lon = getAtt(lonArray, "value");
			}
				
			if(labels != null)
				label = getAttLang(labels, "value", "en");
			
			String comment = "no comment";
			if(resourceObject.has("http://www.w3.org/2000/01/rdf-schema#comment")) {
				org.json.JSONArray comments = resourceObject.getJSONArray("http://www.w3.org/2000/01/rdf-schema#comment");
				if(comments != null)
					comment = getAttLang(comments, "value", "en");
			}
			
			output = "<div class=\"container\" style=\"max-width:inherit;\">" +
						"<div id=\"plans-top-mbox\" class=\"content-main\" style=\"float: none; width: auto; font-size:larger; visibility: visible; margin: 30px;display: block;text-align:left;background-color:white;" +
								"box-shadow: 0px 2px 10px #888888;\">" +
							  "<div class=\"content-header\" style=\"box-shadow:0px 1px 1px #DFDFDF; background-color:#F8F8F8;\">" +
								"<div class=\"header-inner\" style=\"background-color:#F8F8F8;\">" +
								  "<h2 style=\"font-weight:bold;font-size:large;\">"+ StringEscapeUtils.escapeHtml(label) + "</h2>" +
								"</div>" +
							  "</div>" +
							  "<div class=\"stream-container\">" +
									"<div class=\"stream profile-stream\">";
			
			if(lat != null && lon != null) {
				output += "<div class=\"kno-card\">" +
						"<li class=\"mod\" style=\"clear:none\">" +
						"<div class=\"kno-mrg kno-swp kno-mrg-nsi\" id=\"media_result_group\" data-hveid=\"177\" style=\"position:relative\">" +
							"<ol>" +
								"<li class=\"mod\"><!--m-->" +
									"<div class=\"kno-mrg-m\">" +
										"<div class=\"kno-fb-ctx\">" +
											"<div class=\"rhsg4 rhsmap5col\">" +
												"<a target=\"_blank\" href=\"http://maps.google.de/maps?q=" + StringEscapeUtils.escapeHtml(label)  + "\" style=\"position:relative;height:160px;width:454px;display:block\"><img src=\"http://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lon + "&zoom=13&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C" + lat + "," + lon + "&sensor=false\" height=\"100%\" width=\"100%\" id=\"lu_mapß\" alt=\"\" border=\"0\">" +
												"</a>" +
											"</div>" +
										"</div>" +
									"</div>" +
									"<div style=\"clear:both\">" +
									"</div><!--n-->" +
								"</li>" +
							"</ol>" +
						"</div>" +
					"</li>" +
				"</div>" ;
			}
										
			output +=					"<div style=\"margin:20px;\">" +
//											"<div class=\"kno-card\">" +
//												"<li class=\"mod\" style=\"clear:none\"><!--m-->" +
//													"<div aria-level=\"2\" class=\"kno-ecr-t kno-ecr-t-w-st kno-fb-ctx\" role=\"heading\" data-hveid=\"183\" data-ved=\"0CLcBEIcoKAAwEA\">" +
//														"<span class=\"krable\"></span>" +
//														"<div>" +
//															"<div class=\"kno-ecr-st-c\">" +
//																"<div class=\"kno-ecr-st\">" +
//																	"<span class=\"kno-ecr-st-val\" style=\"" +
//																					    "font-style: italic;" +
//																					    "font-size: larger;" +
//																					    "font-weight: bold;" +
//																					"\">City in Germany</span>" +
//																"</div>" +
//															"</div>" +
//														"</div>" +
//													"</div><!--n-->" +
//												"</li>" +
//											"</div>" +
											"<div class=\"kno-card\">" +
												"<li class=\"mod\" style=\"clear:none\"><!--m-->" +
													"<div class=\"kno-fb-ctx kno-desc\" data-hveid=\"158\" data-ved=\"0CJ4BEM4gKAAwEQ\">" +
														"<div class=\"kno-rdesc\">" +
															"<span>" + StringEscapeUtils.escapeHtml(comment) +"</span>" +
														"</div>" +
														"<div class=\"krable\" data-ved=\"0CKABEP8dMBE\" style=\"margin:2px 0 4px\"></div>" +
													"</div><!--n-->" +
												"</li>" +
											"</div>" +
											"<div class=\"kno-card\">" +
												"<li class=\"mod\" style=\"clear:none\"><!--m--><!--n--></li>" +
											"</div>" +
											"<div class=\"kno-card\">" +
												"<li class=\"mod\" style=\"clear:none\"><!--m--><!--n--></li>" +
											"</div>" +
//											"<div class=\"kno-card\">" +
//												"<li class=\"mod\" style=\"clear:none\"><!--m-->" +
//													"<div class=\"kno-fr\">" +
//														"<div class=\"kno-f kno-fb-ctx\" data-ved=\"0CMQBEMsTKAAwFA\">" +
//															"<span class=\"krable\" data-ved=\"0CMUBEP8dKAAwFA\" style=\"margin:0 0 5px\"></span>" +
//															"<span class=\"kno-fh\">" +
//																"<a class=\"fl\" href=\"/search?rlz=1C1GGGE_pt-BRBR472BR491&amp;espv=210&amp;es_sm=93&amp;q=leipzig+area&amp;stick=H4sIAAAAAAAAAGOovnz8BQMDgwYHnxCHfq6-gUl2momWVHaylX5OfnJiSWZ-HpxhlViUmhjl11Ex5_sxXqVPJifWn-9a9FrS-g0Ax_in5EUAAAA&amp;sa=X&amp;ei=s2OsUrvdEOONyQOPvYCgBQ&amp;ved=0CMYBEOgTKAEwFA\">Area</a>: " +
//															"</span>" +
//															"<span class=\"kno-fv\"> "+
//																"<span class=\"kno-fv-vq fl\">297.6 km²</span>" +
//															"</span>" +
//														"</div>" +
//													"</div><!--n--> " +
//												"</li>" +
//											"</div>" +
										"</div>" +
										"<div class=\"stream-footer\">" +
											"<div class=\"timeline-end has-items has-more-items\">" +
													
												"<div class=\"stream-loading\" style=\"margin:0;\">" +
													
												"</div>" +
										   "</div>" +
										"</div>" +
									"</div>" +
							  "</div>" +
							  "<div class=\"flex-module profile-banner-footer clearfix\">" +
								"<div class=\"default-footer\">" +
										

											"<div class=\"user-actions btn-group not-following\" data-protected=\"false\">" +


									"<a target=\"_blank\" href=\"" + source + "\">" + "<button style=\"width:100%;border-bottom-left-radius: 6px;border-bottom-right-radius: 6px;\" class=\"user-actions-follow-button js-follow-btn follow-button btn\" type=\"button\">" +
									  "<span class=\"button-text follow-text\">" +
										 "<i class=\"follow\"></i>Wikipedia" +
										
									  "</span>" +
									"</button> </a>" +
									"</div>" +
								"</div>"+
							"</div>"+
						"</div>" +
						"<span class=\"hrule collapsible_section_rule\"></span>" +
					"</div>";
			
//			output = "<h3 id=\"label" + labelID + "\" style=\"margin: 5px;\">"+ StringEscapeUtils.escapeHtml(label) +"</h3>";
//			
//			output +=
//			"<div id=\"image" + imageID + "\" style=\"float:left;margin: 0 10 10 10;\"></div>" +
//			"<div id=\"content" +  contentID + "\" style=\"text-align: left;height: 60px;\">" +
//				"<div id =\"comment" + commentID + "\">" +  StringEscapeUtils.escapeHtml(comment) + "</div>" +
//				"<a id =\"page" + pageID + "\" style=\"font-size: small;\" href=\"\" target=\"_blank\"></a>";
//			
//			output += getResourceLoader(resource, labelID, commentID, pageID, imageID);
//			output += "</div>"; // closing content div
		} else if (attrValue != null){
			output = "<div id=\"content" +  contentID + "\" style=\"text-align: left;height: 60px;font-size: xx-large;\">";
			output +=  attrValue;// resource.asLiteral().getValue();
			output += "</div>"; // closing content div
		} else {
			output = "<div id=\"content" +  contentID + "\" style=\"text-align: left;height: 60px;font-size: xx-large;\">";
			output +=  "Error :(";// resource.asLiteral().getValue();
			output += "</div>"; // closing content div
		}
		
		out.print(output);
	}
	
	private String getAttLang(org.json.JSONArray array, String attribute, String lang) throws JSONException {
		for(int i=0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			if(obj.has("lang")) {
				if(obj.getString("lang").equals(lang))
					return obj.getString(attribute);
			}
		}
		return null;
	}
	
	private String getAtt(org.json.JSONArray array, String attribute) throws JSONException {
		for(int i=0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
				return obj.getString(attribute);
		}
		return null;
	}
	
	@Override
	protected void printEnd(RenderProvider render, InfoNode infoNode, IContext context, ServletOutputStream out) throws Exception {
//		String resource = (String) infoNode.getResult().getParam(ResultTypes.URL_TYPE);
//		if(resource != null) {
//			resource = StringEscapeUtils.escapeHtml(resource);
//			String source = "http://wikipedia.org/wiki/" + resource.substring(resource.toString().lastIndexOf("/") + 1);
//			if(source != null) {
//				out.print("<div style=\"height:10px;\" ><a target=\"_blank\" style=\"float:right;font-size:smaller;\" href=\"" + source + "\"><span>" + source + "</span></a></div>");
//			}
//		}		
//		out.print(endHTML);
	}
	
	@Override
	public boolean canRender(IContext context, InfoNode infoNode) throws Exception {
		IResult result = infoNode.getResult();
		
		if(result == null)
			return false;
		
		// checking value
		Object value = result.getParam(Properties.RESOURCE);
		if(value!= null) {
			String stringValue = value.toString();
			String normalizedStringValue = stringValue.toLowerCase();
			if(normalizedStringValue.contains("category:"))
				return false;
		}
		
		return super.canRender(context, infoNode);
	}
}
