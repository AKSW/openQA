package org.aksw.openqa.component.providers.impl;

import java.util.List;

import javax.servlet.ServletOutputStream;

import org.aksw.openqa.component.AbstractPluginProvider;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.ui.render.InfoGraphRender;
import org.aksw.openqa.component.ui.render.InfoNode;
import org.aksw.openqa.component.ui.render.IRender;
import org.aksw.openqa.component.ui.render.IRenderFactory;

public class RenderProvider extends AbstractPluginProvider<InfoGraphRender, IRenderFactory> implements IRender {
	
	public RenderProvider(List<? extends ClassLoader> classLoaders, ServiceProvider serviceProvider) {
		super(IRenderFactory.class, classLoaders, serviceProvider);
	}
	
	public RenderProvider() {		
	}
	
	public void render(RenderProvider renderProvider, InfoNode infoNode,
			IContext runnerParams, ServletOutputStream out) throws Exception {
		List<InfoGraphRender> infoGraphs = renderProvider.list();
		for(InfoGraphRender infoGraph : infoGraphs) {
			if(infoGraph.isActive() && infoGraph.canRender(runnerParams, infoNode) && !infoGraph.contains("MAIN"))
				infoGraph.render(renderProvider, infoNode, runnerParams, out);
		}
	}

	public InfoGraphRender getRootRender() {
		List<InfoGraphRender> infoGraphs = list();
		for(InfoGraphRender infoGraph : infoGraphs) {
			if(infoGraph.isActive() && infoGraph.contains("MAIN"))
				return infoGraph;
		}
		return null;
	}
}
