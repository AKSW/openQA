package org.aksw.openqa.component;

import java.util.Map;

/**
 * This class is an interface for the plug-in factory that 
 * all services interfaces should implement, it provide the 
 * common method <code>T create(params)<code>.
 * 
 * @author {@link http://emarx.org}
 *
 * @param {@link T} an implementation of {@link IPlugin} interface.
 */
public interface IPluginFactorySpi<T extends IPlugin> {
	
	/**
	 * Return a new instance of {@link T} with the provided parameters.
	 * 
	 * @param params the factory parameters to be used in the {@link T} instantiation.
	 * @return a new instance of type {@link T}.
	 */
	public T create(Map<String, Object> params);
}
