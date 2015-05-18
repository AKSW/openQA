package org.aksw.openqa.component.ui.render;

import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.aksw.openqa.Properties;
import org.aksw.openqa.component.AbstractPlugin;
import org.aksw.openqa.component.IPlugin;
import org.aksw.openqa.component.IPluginVisitor;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.providers.impl.RenderProvider;


public abstract class AbstractInfoGraphRender extends AbstractPlugin implements InfoGraphRender {
	
	public AbstractInfoGraphRender() {
	}
	
	public AbstractInfoGraphRender(Class<? extends IPlugin> c, Map<String, Object> params) {
		super(c, params);
	}
	
	@Override
	public void render(RenderProvider renderProvider, InfoNode result, IContext context, ServletOutputStream out) throws Exception {
		printStart(renderProvider, result, context, out);
		printContent(renderProvider, result, context, out);
		printEnd(renderProvider, result, context, out);
	}
	
	@Override
	public boolean canRender(IContext context, InfoNode node)
			throws Exception {
		IResult result = node.getResult();
		
		if(result == null)
			return false;		
		
		// default check
		return result.contains(Properties.URI) ||
				result.contains(Properties.Literal.NUMBER) ||
				result.contains(Properties.Literal.DATE) ||
				result.contains(Properties.Literal.BOOLEAN) ||
				result.contains(Properties.RESOURCE);
	}
	
	protected abstract void printStart(RenderProvider render, InfoNode result, IContext context, ServletOutputStream out) throws Exception;

	protected abstract void printEnd(RenderProvider render, InfoNode result, IContext context, ServletOutputStream out) throws Exception;

	protected abstract void printContent(RenderProvider render, InfoNode result, IContext context, ServletOutputStream out) throws Exception;
	
	public void visit(IPluginVisitor visitor) {
		visitor.visit(this);
	}
}
