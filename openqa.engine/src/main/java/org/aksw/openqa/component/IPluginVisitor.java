package org.aksw.openqa.component;

import org.aksw.openqa.component.answerformulation.Interpreter;
import org.aksw.openqa.component.answerformulation.IResolver;
import org.aksw.openqa.component.answerformulation.IRetriever;
import org.aksw.openqa.component.answerformulation.ISynthesizer;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.service.IService;
import org.aksw.openqa.component.ui.render.InfoGraphRender;


public interface IPluginVisitor {	
	public void visit(IService service);	
	public void visit(Interpreter interpreter);	
	public void visit(IRetriever retriever);
	public void visit(ISynthesizer synthesizer);	
	public void visit(IResolver resolver);	
	public void visit(InfoGraphRender render);	
	public void visit(IContext context);
}
