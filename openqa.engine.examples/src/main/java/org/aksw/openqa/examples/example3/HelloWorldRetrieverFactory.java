package org.aksw.openqa.examples.example3;

import java.util.Map;

import org.aksw.openqa.component.answerformulation.AbstractRetrieverFactorySpi;
import org.aksw.openqa.component.answerformulation.IRetriever;

public class HelloWorldRetrieverFactory extends AbstractRetrieverFactorySpi {

	@Override
	public IRetriever create(Map<String, Object> params) {		
		return create(HelloWorldRetriever.class, params);
	}

}
