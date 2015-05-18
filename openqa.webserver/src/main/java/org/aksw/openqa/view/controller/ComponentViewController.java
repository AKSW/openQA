package org.aksw.openqa.view.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.aksw.openqa.SystemVariables;
import org.aksw.openqa.component.IPlugin;
import org.aksw.openqa.component.IProvider;
import org.aksw.openqa.manager.plugin.PluginManager;
import org.aksw.openqa.view.model.ViewComponentWrapper;
import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

public class ComponentViewController implements Serializable {
	
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(ComponentViewController.class);
	private static final long serialVersionUID = -345046386897842183L;
	private List<ViewComponentWrapper> components = new ArrayList<ViewComponentWrapper>();
	private ViewComponentWrapper selectedComponent;
	
	public ComponentViewController(Class<? extends IProvider<? extends IPlugin>> providerClass) {
		List<? extends IPlugin> components;
		try {
			components = ((PluginManager)(SystemVariables.getInstance().getParam(SystemVariables.PLUGIN_MANAGER))).getPlugins(providerClass);
			init(components);
		} catch (Exception e) {
			logger.error("Error initializing class", e);
		}
	}
	
	protected void init(List<? extends IPlugin> componentsList) {
		if(componentsList != null) {
			try {
				for(IPlugin component : componentsList) {
					components.add(new ViewComponentWrapper(component));
				}
			} catch (Exception e) {
				logger.error("Erro initializing class", e);
			} catch (Error e) {
				logger.error("Erro initializing class", e);
			}		
		}
	}
	
	public List<ViewComponentWrapper> getAvaibleComponents() {
		return components;
	}
	
	public void setSelectedComponent(ViewComponentWrapper c) {
		selectedComponent = c;
	}
	
	public ViewComponentWrapper getSelectedComponent() {
		return selectedComponent;
	}
	
	public void save() {
		if(selectedComponent != null)
			selectedComponent.save();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "" , "Params applied with success!"));
	}
	
	public void start() {
		selectedComponent.setActive(true);
	}
	
	public void stop() {
		selectedComponent.setActive(false);
	}
	
    public void onRowSelect(SelectEvent event) {
    	System.out.println(event);
    }

    public void onRowUnselect(UnselectEvent event) {
    	System.out.println(event);
    }
    
    
}