package org.aksw.openqa.component;

import java.util.Map;

import org.aksw.openqa.component.object.IObject;

/**
 * Plug-in interface
 * 
 * @author emarx
 */
public interface IPlugin extends IObject, IComponent {
	
	public String getVersion();
	
	public String getLabel();
	
	public boolean isActive();
	public void setActive(boolean active);
	
	public void setProperties(Map<String, Object> params);
	
	public String getAuthor();
	
	public String getWebsite();
	
	public String getInput();
	
	public String getOutput();
	
	public String getContact();
	
	public String getDescription();
	
	public String getLicense();
	
	public String getId();

	public void visit(IPluginVisitor visitor);

	public String getAPI();

}
