package org.aksw.openqa.component.ui.render;

import java.util.ArrayList;
import java.util.List;

import org.aksw.openqa.component.object.IResult;

public class InfoNode {

	private List<InfoNode> infos;
	protected double relevance = 0;
	protected IResult result = null;
	protected List<String> tags = null;

	public IResult getResult() {
		return result;
	}

	public void setResult(IResult result) {
		this.result = result;
	}

	public double getRelevance() {
		return relevance;
	}

	public void setRelevance(double relevance) {
		this.relevance = relevance;
	}
	
	public void addInfo(InfoNode info) {
		if(infos == null)
			infos = new ArrayList<InfoNode>();
		infos.add(info);
	}
	
	public List<InfoNode> getInfoList() {
		return infos;
	}
}
