package org.aksw.openqa.component.ui.render;

import org.aksw.openqa.component.IPlugin;
import org.aksw.openqa.component.context.IContext;


public interface InfoGraphRender extends IRender, IPlugin {
	public boolean canRender(IContext context, InfoNode node) throws Exception;
}