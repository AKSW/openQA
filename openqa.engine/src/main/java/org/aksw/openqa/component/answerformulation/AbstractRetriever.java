package org.aksw.openqa.component.answerformulation;

import java.util.Map;

import org.aksw.openqa.component.AbstractQFProcessor;
import org.aksw.openqa.component.IPluginVisitor;

public abstract class AbstractRetriever extends AbstractQFProcessor implements IRetriever {

	public AbstractRetriever(Map<String, Object> params) {
		super(params);
	}

	public void visit(IPluginVisitor visitor) {
		visitor.visit(this);
	}
}
