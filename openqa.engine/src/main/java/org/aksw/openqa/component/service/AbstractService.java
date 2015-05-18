package org.aksw.openqa.component.service;

import java.util.Map;

import org.aksw.openqa.component.AbstractPlugin;
import org.aksw.openqa.component.IPluginVisitor;

public abstract class AbstractService extends AbstractPlugin implements IService {

	public AbstractService(Map<String, Object> params) {
		super(params);
	}
	
	public void visit(IPluginVisitor visitor) {
		visitor.visit(this);
	}
	
}