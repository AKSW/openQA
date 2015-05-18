package org.aksw.openqa.examples.example2;

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
import org.aksw.openqa.examples.HelloWorldParameters;

public class HelloWorldInterpreter extends AbstractInterpreter {
	
	String[][] allwedQuestions= {
			// standard greetings
			{"hi", "hello", "hola", "ola", "howdy"},
			// question greetings
			{"how are you", "how r you", "how r u", "how are u"}
	};
	
	public HelloWorldInterpreter() {
		super(null /* there is no params to be passed */);
	}

	public HelloWorldInterpreter(Map<String, Object> params) {
		super(params);
	}
	
	@Override
	public String getVersion() {
		return "sample2"; // version
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
		return param.contains(Properties.Literal.TEXT, String.class);
	}

	@Override
	public List<? extends IResult> process(IParams param,
			ServiceProvider services, IContext context) throws Exception {
		
		String you = param.getParam(Properties.Literal.TEXT, String.class);
		
		Integer response = 0;
		
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
			
			int j = 0;
			while(response==0) {
				// always check the questions from the position j*2 of the allowedQuestions matrix
				if(inArray(you.toLowerCase(), allwedQuestions[j])) {
					response = j;
					break;
				}
				j++; // check next group of questions
				
				// check if the allowed questions vector is finished
				if(j*2==(allwedQuestions.length-1) && response == 0) {
					response = j; // if it is, we did not find any match
				}
			}
		} else
			response = 2; // end of matrix
		
		List<IResult> results = new ArrayList<>();
		Result result = new Result();
		result.setParam(HelloWorldParameters.Property.MATRIX_LINE, response);
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
