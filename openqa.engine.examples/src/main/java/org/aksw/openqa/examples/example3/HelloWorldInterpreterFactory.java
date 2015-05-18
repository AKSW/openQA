package org.aksw.openqa.examples.example3;

import java.util.Map;

import org.aksw.openqa.component.answerformulation.AbstractInterpreterFactory;
import org.aksw.openqa.component.answerformulation.Interpreter;

public class HelloWorldInterpreterFactory extends AbstractInterpreterFactory {

	@Override
	public Interpreter create(Map<String, Object> params) {		
		return create(HelloWorldInterpreter.class, params);
	}

}
