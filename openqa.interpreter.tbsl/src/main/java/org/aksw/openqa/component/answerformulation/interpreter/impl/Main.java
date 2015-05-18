package org.aksw.openqa.component.answerformulation.interpreter.impl;

import java.io.IOException;

import org.aksw.autosparql.tbsl.algorithm.learning.NoTemplateFoundException;
import org.aksw.autosparql.tbsl.algorithm.learning.TbslDbpedia;
import org.aksw.autosparql.tbsl.algorithm.learning.TemplateInstantiation;
import org.json.JSONException;


public class Main {

	/**
	 * @param args
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws NoTemplateFoundException 
	 */
	public static void main(String[] args) throws IOException, JSONException, NoTemplateFoundException {
//		TBSLInterpreter intr = new TBSLInterpreter();
//		System.out.println(intr.getDescription());
		
		String question = "Which caves have more than 3 entrances";
		
//		String question = "Give me all movies starred by Brad Pitt.";
		TemplateInstantiation ti = TbslDbpedia.INSTANCE.answerQuestion(question);
		System.out.println(ti.getQuery());
//		
//		JSONObject object = JSONUtil.read("http://dbpedia.org/data/Tested.json");
//		JSONObject resourceAttr = object.getJSONObject("http://dbpedia.org/resource/Tested");
//		org.json.JSONArray attributes = resourceAttr.getJSONArray("http://dbpedia.org/property/name");
//		System.out.println(attributes.getJSONObject(0).get("value"));
	}

}
