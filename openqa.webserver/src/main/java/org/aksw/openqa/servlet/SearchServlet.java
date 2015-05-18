package org.aksw.openqa.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aksw.openqa.Properties;
import org.aksw.openqa.Status;
import org.aksw.openqa.SystemVariables;
import org.aksw.openqa.component.answerformulation.AnswerFormulation;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.context.impl.DefaultContext;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.providers.impl.ContextProvider;
import org.aksw.openqa.component.providers.impl.RenderProvider;
import org.aksw.openqa.component.ui.render.InfoGraphRender;
import org.aksw.openqa.component.ui.render.InfoNode;
import org.aksw.openqa.manager.plugin.PluginManager;
import org.aksw.openqa.util.HTTPUtil;
import org.apache.log4j.Logger;

@WebServlet(name = "search", urlPatterns = { "/search" })
public class SearchServlet extends HttpServlet {

	public static final String OUTPUT_FORMAT_PARAM = "format";
	public final static String OUTPUT_CONTENT_PARAM = "content";
	public final static String OUTPUT_CONTENT_PARAM_TYPE_SPARQL = "sparql";
	public final static String INPUT_QUERY_PARAM = "q";

	/**
	 * 
	 */
	private static final long serialVersionUID = -5755833016211618969L;
	private static Logger logger = Logger.getLogger(SearchServlet.class);
	private PluginManager pluginManager;

	@Override
	public void init() throws ServletException {
		PluginManager pluginManager = ((PluginManager) SystemVariables
				.getInstance().getParam(SystemVariables.PLUGIN_MANAGER));
		this.pluginManager = pluginManager;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("Starting a new Search");

		Map<String, Object> servletParams = new HashMap<String, Object>();
		Enumeration<String> parameters = req.getParameterNames();
		while (parameters.hasMoreElements()) {
			String parameterName = parameters.nextElement();
			String parameter = req.getParameter(parameterName);
			servletParams.put(parameterName, parameter);
			logger.info("Processing param: " + parameterName + " = "
					+ parameter);
		}

		String host = req.getRemoteHost();
		String ip = HTTPUtil.getClientIpAddr(req);
		String user = req.getRemoteUser();
		logger.debug("User ip :" + ip);

		// setting up the context
		DefaultContext context = null;
		try {
			context = pluginManager.getPlugin(ContextProvider.class,
					DefaultContext.class);
			context.setParam(IContext.REQUEST_HOST, host);
			context.setParam(IContext.REQUEST_IP, ip);
			context.setParam(IContext.REQUEST_USER, user);
			context.setParam(IContext.REQUEST, req);

			resp.setContentType("text/html");
			ServletOutputStream out = resp.getOutputStream();

			// String format = (String)
			// servletParams.get(SearchServlet.OUTPUT_FORMAT_PARAM);
			String outputAbs = (String) servletParams
					.get(SearchServlet.OUTPUT_CONTENT_PARAM);
			String inputQuery = (String) servletParams
					.get(SearchServlet.INPUT_QUERY_PARAM);

			// create the input params
			Map<String, Object> inputParams = new HashMap<String, Object>();
			if (inputQuery != null) {
				if (context != null)
					context.setInputQuery(inputQuery);
				inputParams.put(Properties.Literal.TEXT,
						inputQuery); // add the input query
			}
			boolean skipRetrievalStage = false;
			// skip the retrieval stage in case that a SPARQL query must be
			// render
			if (outputAbs != null
					&& outputAbs
							.equals(SearchServlet.OUTPUT_CONTENT_PARAM_TYPE_SPARQL)) {
				skipRetrievalStage = true;
			}

			// increasing number of queries
			int numberOfQueries = Status.getNumberOfQueries() + 1;
			Status.setNumberOfQueries(numberOfQueries);

			// render
			InfoNode node = new InfoNode();
			try {
				AnswerFormulation inputProcessor = new AnswerFormulation();
				List<? extends IResult> resolved = inputProcessor.process(
						skipRetrievalStage, inputParams, pluginManager,
						context);
				if (resolved.size() == 0) {
					// increasing number of queries without result
					int numberOfQueriesWithoutResult = Status
							.getQueriesWithourResult() + 1;
					Status.setQueriesWithourResult(numberOfQueriesWithoutResult);
				}
				logger.debug("Rendering result");
				for (IResult result : resolved) {
					InfoNode childNode = new InfoNode();
					childNode.setResult(result);
					node.addInfo(childNode);
				}
			} catch (Exception e) {
				int numberOfErrors = Status.getNumberOfErrors() + 1;
				Status.setNumberOfErrors(numberOfErrors);
				logger.error("Error processing params", e);
			}

			// getting the main render
			RenderProvider renderProvider = (RenderProvider) pluginManager
					.getProvider(RenderProvider.class);
			InfoGraphRender mainRender = renderProvider.getRootRender();
			if (mainRender == null)
				throw new Exception(
						"The interface should have at least one Main Render Active");
			mainRender.render(renderProvider, node, context, out);
		} catch (Exception e) {
			int numberOfErrors = Status.getNumberOfErrors() + 1;
			Status.setNumberOfErrors(numberOfErrors);
			logger.error("Error processing params", e);
		}
	}
}
