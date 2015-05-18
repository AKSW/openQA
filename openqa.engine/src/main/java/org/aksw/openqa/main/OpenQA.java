package org.aksw.openqa.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.aksw.openqa.Properties;
import org.aksw.openqa.QAEngineProcessor;
import org.aksw.openqa.component.IPlugin;
import org.aksw.openqa.component.IPluginVisitor;
import org.aksw.openqa.component.answerformulation.IResolver;
import org.aksw.openqa.component.answerformulation.IRetriever;
import org.aksw.openqa.component.answerformulation.ISynthesizer;
import org.aksw.openqa.component.answerformulation.Interpreter;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.context.impl.DefaultContext;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.providers.impl.ContextProvider;
import org.aksw.openqa.component.providers.impl.InterpreterProvider;
import org.aksw.openqa.component.providers.impl.RetrieverProvider;
import org.aksw.openqa.component.providers.impl.ServiceProvider;
import org.aksw.openqa.component.providers.impl.SynthesizerProvider;
import org.aksw.openqa.component.service.IService;
import org.aksw.openqa.component.ui.render.InfoGraphRender;
import org.aksw.openqa.manager.plugin.PluginManager;
import org.aksw.openqa.qald.schema.Answer;
import org.aksw.openqa.qald.schema.Answers;
import org.aksw.openqa.qald.schema.Dataset;
import org.aksw.openqa.qald.schema.Question;
import org.aksw.openqa.util.JAXBUtil;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class OpenQA {
	
	private static Logger logger = Logger.getLogger(OpenQA.class);
	
	private static final String OUT_OF_SCOPE_MESSAGE = " OUT OF SCOPE ";
	
	public Dataset evaluate(File qaldTest, String lang, PluginManager pluginManager) throws ParserConfigurationException, SAXException, IOException, JAXBException {

		Dataset dataset = JAXBUtil.deserialize(Dataset.class, qaldTest);
		
		ContextProvider contextProvider = (ContextProvider) pluginManager.getProvider(ContextProvider.class);
		ServiceProvider serviceProvider = (ServiceProvider) pluginManager.getProvider(ServiceProvider.class);
		InterpreterProvider interpreterProvider = (InterpreterProvider) pluginManager.getProvider(InterpreterProvider.class);
		RetrieverProvider retrieverProvider = (RetrieverProvider) pluginManager.getProvider(RetrieverProvider.class);
		SynthesizerProvider synthesizerProvider = (SynthesizerProvider) pluginManager.getProvider(SynthesizerProvider.class);		
		QAEngineProcessor queryProcessor = new QAEngineProcessor(contextProvider, serviceProvider, interpreterProvider, 
				retrieverProvider, synthesizerProvider);
				
		// setting up the context
		IContext context = contextProvider.get(IContext.class);
		
		context.setParam(IContext.REQUEST_HOST, "localhost");
		
		logger.debug("Running tests for dataset " + dataset.getId());
		
		List<Question> questions = dataset.getQuestion();
		
		for(Question question : questions) {
			List<org.aksw.openqa.qald.schema.String> stringElements = question.getString();
			for(org.aksw.openqa.qald.schema.String query : stringElements) {
				if(lang == null || query.getLang().equals(lang)) {
					String queryValue = query.getValue();
					logger.debug("Running query: " + queryValue);
					try {
						IResult result = queryProcessor.process(queryValue);
						@SuppressWarnings("unchecked")
						List<IResult> interpreterResults = (List<IResult>) result.getParam(QueryResult.Attr.INTERPRETING_RESULT, ProcessResult.class).getOutput();
						String sparqlQueriesAnswer = null;
						if(interpreterResults != null && interpreterResults.size() > 0) {
							for(IResult interpreterResult : interpreterResults) {
								String sparqlParam = (String) interpreterResult.getParam(Properties.SPARQL);
								if(sparqlParam != null) {									
									if(sparqlQueriesAnswer != null)
										sparqlQueriesAnswer += sparqlParam + ";";
									else
										sparqlQueriesAnswer = sparqlParam + ";";
								}
							}
						}
						
						if(sparqlQueriesAnswer == null)
							sparqlQueriesAnswer = OpenQA.OUT_OF_SCOPE_MESSAGE;
						
						question.setQuery(sparqlQueriesAnswer);
						@SuppressWarnings("unchecked")
						List<IResult> entryResults = (List<IResult>) result.getParam(QueryResult.Attr.SYNTHESIS_RESULT, ProcessResult.class).getOutput();
						if(entryResults != null) {
							question.setAnswers(getAnswers(entryResults));
						}
					} catch (Exception e) {
						logger.error("Erro executing query: " + queryValue, e);
					}
				}
			}
		}
		
		return dataset;
	}
	
	public void answer(String question, PluginManager pluginManager) {
		logger.debug("Running query: " + question);
	
		ContextProvider contextProvider = (ContextProvider) pluginManager.getProvider(ContextProvider.class);
		ServiceProvider serviceProvider = (ServiceProvider) pluginManager.getProvider(ServiceProvider.class);
		InterpreterProvider interpreterProvider = (InterpreterProvider) pluginManager.getProvider(InterpreterProvider.class);
		RetrieverProvider retrieverProvider = (RetrieverProvider) pluginManager.getProvider(RetrieverProvider.class);
		SynthesizerProvider synthesizerProvider = (SynthesizerProvider) pluginManager.getProvider(SynthesizerProvider.class);
		// setting up the context
		IContext context = contextProvider.get(DefaultContext.class);
		if(context != null)
			context.setParam(IContext.REQUEST_HOST, "localhost");
		QAEngineProcessor queryProcessor = new QAEngineProcessor(contextProvider, serviceProvider, interpreterProvider,
				retrieverProvider, synthesizerProvider);
		QueryResult result;
		try {
			result = queryProcessor.process(question);
			List<? extends IResult> output = result.getOutput();
			String array = "";
			if(output != null && output.size() > 0) {
				array += "[";
				for(IResult outputEntry : output) {
					Map<String, Object> attrs = outputEntry.getParameters();
					array += "{"; // starting element
					String elementAttr = "";
					for(Entry<String, Object> attrEntry : attrs.entrySet()) {
						elementAttr += "\"" + attrEntry.getKey() + "\": \"" + attrEntry.getValue() + "\", ";
					}
					elementAttr = elementAttr.substring(0, elementAttr.length()-2); // remove last comma
					array += elementAttr + "}, "; // finishing element
				}
				array = array.substring(0, array.length()-2);
				array += "]";
				System.out.println(array);
			}
		} catch (Exception e) {
			logger.error("Error running query: " + question, e);
		}
	}
	
	public Answers getAnswers(List<IResult> entryResults) {
		Answers answers = new Answers();
		for(IResult entryrResult : entryResults) {
			String attrValue = null;
			String attrKey = null;
			if(entryrResult.contains(Properties.URI)) {
				attrValue = (String) entryrResult.getParam(Properties.URI);
				attrKey = Properties.URI;
			} else if (entryrResult.contains(Properties.Literal.NUMBER)) {
				attrValue = entryrResult.getParam(Properties.Literal.NUMBER).toString();
				attrKey = Properties.Literal.NUMBER;
			} else if (entryrResult.contains(Properties.Literal.DATE)) {
				attrValue = (String) entryrResult.getParam(Properties.Literal.DATE);
				attrKey = Properties.Literal.DATE;
			} else if (entryrResult.contains(Properties.Literal.BOOLEAN)) {
				attrValue = (String) entryrResult.getParam(Properties.Literal.BOOLEAN);
				attrKey = Properties.Literal.BOOLEAN;
			} else {
				attrValue = (String) entryrResult.getParam(Properties.RESOURCE);
				attrKey = Properties.RESOURCE;
			}
			
			Answer answer = null;
			if(attrKey ==  Properties.URI) {
				answer = new Answer();
				answer.setUri(attrValue);
			} else if(attrKey == Properties.Literal.DATE) {
				answer = new Answer();
				answer.setDate(attrValue);
			} else if(attrKey == Properties.Literal.NUMBER) {
				answer = new Answer();
				answer.setNumber(attrValue);
			} else if(attrKey == Properties.Literal.BOOLEAN) {
				answer = new Answer();
				answer.setBoolean(attrValue);
			} else {
				answer = new Answer();
				answer.setUri(attrValue);
			}			
			if(answer != null)
				answers.getAnswer().add(answer);
		}
		return answers;
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length < 2) {
			printHelp();
			return;
		}

		String args0 = args[0];
		if((args0.charAt(0) == '/') || (args0.charAt(0) == '\\'))
			args0 = args0.substring(1, args0.length());
		
		File pluginDir = new File(args0);
		
		if(!pluginDir.isDirectory()) {
			printHelp();
			return;
		}
		
		OpenQA openQA = new OpenQA();
		
		ClassLoader contextClassLoader = openQA.getClass().getClassLoader();
		PluginManager pluginManager = new PluginManager(pluginDir.getAbsolutePath(), contextClassLoader);
    	
    	String[] activateList = openQA.getActivateList(args);
    	if(activateList == null)
    		pluginManager.setActive(true);
    	else
    		pluginManager.setActive(true, activateList);
    	
    	String arg1 = args[1];
    	
		if(arg1.equals("-list") && (args.length == 2)) {
			printPlugins(pluginManager);
			return;
		} else if (arg1.equals("-info") && (args.length == 3)) {
			printInfo(pluginManager, args[2]);
		} else if(arg1.equals("-query") && (args.length == 3 || args.length == 5)) {
			openQA.answer(args[2], pluginManager);
			return;
		} else if(arg1.equals("-qald") && (args.length == 5 || args.length == 7)) {
			String arg2 = args[2];
			File qaldFile = new File(arg2);
	    	if(qaldFile.isFile()) {
	    		File outputFile = new File(args[3]);
	    		Dataset dataset = openQA.evaluate(qaldFile, args[4], pluginManager);
				FileOutputStream fout = new FileOutputStream(outputFile);
				JAXBUtil.serialize(Dataset.class, dataset, fout, "string keywords query");
	    	}			
			return;
		} else {
    		printHelp();
    	}
	}
	
	private static void printInfo(PluginManager pluginManager, final String pluginID) throws Exception {
		System.out.println("Info: ");
		pluginManager.visit(new IPluginVisitor() {
			
			@Override
			public void visit(IContext context) {
				print(context, pluginID);
			}
			
			@Override
			public void visit(InfoGraphRender render) {
				print(render, pluginID);
			}
			
			@Override
			public void visit(IResolver resolver) {
				print(resolver, pluginID);
			}
			
			@Override
			public void visit(ISynthesizer synthesizer) {
				print(synthesizer, pluginID);
			}
			
			@Override
			public void visit(IRetriever retriever) {
				print(retriever, pluginID);
			}
			
			@Override
			public void visit(Interpreter interpreter) {
				print(interpreter, pluginID);
			}
			
			@Override
			public void visit(IService service) {
				print(service, pluginID);
			}
		});
	}
	
	private static void printPlugins(PluginManager pluginManager) throws Exception {
		System.out.println("Plugins: ");
		pluginManager.visit(new IPluginVisitor() {
			
			@Override
			public void visit(IContext context) {
				print(context);			
			}
			
			@Override
			public void visit(InfoGraphRender render) {
				print(render);
			}
			
			@Override
			public void visit(IResolver resolver) {
				print(resolver);
			}
			
			@Override
			public void visit(ISynthesizer synthesizer) {
				print(synthesizer);
			}
			
			@Override
			public void visit(IRetriever retriever) {
				print(retriever);
			}
			
			@Override
			public void visit(Interpreter interpreter) {
				print(interpreter);
			}
			
			@Override
			public void visit(IService service) {
				print(service);
			}
		});
	}

	private static void print(IPlugin plugin) {
			System.out.println("    " + plugin.getId());
	}
	
	private static void print(IPlugin plugin, String pluginID) {
		if(pluginID.equals(plugin.getId())) {
			System.out.println("id: \t\t " + plugin.getId());
			System.out.println("label: \t\t " + plugin.getLabel());
			System.out.println("version: \t " + plugin.getVersion());
			System.out.println("API: \t\t " + plugin.getAPI());
			System.out.println("author: \t " + plugin.getAuthor());
			System.out.println("contact: \t " + plugin.getContact());
			System.out.println("website: \t " + plugin.getWebsite());
			System.out.println("license: \t " + plugin.getLicense());
			System.out.println("description: \t " + plugin.getDescription());
			System.out.println("input: \t\t " + plugin.getInput());
			System.out.println("output: \t " + plugin.getOutput());
		}
	}

	public String[] getActivateList(String[] args) {
		List<String> argumentList = Arrays.asList(args);
		int activateIndx = argumentList.indexOf("-activate");
		if(activateIndx == -1 || activateIndx+2 > argumentList.size())
			return null;
		
		String activeStringList = argumentList.get(activateIndx+1);
		return activeStringList.split(",");
	}
	
	public static void printHelp() {
		System.out.println("Use java -jar openqa.engine.jar <plug-insDir> [option]");
		System.out.println("Where  [option] is:");
		System.out.println("   * -query <query> -activate <activateList>");
		System.out.println("       e.g. java -jar openqa.engine.jar \\pluginsDir \"How to run openQA in a standalone fashion?\" -activate \"componentId1, compoenentId2\"");
		System.out.println("   * -qald <QALDtestfile> <outputfile> <lang> -activate <activateList>");
		System.out.println("       e.g. java -jar openqa.engine.jar \\pluginsDir QALD4.xml result.xml en -activate \"componentId1, compoenentId2\"");
		System.out.println("   * -list     - list all plug-ins in the given plug-ins directory");
		System.out.println("   * -info  <plug-inID>   - show the plug-in info");
		System.out.println("   * -compile  <plug-inDir> - generate an Plug-in Archive ('.par') of the given plug-in directory.");
		System.out.println("Dictionary: ");
		System.out.println("  plug-insDir		-	directory containing the plug-ins.");
		System.out.println("  plug-inDir	-	 directory of the plug-in.");
		System.out.println("  query		-	a cotated query (keywords or full question) e.g.\"Ho was the discoverer of Brazil?\".");
		System.out.println("  QALDTestFile		- QALD test file.");
		System.out.println("  outputFile		-	destination output file.");
		System.out.println("  lang			-	language to test (en,de,es,it,fr,nl,ro).");
		System.out.println("  -activate		-	list of plug-ins to be activated splited by comma (optional). e.g \"plug-inId1, plug-inId2\".");
	}
}