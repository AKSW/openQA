package org.aksw.openqa.component.answerformulation;

import java.io.IOException;
import java.util.Map;

import org.aksw.openqa.component.AbstractQFProcessor;
import org.aksw.openqa.component.IPluginVisitor;

public abstract class AbstractResolver extends AbstractQFProcessor implements IResolver {

	public AbstractResolver(Map<String, Object> params) throws IOException {
		super(params);
	}

	public void visit(IPluginVisitor visitor) {
		visitor.visit(this);
	}
}
