package org.aksw.openqa.component.context;

import java.util.Map;

import org.aksw.openqa.component.AbstractPlugin;
import org.aksw.openqa.component.IPluginVisitor;

public abstract class AbstractContext extends AbstractPlugin implements IContext {

	public AbstractContext(Map<String, Object> params) {
		super(params);
	}
	
	public void visit(IPluginVisitor visitor) {
		visitor.visit(this);
	}
}
