package org.aksw.openqa.view.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.aksw.openqa.component.providers.impl.RenderProvider;

@ManagedBean(name="renderViewController")
@ViewScoped
public class RenderViewController extends ComponentViewController implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8671083252311393740L;

	public RenderViewController() {
		super(RenderProvider.class);
	}
}
