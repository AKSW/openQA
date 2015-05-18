package org.aksw.openqa.component.answerformulation.synthesizer.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aksw.openqa.Properties;
import org.aksw.openqa.component.answerformulation.AbstractSynthesizer;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.object.IParams;
import org.aksw.openqa.component.object.IResult;
import org.aksw.openqa.component.object.Result;
import org.aksw.openqa.component.providers.impl.ServiceProvider;

public class DefaultRDFTermSynthesizer extends AbstractSynthesizer {

	
	public DefaultRDFTermSynthesizer(Map<String, Object> params)
			throws IOException {
		super(params);
	}

	Comparator<Entry<String, Candidate>> comparator = new Comparator<Entry<String, Candidate>>() {
	    public int compare(Entry<String, Candidate> e1, Entry<String, Candidate> e2) {
	    	Candidate c1 = e1.getValue();
	    	Candidate c2 = e2.getValue();
	        return c2.incidence - c1.incidence; // measuring the incidence
	    }
	};
	
	@Override
	public boolean canProcess(IParams param) {
		return param.contains(Properties.URI) ||
						param.contains(Properties.Literal.NUMBER) ||
								param.contains(Properties.Literal.DATE) ||
										param.contains(Properties.Literal.BOOLEAN) ||
												param.contains(Properties.Literal.TEXT) ||
												param.contains(Properties.RESOURCE);
	}
	
	@Override
	public java.util.List<? extends IResult> process(java.util.List<? extends IParams> inputParams,	ServiceProvider services, 
			IContext context) throws Exception {
		Map<String, Candidate>  instanceIncidence = new HashMap<String, Candidate>();
		for(IParams param : inputParams) {
			String attrValue = null;
			String attrKey = null;
			if(param.contains(Properties.URI)) {
				attrValue = (String) param.getParam(Properties.URI);
				attrKey = Properties.URI;
			} else if (param.contains(Properties.Literal.NUMBER))
				attrValue = param.getParam(Properties.Literal.NUMBER).toString();
			else if (param.contains(Properties.Literal.DATE))
				attrValue = (String) param.getParam(Properties.Literal.DATE);
			else if (param.contains(Properties.Literal.BOOLEAN))
				attrValue = (String) param.getParam(Properties.Literal.BOOLEAN);
			else if (param.contains(Properties.Literal.TEXT))
				attrValue = (String) param.getParam(Properties.Literal.TEXT);
			else {
				attrValue = (String) param.getParam(Properties.RESOURCE);
			}
			
			// there is no check for null, once canProcess assure that there is the attribute

			if(attrKey == Properties.URI) {
				attrValue = attrValue.substring(attrKey.lastIndexOf("/") + 1);
			}
			
			if(instanceIncidence.containsKey(attrValue)) {
				Candidate candidate = instanceIncidence.get(attrValue);
				candidate.incidence++;
				instanceIncidence.put(attrValue, candidate);
			} else {
				Candidate candidate = new Candidate();
				candidate.param = param;
				candidate.incidence++;
				instanceIncidence.put(attrValue, candidate);
			}		
		}
		
		List<Entry<String, Candidate>> intanceIncidenceList = new ArrayList<Entry<String, Candidate>>(instanceIncidence.entrySet());
		Collections.sort(intanceIncidenceList, comparator);
		List<IResult> results = new ArrayList<IResult>();
		for(Entry<String, Candidate> entry: intanceIncidenceList) {			
			Candidate candidate = entry.getValue();
			IParams inputParam = candidate.param;
			Result result = new Result(inputParam.getParameters(), inputParam, this);
			results.add(result);
		}
		
		return results;
	}

	private class Candidate {
		IParams param;
		int incidence = 0;
	}

	@Override
	public List<? extends IResult> process(IParams param,
			ServiceProvider services, IContext context) throws Exception {
		List<IResult> results = new ArrayList<IResult>();
		Result result = new Result(param.getParameters());
		results.add(result);
		return results;
	}
	
}
