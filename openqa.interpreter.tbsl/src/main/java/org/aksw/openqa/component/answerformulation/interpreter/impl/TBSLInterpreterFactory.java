package org.aksw.openqa.component.answerformulation.interpreter.impl;

import java.util.Map;

import org.aksw.openqa.component.answerformulation.AbstractInterpreterFactory;
import org.aksw.openqa.component.answerformulation.Interpreter;

public class TBSLInterpreterFactory extends AbstractInterpreterFactory {
	@Override
	public Interpreter create(Map<String, Object> params) {
		return create(TBSLInterpreter.class, params);
	}
}
