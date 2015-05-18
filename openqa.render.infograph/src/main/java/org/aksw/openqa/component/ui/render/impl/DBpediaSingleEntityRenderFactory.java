package org.aksw.openqa.component.ui.render.impl;

import java.util.Map;

import org.aksw.openqa.component.ui.render.AbstractRenderFactory;
import org.aksw.openqa.component.ui.render.InfoGraphRender;

public class DBpediaSingleEntityRenderFactory extends AbstractRenderFactory {
	@Override
	public InfoGraphRender create(Map<String, Object> params) {
		return create(DBpediaSingleEntityRender.class, params);
	}
}
