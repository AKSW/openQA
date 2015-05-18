package org.aksw.openqa.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.aksw.openqa.component.providers.impl.ServiceProvider;

/**
 * This class is an abstract implementation of a component provider.
 * 
 * @author {@linkplain http://emarx.org}
 *
 * @param {@link C} A {@link IComponent} object.
 * @param {@link F} A {@link ComponentFactory} of {@link C}.
 */
public abstract class AbstractPluginProvider<C extends IPlugin, F extends IPluginFactorySpi<C>> extends AbstractProvider<C> {
	
	private static final String DEFAULT_META_PATH = "/";
	private static final String DEFAULT_INIT_SUFFIX = ".ini";
	
	private Map<String, C> components;
	private Map<Class<?>, C> componentClasses;
	private List<C> componentList;
	
	public AbstractPluginProvider() {
		components = new HashMap<String, C>();
		componentClasses = new HashMap<Class<?>, C>();
		componentList = new ArrayList<C>();
	}

	public AbstractPluginProvider(Class<F> clazz, List<? extends ClassLoader> classLoaders, ServiceProvider serviceProvider) {
		this();
		loadComponents(clazz, classLoaders, serviceProvider);
	}
	
	protected void loadComponents(Class<F> clazz, List<? extends ClassLoader> classLoaders, ServiceProvider serviceProvider) {
		List<C> componentList = list(clazz, classLoaders);
		for(C component : componentList) {
			register(component);
		}
	}
	
	public void register(C component) {
		String componentId = component.getId();
		// Do not include an instance, if there is already an instance registered to the component
		if(!components.containsKey(componentId)) {
			this.components.put(componentId, component);
			this.componentList.add(component);
			this.componentClasses.put(component.getClass(), (C) component);
		}
	}
	
	public void unregister(C component) {
		String componentId = component.getId();
		this.components.remove(componentId);
		this.componentList.remove(component);
		this.componentClasses.remove(component.getClass());
	}
	
	private List<C> list(Class<F> clazz, ServiceLoader<F> serviceLoader) {
		List<C> list = new ArrayList<C>();
		for (F factory : serviceLoader) {
			String className = factory.getClass().getName();
			String classPath = factory.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			classPath = classPath.substring(0, classPath.lastIndexOf("/"));
			// try to load properties from plug-in local directory
			Map<String, Object> params = null;

			params = getPropertiesFromFile(DEFAULT_META_PATH + className + DEFAULT_INIT_SUFFIX);
			if(params == null) // if there is not try to load from the plug-in jar
				params = getProperties(clazz, DEFAULT_META_PATH + className + DEFAULT_INIT_SUFFIX);
				
			list.add(factory.create(params));
		}
		return list;
	}
	
	@Override
	public C get(String id) {
		return components.get(id);
	}
	
	/**
	 * Return a list containing all {@link IComponent}'s found in the environment.
	 * 
	 * @return a list of all {@link IComponent}'s found in the environment.
	 */
	public List<C> list() {
		return componentList;
	}
	
	/**
	 * Retrieve a registered {@link IComponent} same as, or that implements or extends the given
	 * {@link IComponentProvider} class.
	 * In case there is no {@link IComponent} for a given class null is returned.  
	 * 
	 * @param clazz the Class of the component that should be returned
	 * @return an activated class of {@link IComponent} that implements the given class or null 
	 * if the component can not be found or is deactivated.
	 */
	@SuppressWarnings("unchecked")
	public <K extends C> K get(Class<K> clazz) {
		for(Class<?> classEntry : componentClasses.keySet()) {
			if(clazz.isAssignableFrom(classEntry)) {
				C component = componentClasses.get(classEntry);
				if(component.isActive())
					return ((K) component);
			}
		}
		return null;
	}
	
	/**
	 * Instantiate all the existing {@link F} classes in the given classLoaders.  
	 * 
	 * @param clazz the {@link F} Class of the objects that should be returned.
	 * @param classLoaders the {@link ClassLoaders} that will be used to searching for the {@link F} implementations. 
	 * @return a list containing all instantiated {@link C} from a given {@link F} in the classLoaders.
	 */
	protected List<C> list(Class<F> clazz, List<? extends ClassLoader> classLoaders) {
		List<C> components = new ArrayList<C>();
		for(ClassLoader loader: classLoaders) {
			ServiceLoader<F> serviceLoader = ServiceLoader.load(clazz, loader);
			components.addAll(list(clazz, serviceLoader));
		}
		return components;
    }
}
