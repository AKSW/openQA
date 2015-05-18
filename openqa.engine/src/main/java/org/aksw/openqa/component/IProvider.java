package org.aksw.openqa.component;

import java.util.List;


public interface IProvider<C> extends IComponent {
	
	
	/**
	 * Return a list containing all <C> found in the environment.
	 * 
	 * @return a list of all <C> found in the environment.
	 */
	List<C> list();
	
	/**
	 * Return <C> where <C> is a implementation of the given class.
	 * 
	 * @param clazz The class that will be used to retrieve <C>.
	 * @return a implementation of <C>. 
	 */
	<K extends C> K get(Class<K> clazz);
	
	/**
	 * Return <C> where <C> is a implementation of the given class.
	 * 
	 * @param id The id that will be used to retrieve <C>.
	 * @return a implementation of <C>. 
	 */
	C get(String id);
	
	/**
	 * Register the plug-in in the provider.
	 * 
	 * @param plugin the plug-in instance to be registered in this provider.
	 * 
	 */
	void register(C plugin);	

	/**
	 * Unregister the plug-in <IPlugin> in the provider.
	 * 
	 * @param plugin the plug-in instance to be unregistered in this provider.
	 * 
	 */
	void unregister(C plugin);
}
