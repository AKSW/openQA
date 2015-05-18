package org.aksw.openqa.examples.example2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aksw.openqa.Properties;
import org.aksw.openqa.component.answerformulation.AbstractRetriever;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.object.Result;
import org.aksw.openqa.component.providers.impl.ServiceProvider;
import org.aksw.openqa.examples.HelloWorldParameters;

public class HelloWorldRetriever extends AbstractRetriever {

	String[][] answers= {
			// standard greetings
			{"hi", "hello", "hey"},
			// question greetings
			{"good", "doing well", "hey"},
			// default
			{"shut up", "you're bad", "noob", "stop talking", "openQA is unavailable, due to LOL"}
	};
	
	public HelloWorldRetriever() {
		super(null);
	}
	

	public HelloWorldRetriever(Map<String, Object> params) {
		super(params);
	}
	
	@Override
	public String getVersion() {
		return "sample2"; // version
	}
	
	@Override
	public String getLabel() {
		return "HelloWorldRetriever"; // the label
	}

	@Override
	public boolean canProcess(IParams param) {
		// check if the argument have the required attribute
		return param.contains(HelloWorldParameters.Property.MATRIX_LINE, Integer.class);
	}

	@Override
	public List<? extends IResult> process(IParams param,
			ServiceProvider services, IContext context) throws Exception {
		int respose = param.getParam(HelloWorldParameters.Property.MATRIX_LINE, Integer.class);
		
		int r = (int) Math.floor(Math.random() *  answers[respose].length);
		
		// print the answer
		String answer = answers[respose][r];
		
		List<IResult> results = new ArrayList<>();
		Result result = new Result();
		result.setParam(Properties.Literal.TEXT, answer);
		results.add(result);
		
		return results; // since we already consume the parameter, return null
	}
}
