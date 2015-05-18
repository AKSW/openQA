package org.aksw.openqa.component.answerformulation.interpreter.impl;

import java.util.Map;

import org.aksw.openqa.component.answerformulation.AbstractInterpreterFactory;
import org.aksw.openqa.component.answerformulation.Interpreter;

public class SinaInterpreterFactory extends AbstractInterpreterFactory {
	@Override
	public Interpreter create(Map<String, Object> params) {
		return create(SinaInterpreter.class, params);
	}
}
