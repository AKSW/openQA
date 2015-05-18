package org.aksw.openqa.component.object;

import java.util.Map;

import org.aksw.openqa.component.IComponent;

public class Result extends AbstractResult {
		
	protected IComponent source;
	protected IParams inputParam;

	public Result() {
	}
	
	public Result(Map<String, Object> params, IParams inputParam, IComponent source) {
		// setting the source
		this.source = source;
		this.inputParam = inputParam;
		setProperties(params);
	}
	
	public Result(Map<String, Object> params) {
		// setting the source
		setProperties(params);
	}
	
	public Result(IParams inputParam, IComponent source) {
		// setting the source
		this.source = source;
		this.inputParam = inputParam;
	}

	@Override
	public IComponent getSource() {
		return source;
	}

	@Override
	public IParams getInputParam() {
		return inputParam;
	}
}
