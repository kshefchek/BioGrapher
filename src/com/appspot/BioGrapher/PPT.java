/* PPT.java
 * 
 * Contains the common properties used among the different enrichers.
 */
package com.appspot.BioGrapher;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class PPT 
{
   	public static Property identifier = ResourceFactory.createProperty(NS.dc, "identifier");
   	public static Property type = ResourceFactory.createProperty(NS.rdf, "type");
   	public static Property title = ResourceFactory.createProperty(NS.dc, "title");
   	public static Property creator = ResourceFactory.createProperty(NS.dc, "creator");
   	public static Property date = ResourceFactory.createProperty(NS.dc, "date");
   	public static Property description = ResourceFactory.createProperty(NS.dc, "description");
   	public static Property source = ResourceFactory.createProperty(NS.dc, "source");
	public static Property label = ResourceFactory.createProperty(NS.RDFS, "label"); 
	public static Property lldUri = ResourceFactory.createProperty(NS.owl, "samAs"); 
}
