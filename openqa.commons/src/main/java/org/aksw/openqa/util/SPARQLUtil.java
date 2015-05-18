package org.aksw.openqa.util;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementPathBlock;
import com.hp.hpl.jena.sparql.syntax.ElementUnion;

public class SPARQLUtil {
	
	public static boolean isSingleEntity(Query query) {
		ElementGroup elementGroup  = (ElementGroup) query.getQueryPattern();
		if(elementGroup.getElements().size()==1) {
			com.hp.hpl.jena.sparql.syntax.Element element = elementGroup.getElements().get(0);				
			if(element instanceof ElementUnion) {
				ElementUnion elementUniont = (ElementUnion) elementGroup.getElements().get(0);
				elementGroup  = (ElementGroup) elementUniont.getElements().get(0);
			}					
			ElementPathBlock elementTripleBlock = (ElementPathBlock) elementGroup.getElements().get(0);
			com.hp.hpl.jena.graph.Node subject = elementTripleBlock.getPattern().get(0).getSubject();
			com.hp.hpl.jena.graph.Node predicate = elementTripleBlock.getPattern().get(0).getPredicate();
			com.hp.hpl.jena.graph.Node object = elementTripleBlock.getPattern().get(0).getObject();
			return subject.isURI() && predicate.isVariable() && object.isVariable();
		}
		return false;
	}
	
	public static boolean isGeneric(String sparqlQuery) {
		Query query = QueryFactory.create(sparqlQuery);
		return isGeneric(query);
	}
	
	public static boolean isGeneric(Query query) {
		ElementGroup elementGroup  = (ElementGroup) query.getQueryPattern();
		if(elementGroup.getElements().size() == 1) {
			com.hp.hpl.jena.sparql.syntax.Element element = elementGroup.getElements().get(0);				
			if(element instanceof ElementUnion) {
				ElementUnion elementUniont = (ElementUnion) elementGroup.getElements().get(0);
				elementGroup  = (ElementGroup) elementUniont.getElements().get(0);
			}					
			ElementPathBlock elementTripleBlock = (ElementPathBlock) elementGroup.getElements().get(0);
			com.hp.hpl.jena.graph.Node subject = elementTripleBlock.getPattern().get(0).getSubject();
			com.hp.hpl.jena.graph.Node predicate = elementTripleBlock.getPattern().get(0).getPredicate();
			com.hp.hpl.jena.graph.Node object = elementTripleBlock.getPattern().get(0).getObject();
			return subject.isVariable() && predicate.isURI() && object.isVariable();
		}
		return false;
	}
}
