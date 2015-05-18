package org.aksw.openqa.examples.example1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aksw.openqa.Properties;
import org.aksw.openqa.component.answerformulation.AbstractInterpreter;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.object.Result;
import org.aksw.openqa.component.providers.impl.ServiceProvider;

public class HelloWorldInterpreter extends AbstractInterpreter {
	
	String[][] openQA= {
			// standard greetings
			{"hi", "hello", "hola", "ola", "howdy"},
			{"hi", "hello", "hey"},
			// question greetings
			{"how are you", "how r you", "how r u", "how are u"},
			{"good", "doing well", "hey"},
			// default
			{"shut up", "you're bad", "noob", "stop talking", "openQA is unavailable, due to LOL"}
	};
	
	public HelloWorldInterpreter() {
		super(null /* there is no params to be passed */);
	}

	public HelloWorldInterpreter(Map<String, Object> params) {
		super(params);
	}
	
	@Override
	public String getVersion() {
		return "sample1"; // version
	}
	
	@Override
	public String getLabel() {
		return "HelloWorldInterpreter"; // the label
	}
	
	@Override
	public String getId() {
		return getLabel() + " " + getVersion(); // id = label + version
	}
	
	@Override
	public boolean canProcess(IParams param) {
		return param.contains(Properties.Literal.TEXT);
	}

	@Override
	public List<? extends IResult> process(IParams param,
			ServiceProvider services, IContext context) throws Exception {
		
		String you = (String) param.getParam(Properties.Literal.TEXT);	
		String answer = "";

		int response = 0; // default response 2
		
		if(!you.isEmpty()) {
		
			// eliminating white spaces
			you.trim();
			
			// removing punctuation (.,!,?)
			while(
					you.charAt(you.length()-1) == '.' ||
					you.charAt(you.length()-1) == '!' ||
					you.charAt(you.length()-1) == '?'
					) {
				you = you.substring(0, you.length()-1);
			}
			
			// eliminating white spaces, again :)
			you.trim();
			while(((response*2)+1)< openQA.length) {
				// always check the questions from the position j*2 of the openQA matrix
				if(inArray(you.toLowerCase(), openQA[response*2])) {
					// get one of the answers from the position j*2+1 of the matched questions
					int r = (int) Math.floor(Math.random() *  openQA[(response*2)+1].length);
					
					// print the answer
					answer = openQA[(response*2)+1][r];
					break; // interrupt the interaction
				}
				response++; // check next group of questions
			}
		} else
			response = 2;
		
		if(response == 2) { // if we did not find any math, answer default answers
			// get one of the answers from the position j*2+1 of the matched questions
			int r = (int) Math.floor(Math.random() *  openQA[(response*2)].length);
			
			// print the answer
			answer = openQA[response*2][r];
		}
		
		List<IResult> results = new ArrayList<>();
		Result result = new Result();
		result.setParam(Properties.Literal.TEXT, answer);
		results.add(result);
		
		return results; // since we already consume the parameter, return null
	}
	
	/**
	 * Return true if there is any sentence in the array that match the given input 
	 * string or false otherwise.
	 * 
	 * @param in input string.
	 * @param str an array containing am array of sentences (questions).
	 * @return true if there is any sentence in the array that match the given input string.
	 */
	private boolean inArray(String in, String[] str) {
		for(int i = 0;i<str.length;i++) {
			if(str[i].equals(in))
				return true;
		}		
		return false;
	}
}
