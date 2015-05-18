package org.aksw.openqa.manager.plugin;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aksw.openqa.component.IPlugin;
import org.aksw.openqa.component.IPluginVisitor;
import org.aksw.openqa.component.IProvider;
import org.aksw.openqa.component.answerformulation.Interpreter;
import org.aksw.openqa.component.answerformulation.IResolver;
import org.aksw.openqa.component.answerformulation.IRetriever;
import org.aksw.openqa.component.answerformulation.ISynthesizer;
import org.aksw.openqa.component.context.IContext;
import org.aksw.openqa.component.providers.impl.ContextProvider;
import org.aksw.openqa.component.providers.impl.InterpreterProvider;
import org.aksw.openqa.component.providers.impl.RenderProvider;
import org.aksw.openqa.component.providers.impl.ResolverProvider;
import org.aksw.openqa.component.providers.impl.RetrieverProvider;
import org.aksw.openqa.component.providers.impl.ServiceProvider;
import org.aksw.openqa.component.providers.impl.SynthesizerProvider;
import org.aksw.openqa.component.service.IService;
import org.aksw.openqa.component.ui.render.InfoGraphRender;
import org.aksw.openqa.util.PropertyLoaderUtil;
import org.apache.log4j.Logger;

public class PluginManager extends PropertyLoaderUtil {
	
	private static Logger logger = Logger.getLogger(PluginManager.class);
	
	private final static String PLUGIN_PATH = "plugins";
	
	private File plugginDir = new File(PLUGIN_PATH);
	private ClassLoader parent;
	
	ServiceProvider serviceProvider;
	InterpreterProvider interpreterProvider;
	RetrieverProvider retrieverProvider;
	SynthesizerProvider synthesizerProvider;
	ResolverProvider resolverProvider;
	RenderProvider renderProvider;
	ContextProvider contextProvider;
	
	RegisterVisitor registerVisitor = new RegisterVisitor();
	UnregisterVisitor unregisterVisitor = new UnregisterVisitor();
	
	Map<Class<? extends IProvider<? extends IPlugin>>, IProvider<? extends IPlugin>> providers = new HashMap<Class<? extends IProvider<? extends IPlugin>>, IProvider<? extends IPlugin>>();
	
	public PluginManager(String plugginDir, ClassLoader parent) {
		this.parent = parent;
		this.plugginDir = new File(plugginDir);
		load();
	}
	
	public PluginManager(ClassLoader parent) {
		this.parent = parent;
		load();
	}
	
	public PluginManager() {        
        this.parent = Thread.currentThread().getContextClassLoader();
        load();
	}
	
	private void load() {		
        List<ClassLoader> classLoaders = new ArrayList<ClassLoader>();
        
        // add the application directory as class path to look for plugins in main class path
        classLoaders.add(parent);
        
        if(plugginDir != null) {
	        File[] directories = plugginDir.listFiles(new FileFilter() {
	          @Override
	          public boolean accept(File file) {
	            return file.isDirectory();
	          }
	        });        
	        
	        if(directories != null) {
		        for(File directory : directories) {
		           File[] flist = directory.listFiles(new FileFilter() {
		                public boolean accept(File file) {return file.getPath().toLowerCase().endsWith(".jar");}
		           });
		
		           URL[] urls = new URL[flist.length];
		           for (int i = 0; i < flist.length; i++) {
		        	   try {
							urls[i] = flist[i].toURI().toURL();
		        	   } catch (MalformedURLException e) {
							logger.error("Error in URI format", e);
		        	   }
		           }
		           
		           if(urls.length > 0 ) {
		        	   URLClassLoader ucl = new URLClassLoader(urls, parent);
		        	   classLoaders.add(ucl);
		           }
		        }
	        }
        }
        
        serviceProvider = new ServiceProvider(classLoaders);
        providers.put(ServiceProvider.class, serviceProvider);
        
        interpreterProvider = new InterpreterProvider(classLoaders, serviceProvider);
        providers.put(InterpreterProvider.class, interpreterProvider);
        
        retrieverProvider = new RetrieverProvider(classLoaders, serviceProvider);
        providers.put(RetrieverProvider.class, retrieverProvider);
        
        synthesizerProvider = new SynthesizerProvider(classLoaders, serviceProvider);
        providers.put(SynthesizerProvider.class, synthesizerProvider);
        
        resolverProvider = new ResolverProvider(classLoaders, serviceProvider);
        providers.put(ResolverProvider.class, resolverProvider);
        
        renderProvider = new RenderProvider(classLoaders, serviceProvider);
        providers.put(RenderProvider.class, renderProvider);
        
        contextProvider = new ContextProvider(classLoaders, serviceProvider);
        providers.put(ContextProvider.class, contextProvider);
	}
    
    public void register(IPlugin plugin) {
    	plugin.visit(registerVisitor);
    }
    
    public void unregister(IPlugin plugin) {
    	plugin.visit(registerVisitor);
    }
        
    public List<? extends IPlugin> getPlugins(Class<? extends IProvider<? extends IPlugin>> providerClass) throws Exception {
    	Map<Class<? extends IProvider<? extends IPlugin>>, IProvider<? extends IPlugin>> providers = getProviders();
    	IProvider<? extends IPlugin> provider = providers.get(providerClass);
    	return provider.list();
    }
    
    public IPlugin getPlugin(String id)  {    	
    	Map<Class<? extends IProvider<? extends IPlugin>>, IProvider<? extends IPlugin>> providers = getProviders();
    	for(IProvider<? extends IPlugin> providerEntry : providers.values()) {
    		IPlugin component = providerEntry.get(id);
    		if(component != null)
    			return component;
    	}    	
    	return null;
    }
    
