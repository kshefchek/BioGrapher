package com.appspot.BioGrapher;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

/**
 * A facility that contains common namespaces, in the form of string constants. Please note that the string fields
 * are defined in lower case here, despite the Java convention of using all-upper-case names for constants. That's 
 * because it is so common to use lower case for RDF/XML namespaces.  
 *
 *
 */
public class NS
{
	public static final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String DCR = "http://bio-grapher.appspot.com/ontology#";
	public static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String title = "http://purl.org/dc/elements/1.1/title";
	public static final String ABSTRACT = "http://purl.org/dc/elements/1.1/abstract";
	public static final String owl = "http://www.w3.org/2002/07/owl#";
	public static final String obo = "http://purl.obolibrary.org/obo/";
	public static final String dc= "http://purl.org/dc/elements/1.1/";
	public static final Map<String, String> PREFIX;
	    static {
	        Map<String, String> predicates = new HashMap<String,String>();
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedDocument_1", "hasRelatedDocument");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedDocument_2", "hasRelatedDocument");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedDocument_3", "hasRelatedDocument");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedDocument_4", "hasRelatedDocument");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedDocument_5", "hasRelatedDocument");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedProtein_1", "hasRelatedProtein");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedProtein_2", "hasRelatedProtein");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedProtein_3", "hasRelatedProtein");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedProtein_4", "hasRelatedProtein");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedProtein_5", "hasRelatedProtein");
	        predicates.put("http://www.w3.org/2000/01/rdf-schema#label", "hasLabel");
	        predicates.put("http://www.w3.org/2000/01/rdf-schema#seeAlso", "seeAlso");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedTermClass_1", "hasGoTerm");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedTermClass_2", "hasGoTerm");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedTermClass_3", "hasGoTerm");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedTermClass_4", "hasGoTerm");
	        predicates.put("http://bio-grapher.appspot.com/ontology#hasRelatedTermClass_5", "hasGoTerm");
	        predicates.put("http://purl.org/dc/elements/1.1/creator", "hasAuthor");
	        
	        PREFIX = Collections.unmodifiableMap(predicates);
	    }
}
