package org.aksw;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;

public class SparqlDescribeTest {
	public static void main(String[] args) {
		
		String nextQuery = "Construct { <http://dbpedia.org/resource/Peace_and_War> ?p ?o } where { <http://dbpedia.org/resource/Peace_and_War> ?p ?o}";
		Query describeQuery = QueryFactory.create(nextQuery);
		QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", describeQuery);
		Model m = exec.execConstruct();
		
		Resource resource = m.getResource("http://dbpedia.org/resource/Peace_and_War");
		Property property = new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#label");
		Statement propertyResourceValue = resource.getProperty(property);
		System.out.println(propertyResourceValue.getObject().asLiteral().getValue());
		
	}
}

