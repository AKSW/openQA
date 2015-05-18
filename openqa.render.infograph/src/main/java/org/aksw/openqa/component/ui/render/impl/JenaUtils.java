package org.aksw.openqa.component.ui.render.impl;

import com.hp.hpl.jena.datatypes.BaseDatatype.TypedValue;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;

public class JenaUtils {
	
	public static Resource getResource(String resourceURI, String enpoint, String graph) {
		String nextQuery = "Construct { <" + resourceURI + "> ?p ?o } where { <" + resourceURI + "> ?p ?o}";
		Query describeQuery = QueryFactory.create(nextQuery);
		QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", describeQuery);
		Model m = exec.execConstruct();
		Resource resource = m.getResource(resourceURI);
		return resource;
	}

	public static String getValue(Resource resource, String propertyURI) {		
		Property property = new PropertyImpl(propertyURI);
		Statement propertyResourceValue = resource.getProperty(property);		
		if(propertyResourceValue != null) {
			RDFNode node = propertyResourceValue.getObject();			
			if(node != null) {
				if(node.isURIResource()) {
					String value = node.toString();
					return value;
				} else if (node.isLiteral()) {
					Object value = node.asLiteral().getValue();			
					if(value instanceof TypedValue) {
						TypedValue typedValue = (TypedValue) value;
						return typedValue.lexicalValue;
					}
					return value.toString();
				} else if (node.isResource()) {
					String value = (String) node.asResource().getURI();
					return value;
				}
			}
		}
		
		return null;
	}
	
	public static boolean exists(Resource resource, String propertyURI) {
		return getValue(resource, propertyURI) != null;
	}
}
