package org.aksw.openqa.component.answerformulation.retriever.impl;

import java.util.Map;

import org.aksw.openqa.component.answerformulation.AbstractRetrieverFactorySpi;
import org.aksw.openqa.component.answerformulation.IRetriever;

public class TripleStoreRetrieverFactory extends AbstractRetrieverFactorySpi {
	@Override
	public IRetriever create(Map<String, Object> params) {
		return create(TripleStoreRetriever.class, params);
	}
}
