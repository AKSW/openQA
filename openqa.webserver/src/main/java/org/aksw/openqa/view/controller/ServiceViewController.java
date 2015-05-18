package org.aksw.openqa.view.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.aksw.openqa.component.providers.impl.ServiceProvider;

@ManagedBean(name="serviceViewController")
@ViewScoped
public class ServiceViewController extends ComponentViewController implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4847640287632458740L;

	public ServiceViewController() {
		super(ServiceProvider.class);
	}
}
