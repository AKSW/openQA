package org.aksw.openqa.component.context.impl;

import java.util.Map;

import org.aksw.openqa.component.context.AbstractContextFactorySpi;
import org.aksw.openqa.component.context.IContext;

public class DefaultContextServiceFactory extends AbstractContextFactorySpi {
	@Override
	public IContext create(Map<String, Object> params) {
		return create(DefaultContext.class, params);
	}
}
