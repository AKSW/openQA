package org.aksw.openqa.component.object;

import org.aksw.openqa.component.IComponent;

public class Params extends AbstractObject implements IParams {

	IComponent source;
	
	@Override
	public IComponent getSource() {
		return source;
	}
	
	public void setSource(IComponent source) {
		this.source = source;
	}
}
