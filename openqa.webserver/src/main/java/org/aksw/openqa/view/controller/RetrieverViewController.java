package org.aksw.openqa.view.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.aksw.openqa.component.providers.impl.RetrieverProvider;

@ManagedBean(name="kbConsultingViewController")
@ViewScoped
public class RetrieverViewController extends ComponentViewController implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2260026837025370659L;

	public RetrieverViewController() {
		super(RetrieverProvider.class);
	}
}