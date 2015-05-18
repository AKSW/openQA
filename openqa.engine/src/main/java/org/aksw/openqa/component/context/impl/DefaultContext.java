package org.aksw.openqa.component.context.impl;

import java.util.Map;

import org.aksw.openqa.component.context.AbstractContext;

public class DefaultContext extends AbstractContext {

	String inputQuery;
	
	public DefaultContext(Map<String, Object> params) {
		super(params);
	}

	@Override
	public String getInputQuery() {
		return inputQuery;
	}
	
	public void setInputQuery(String inputQuery) {
		this.inputQuery = inputQuery;
	}

}
