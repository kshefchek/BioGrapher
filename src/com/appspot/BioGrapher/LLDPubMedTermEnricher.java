/*LLDPubMedTermEnricher.java
 * 
 * Description: Obtains the pubMed IDs of related PubMed articles.  It then takes those IDs, searches each one using SPARQL for related resources.  Those resources,
 *  and their relationships, are then added to the Jena model, resultsModel.
 */

package com.appspot.BioGrapher;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.query.*;
import com.appspot.BioGrapher.NS;
import com.appspot.BioGrapher.PPT;

import java.util.*;

public class LLDPubMedTermEnricher implements InputProcessor
{
	private Model resultModel = ModelFactory.createDefaultModel(); //initializes the model
	private List<String> pmids =  new ArrayList<String>(); //contains the PubMed IDs
	private String term = "";
	
	//Returns term
	public String getTerm ()
	{
		return term;
	}
	
	//sets term
	public void setTerm ( String term )
	{
		this.term = term;
	}

	public List<String> getPMIDs ()
	{
		return pmids;
	}

	public void setPMIDs ( List<String> pmids )
	{
		this.pmids = pmids;
	}

	public Model getResultModel ()
	{
		return resultModel;
	}

	@Override
	public void run ()
	{
		int autoDocCount = 1; //count for the AutoRelatedDocument_#
		int resultCount = 0; //used in counting the results returned in each table
		int lldFailCount = 0;
		
		//sets the prefixes for the model
		resultModel.setNsPrefix("dcr", NS.DCR);
		resultModel.setNsPrefix("rdfs", NS.RDFS);
		resultModel.setNsPrefix("owl", NS.owl);
		
		//Creates the properties defining the related resources found in LLD and the resource for the DC-Thera resource
	    term = term.replaceAll(" ","%20");
		Resource inputResource = resultModel.createResource("https://bio-grapher.appspot.com/"+term);
		Property hasRelatedDoc = null;
		
		for(int x = 0; x < pmids.size(); x++)
		{	
			//Sparql Statement to retrieve resources from LLD
			String sparqlQuery=
					"PREFIX pubmed: <http://linkedlifedata.com/resource/pubmed/> " +
					"PREFIX lifeskim: <http://linkedlifedata.com/resource/lifeskim/> " +
					"PREFIX skos: <http://www.w3.org/2004/02/skos/core#> " +

					"SELECT DISTINCT ?concept ?termLabel " +
						"WHERE {" +
						"   <https://linkedlifedata.com/resource/pubmed/id/" + pmids.get(x) + "> lifeskim:mentions ?concept." +
						"   ?concept skos:prefLabel ?termLabel." +
						" } " + 
						"";
			
			//Execution of the Sparql Query--Obtain results
			Query query = QueryFactory.create(sparqlQuery);
			QueryEngineHTTP qexec = new QueryEngineHTTP("http://linkedlifedata.com/sparql", query);
			qexec.setTimeout(1000);
			
			ResultSet results = null;
			
			try{
				results = qexec.execSelect();
			}
			catch(JenaException e){
				lldFailCount++;
				System.err.println("Could not finish LLD query for pubmed resource");
				if (lldFailCount < 3){
				    continue;
				}
				else{
					break;
				}
			}
			
			
			//Creation of resources/properties to populate the model
			Resource document = ResourceFactory.createResource("http://www.ncbi.nlm.nih.gov/pubmed/" + pmids.get(x)); //resource for pubMed article that was searched
			
			if(autoDocCount < 5)
				hasRelatedDoc = ResourceFactory.createProperty(NS.DCR, "hasRelatedDocument_" + autoDocCount); //resource showing that the article is an autorelatedDocument
			else
				hasRelatedDoc = ResourceFactory.createProperty(NS.DCR, "hasRelatedDocument_5");
			
				resultCount = 0; 
				
				while (results.hasNext())
				{	
					
					QuerySolution sol = results.nextSolution(); //obtains a line in the results
					
					// Obtains the termLabel and concept from each line of the results
					RDFNode termLabel = sol.get("termLabel");  
					RDFNode concept = sol.get("concept"); 
					
					String con = concept.toString();
					
					Resource lldConcept = ResourceFactory.createResource(con); //Resource for the concept that was returned
	            	
					//Populating the model with the relationships between LLD and the Dc-Thera resource
					//resultModel.add(inputResource, hasRelatedDoc, document);
	            	resultModel.add(document, PPT.label, termLabel);
	            	//resultModel.add(document, PPT.lldUri, lldConcept);
	            	
	            	//Populating the model with the relationships describing the LLD resource
	            	resultModel.add(lldConcept, PPT.label, termLabel);
	            	
	            	resultCount++;
				}
			
				qexec.close();
			
			
			autoDocCount++;
		}

	}

}

