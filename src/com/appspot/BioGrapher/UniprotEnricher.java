package com.appspot.BioGrapher;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.conn.ssl.SSLInitializationException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.shared.JenaException;
import com.appspot.BioGrapher.NS;
import com.appspot.BioGrapher.PPT;

/**
 * Enriches an input term with annotations extracted from UniProt by doing term search.
 */
public class UniprotEnricher implements InputProcessor
{
	private String term;
	private String organism;
	private Model resultModel = ModelFactory.createDefaultModel();
	private OntModel uniprotOnt = null;
	

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
	
	public String getOrganism ()
	{
		return organism;
	}

	public void setOrganism( String organism)
	{
		this.organism = organism;
	}
	
	public Model getResultModel ()
	{
		return resultModel;
	}

	@Override
	public void run ()
	{
		
		boolean testURI = false;
		String org;
		String protSearch;
		int pcount = 0;
		Property seeAlso = ResourceFactory.createProperty(NS.RDFS + "seeAlso");
		Property hasGoTerm = ResourceFactory.createProperty(NS.DCR, "hasRelatedTermClass_1");
		
		/*Local vars for LLD section
		int tcount = 0;
		int lldFailCount = 0;
		List<String> uniqueTerms = new ArrayList <String>();*/
		
		boolean protTriple;
		
	    term = term.replaceAll(" ","%20");
		Resource inputResource = resultModel.createResource("https://bio-grapher.appspot.com/"+term);

		  
		  
			if ((this.organism != null)&&(!this.organism.isEmpty()))
			{
			 
			    org = organism.replaceAll(" ","%20"); 	  
				//If an organism was named than 3 proteins should be sufficient
				protSearch = "http://www.uniprot.org/uniprot/?query=" + term + "%20" + org + "&sort=score&limit=5&format=rdf";
				uniprotOnt =  ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
				try{	  
				    URL url = new URL(protSearch);
				    URLConnection con = url.openConnection();
				    con.setConnectTimeout(5000); // 5 seconds
			        uniprotOnt.read (con.getInputStream(),"RDF/XML");
			        testURI = true;
			    }
			    catch(SSLInitializationException e){
				    e.printStackTrace(System.err);
			    }
			    catch(JenaException e){
				    testURI = false;
				    System.err.println("\nCould not load UniProt URL "+protSearch);
				    e.printStackTrace(System.err);
				} 
				catch (MalformedURLException e) {	
				    e.printStackTrace();
				} 
				catch (IOException e) {
				    e.printStackTrace();
				}
			
			}
			else
			{
				//Increasing results to 7 proteins for no organism
			    protSearch = "http://www.uniprot.org/uniprot/?query=" + term + "&sort=score&limit=7&format=rdf";
			    uniprotOnt =  ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
			    try{
		            URL url = new URL(protSearch);
		            URLConnection con = url.openConnection();
		            con.setConnectTimeout(5000); // 5 seconds
			        uniprotOnt.read (con.getInputStream(),"RDF/XML");
			        testURI = true;
			    }
			    catch(SSLInitializationException e){
				    e.printStackTrace(System.err);
			    }
			    catch(JenaException e){
				    testURI = false;
				    System.err.println("\nCould not load UniProt URL "+protSearch);
				    e.printStackTrace(System.err);
			    } 
			    catch (MalformedURLException e) {
				    e.printStackTrace();
			    } 
			    catch (IOException e) {
				    e.printStackTrace();
			    }
			}
		
		
			if (testURI == true)
			{
				// The UniProt web service doesn't define Protein as a class and the inference type we use here is not able
				// to entail that Protein is a class from <s, rdf:type, Protein>. Therefore, listindividuals won't work.
				ResIterator itr = uniprotOnt.listResourcesWithProperty ( 
					uniprotOnt.getProperty	( NS.rdf + "type" ), 
					uniprotOnt.getResource ( "http://purl.uniprot.org/core/Protein" ) 
				);
				
				if ( itr != null ) while (itr.hasNext ())
				{
			
					protTriple = false;
					Resource protResource = itr.next ();
				
					pcount++;
					
					if (pcount < 5)
					{
						Property hasRelatedProtein = ResourceFactory.createProperty(NS.DCR, "hasRelatedProtein_"+ pcount);
						resultModel.add(inputResource, hasRelatedProtein, protResource);}
					else
					{
						Property hasRelatedProtein = ResourceFactory.createProperty(NS.DCR, "hasRelatedProtein_5");
						resultModel.add(inputResource, hasRelatedProtein, protResource);
					}
					
					
					/* Add other interesting links provided by the UniProt webservice, such as KEGG Pathways, Reactome, and Array Express
					 * Links
					 */
				
					NodeIterator riter = uniprotOnt.listObjectsOfProperty ( 
							protResource,
							uniprotOnt.getProperty	( NS.RDFS + "seeAlso" )
					);
					
					while(riter.hasNext()){
						
						RDFNode link = riter.next ();
						Resource linkResource = link.asResource();
						
						String regex = "http://purl.uniprot.org/kegg/";
				    	Pattern pt1 = Pattern.compile(regex);
				    	Matcher match = pt1.matcher(link.toString());
				    
				    	String regex2 = "http://purl.uniprot.org/reactome/";
				    	Pattern pt2 = Pattern.compile(regex2);
				    	Matcher match2 = pt2.matcher(link.toString());
				    	
				    	String regex3 = "http://purl.uniprot.org/arrayexpress/";
				    	Pattern pt3 = Pattern.compile(regex3);
				    	Matcher match3 = pt3.matcher(link.toString());
				    	
				    	String regex4 = "http://purl.uniprot.org/pathway-interaction-db/";
				    	Pattern pt4 = Pattern.compile(regex4);
				    	Matcher match4 = pt4.matcher(link.toString());
				    	
				    	
				    	
				    	if( (match.find()) || (match2.find()) || (match3.find()) || (match4.find()))
				    	{
				    		resultModel.add(protResource, seeAlso, link);
				    		resultModel.add(linkResource, PPT.label, link);
				    	}
				    	
	
					}
					if (pcount == 1){
					
					    NodeIterator goIterator = uniprotOnt.listObjectsOfProperty ( 
							protResource,
							uniprotOnt.getProperty	( "http://purl.uniprot.org/core/classifiedWith" )
					    );
					
					    while(goIterator.hasNext()){
						
						    RDFNode link = goIterator.next ();
						
						    String regex5 = "http://purl.uniprot.org/go/";
				    	    Pattern pt5 = Pattern.compile(regex5);
				    	    Matcher match5 = pt5.matcher(link.toString());
				    	
				    	    if (match5.find()){
				    		    resultModel.add(protResource, hasGoTerm, link);
				    	    }
						
					    }
					}
				
					/* The Sparql query returns both Go terms and Keywords.  Also, Go terms have multiple term labels, so 
					 * the query returns duplicate go terms for a single protein.  To resolve these issues, the program
					 * only pulls out terms that match http://purl.uniprot.org/go/.  A linked hash set is used to remove duplicates.
					 */
					
					/* Commenting out LLD function for now, since we can get GO terms from UniProt and LLD
					 * has a variable response time (often failing due to a socket timeout) and because we
					 * don't need protein names and go term labels.  Leaving this section here in case we 
					 * want to obtain information from LLD
	
					String sparqlQuery=
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
							"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
							"PREFIX uniprot: <http://purl.uniprot.org/core/> " +

							"SELECT distinct ?fullname ?term ?termLabel " +
							"WHERE { " +
							"<"+protResource.toString()+">" +
							"  uniprot:classifiedWith ?term;" +
							"  uniprot:recommendedName ?name." +
							"  ?name uniprot:fullName ?fullname." +
							"  ?term rdfs:label ?termLabel." +
							"} ";
					
					Query query = QueryFactory.create(sparqlQuery);
					QueryEngineHTTP qexec = new QueryEngineHTTP("http://linkedlifedata.com/sparql", query);
					qexec.setTimeout(1000); //1 sec
					
					ResultSet results = null;
					
					try{
						results = qexec.execSelect();
					}
					catch(JenaException e){
						System.err.println("Could not finish LLD query for UniProt resource");
						lldFailCount++;
						if (lldFailCount < 3){
						    continue;
						}
						else{
							break;
						}
					}
		
					while (results.hasNext())
					{
					
						//reset variables	
						uniqueTerms.clear();	
						
						QuerySolution sol = results.nextSolution();
						RDFNode go = sol.get("term");
						
						String goTerm = go.toString();
						
						String reg6 = "http://purl.uniprot.org/go/";
				    	Pattern pt6 = Pattern.compile(reg6);
				    	Matcher mt6 = pt6.matcher(goTerm);
				    	
				    	if(mt6.find())
				    	{
			    		
				    		uniqueTerms.add(goTerm);
			    		
				    		Resource goResource = resultModel.createResource(goTerm);
			    		
				    		RDFNode pLabel = sol.get("fullname");
				    		RDFNode gLabel = sol.get("termLabel");
			    		
				    		if (protTriple == false)
				    		{
				    			resultModel.add(protResource, PPT.label, pLabel);
				    			protTriple = true;
				    		}
			    		
			    		    resultModel.add(goResource,PPT.label,gLabel);
			    		    
			    		
				    	}
					
					}
				
					qexec.close() ;
					//Remove duplicates from Go Terms and preserve order
				
					Set<String> uniqueGo = new LinkedHashSet<String>(uniqueTerms);
				
					Iterator<String> iterGo = uniqueGo.iterator();
				
					while (iterGo.hasNext())
					{
					
					    Resource uniqueGoResource = resultModel.createResource(iterGo.next());
					
					    tcount++;
					    if (tcount < 5)
					    {
		    	 	        Property hasRelatedTermClass = ResourceFactory.createProperty(NS.DCR, "hasRelatedTermClass_"+ tcount);
					        resultModel.add(protResource, hasRelatedTermClass, uniqueGoResource);
					    }
					    else
					    {
					        Property hasRelatedTermClass = ResourceFactory.createProperty(NS.DCR, "hasRelatedTermClass_5");
					        resultModel.add(protResource, hasRelatedTermClass, uniqueGoResource);	
					    }		
				    }*/			
				}
			}
		}
		
	
}
