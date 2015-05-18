package org.aksw.openqa.component.ui.render.impl;

import java.util.Map;

import org.aksw.openqa.component.ui.render.AbstractRenderFactory;
import org.aksw.openqa.component.ui.render.InfoGraphRender;

public class DBpediaSPARQLQueryRenderFactory extends AbstractRenderFactory {
	@Override
	public InfoGraphRender create(Map<String, Object> params) {
		return create(DBpediaSPARQLQueryRender.class, params);
	}
}