    public <C extends IPlugin> C getPlugin(Class<? extends IProvider<?>> providerClass, Class<C> componentClass) {
    	Map<Class<? extends IProvider<? extends IPlugin>>, IProvider<? extends IPlugin>> providers = getProviders();
    	@SuppressWarnings("unchecked")
		IProvider<C> provider = (IProvider<C>) providers.get(providerClass);
    	if(provider != null)
			return provider.get(componentClass);
    	return null;
    }
    
    public <C extends IPlugin> IProvider<? extends C> getProvider(Class<? extends IProvider<C>> providerClass) {
    	Map<Class<? extends IProvider<? extends IPlugin>>, IProvider<? extends IPlugin>> providers = getProviders();
    	@SuppressWarnings("unchecked")
		IProvider<? extends C> provider = (IProvider<? extends C>) providers.get(providerClass);
		return provider;
    }
    
	private Map<Class<? extends IProvider<? extends IPlugin>>, IProvider<? extends IPlugin>> getProviders() {
		return providers;
	}

	/**
	 * Set the activation value to all plug-ins.
	 * 
	 * @param active true to activate all loaded plug-ins or false otherwise. 
	 */
	public void setActive(boolean active) {
		Map<Class<? extends IProvider<? extends IPlugin>>, IProvider<? extends IPlugin>> providersMap = getProviders();
		Collection<IProvider<? extends IPlugin>> providers = providersMap.values();
		for(IProvider<? extends IPlugin> provider : providers) {
			List<? extends IPlugin> plugins = provider.list();
			for(IPlugin plugin : plugins) {
				plugin.setActive(active);
			}
		}
	}
	
	/**
	 * Set the activation to all plug-ins.
	 * 
	 * @param active true to activate all loaded plug-ins or false otherwise.
	 * @param ids plug-in list id's to be activated or deactivated.
	 */
	public void setActive(boolean active, String[] ids) {
		Map<Class<? extends IProvider<? extends IPlugin>>, IProvider<? extends IPlugin>> providersMap = getProviders();
		Collection<IProvider<? extends IPlugin>> providers = providersMap.values();
		List<String> idList = Arrays.asList(ids);
		for(IProvider<? extends IPlugin> provider : providers) {
			List<? extends IPlugin> plugins = provider.list();
			for(IPlugin plugin : plugins) {
				if(idList.contains(plugin.getId()))
					plugin.setActive(active);
			}
		}
	}
	
	/**
	 * Set the activation of a plug-in.
	 * 
	 * @param active true to activate or false otherwise.
	 * @param id the id of the plug-in.
	 */
	public void setActive(boolean active, String id) {
		Map<Class<? extends IProvider<? extends IPlugin>>, IProvider<? extends IPlugin>> providersMap = getProviders();
		Collection<IProvider<? extends IPlugin>> providers = providersMap.values();
		for(IProvider<? extends IPlugin> provider : providers) {
			List<? extends IPlugin> plugins = provider.list();
			for(IPlugin plugin : plugins) {
				if(id.equals(plugin.getId()))
					plugin.setActive(active);
			}
		}
	}
	
	/**
	 * Visit all plug-ins in the classpath active and inactive.
	 * 
	 * @param visitor visitor of the plug-ins
	 */
	public void visit(IPluginVisitor visitor) {
		Map<Class<? extends IProvider<? extends IPlugin>>, IProvider<? extends IPlugin>> providersMap = getProviders();
		Collection<IProvider<? extends IPlugin>> providers = providersMap.values();
		for(IProvider<? extends IPlugin> provider : providers) {
			List<? extends IPlugin> plugins = provider.list();
			for(IPlugin plugin : plugins) {
					plugin.visit(visitor);
			}
		}
	}
	
	private class RegisterVisitor implements org.aksw.openqa.component.IPluginVisitor {
    	
    	public void visit(IService service) {
    		serviceProvider.register(service);
    	}
    	
    	public void visit(Interpreter interpreter) {
    		interpreterProvider.register(interpreter);
    	}
    	
    	public void visit(IRetriever retriever) {
    		retrieverProvider.register(retriever);
    	}
    	
    	public void visit(ISynthesizer synthesizer) {
    		synthesizerProvider.register(synthesizer);
    	}
    	
    	public void visit(IResolver resolver) {
    		resolverProvider.register(resolver);
    	}
    	
    	public void visit(InfoGraphRender render) {
    		renderProvider.register(render);
    	}
    	
    	public void visit(IContext context) {
    		contextProvider.register(context);
    	}
    	
    }
    
    private class UnregisterVisitor implements org.aksw.openqa.component.IPluginVisitor {
    	
    	public void visit(IService service) {
    		serviceProvider.unregister(service);
    	}
    	
    	public void visit(Interpreter interpreter) {
    		interpreterProvider.unregister(interpreter);
    	}
    	
    	public void visit(IRetriever retriever) {
    		retrieverProvider.unregister(retriever);
    	}
    	
    	public void visit(ISynthesizer synthesizer) {
    		synthesizerProvider.unregister(synthesizer);
    	}
    	
    	public void visit(IResolver resolver) {
    		resolverProvider.unregister(resolver);
    	}
    	
    	public void visit(InfoGraphRender render) {
    		renderProvider.unregister(render);
    	}
    	
    	public void visit(IContext context) {
    		contextProvider.unregister(context);
    	}
    	
    }
}