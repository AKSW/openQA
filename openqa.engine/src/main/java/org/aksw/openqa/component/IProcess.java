package org.aksw.openqa.component;

import java.util.List;

import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.providers.impl.ServiceProvider;

public interface IProcess {	
	List<? extends IResult> process(List<? extends IParams> params, ServiceProvider services, IContext context) throws Exception;
}
