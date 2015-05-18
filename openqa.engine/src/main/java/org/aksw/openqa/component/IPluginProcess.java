package org.aksw.openqa.component;

import java.util.List;

import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.providers.impl.ServiceProvider;

public interface IPluginProcess extends IPlugin, IProcess {
	public boolean canProcess(List<? extends IParams> params);
	public boolean canProcess(IParams param);	
	public List<? extends IResult> process(IParams param, ServiceProvider services, IContext context) throws Exception;
}