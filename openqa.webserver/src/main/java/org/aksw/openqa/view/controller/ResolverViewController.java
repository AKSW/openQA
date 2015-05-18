package org.aksw.openqa.view.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.aksw.openqa.component.providers.impl.ResolverProvider;

@ManagedBean(name="resolverViewController")
@ViewScoped
public class ResolverViewController extends ComponentViewController implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4847640287632458740L;

	public ResolverViewController() {
		super(ResolverProvider.class);
	}
}

