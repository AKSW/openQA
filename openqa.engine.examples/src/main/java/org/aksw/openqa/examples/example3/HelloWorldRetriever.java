package org.aksw.openqa.examples.example3;

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

	public HelloWorldRetriever(Map<String, Object> params) {
		super(params);
	}
	
	@Override
	public boolean canProcess(IParams param) {
		// check if the argument have the required attribute
		return param.contains(HelloWorldParameters.Property.MATRIX_LINE, HelloWorldParameter.class);
	}

	@Override
	public List<? extends IResult> process(IParams param,
			ServiceProvider services, IContext context) throws Exception {
		HelloWorldParameter helloWorldParameter = param.getParam(HelloWorldParameters.Property.MATRIX_LINE, HelloWorldParameter.class);
		int response = helloWorldParameter.getMatrixLine();
		
		int r = (int) Math.floor(Math.random() *  answers[response].length);
		
		// print the answer
		String answer = answers[response][r];
		
		List<IResult> results = new ArrayList<>();
		Result result = new Result();
		result.setParam(Properties.Literal.TEXT, answer);
		results.add(result);
		
		return results; // since we already consume the parameter, return null
	}
}
