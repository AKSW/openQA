package org.aksw.openqa.examples.example3;

import java.util.List;

import org.aksw.openqa.Properties;
import org.aksw.openqa.QAEngineProcessor;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.main.QueryResult;
import org.aksw.openqa.manager.plugin.PluginManager;

public class HelloWorldMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String question = "how are you";
		String answer = "";
		if(args.length > 0)
			question  = args[0];
		
		System.out.println("You:\t" + question);
		
		ClassLoader contextClassLoader = HelloWorldMain.class.getClassLoader();    	
    	PluginManager pluginManager = new PluginManager(contextClassLoader);
    	pluginManager.setActive(true);
    	
    	QAEngineProcessor queryProcessor = new QAEngineProcessor(pluginManager);
    	
    	QueryResult result;
		result = queryProcessor.process(question);
		List<? extends IResult> output = result.getOutput();
		if(output.size() > 0)
			answer = output.get(0).getParam(Properties.Literal.TEXT, String.class);
		
		System.out.println("openQA:\t" + answer);		
	}

}
