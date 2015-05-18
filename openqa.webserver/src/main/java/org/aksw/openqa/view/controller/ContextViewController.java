package org.aksw.openqa.view.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.aksw.openqa.component.providers.impl.ContextProvider;

@ManagedBean(name="contextViewController")
@ViewScoped
public class ContextViewController extends ComponentViewController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9126175686356935535L;

	public ContextViewController() {
		super(ContextProvider.class);
	}
}
