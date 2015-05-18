package org.aksw.openqa.component.ui.render;

import javax.servlet.ServletOutputStream;

import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.providers.impl.RenderProvider;

public interface IRender {
	public void render(RenderProvider renderProvider, InfoNode infoNode, IContext context, ServletOutputStream out) throws Exception;
}
