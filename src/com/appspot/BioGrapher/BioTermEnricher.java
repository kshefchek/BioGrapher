package com.appspot.BioGrapher;

import java.util.ConcurrentModificationException;

import com.google.appengine.api.ThreadManager;

/**
 * 
 *  Does the enrichment of a Directory biomaterial. This contains a whole pipeline of several enrichers.
 *
 * <dl><dt>date</dt><dd>Feb 28, 2012</dd></dl>
 *
 */
public class BioTermEnricher implements InputProcessor
{
	
	private String term;
	private String organism;
	
	public BioTermEnricher()
	{
	}
	
	public BioTermEnricher(String term, String org)
	{
		this.term = term;
		this.organism = org;	
	}
	

	public String getTerm ()
	{
		return term;
	}

	public void setTerm ( String term )
	{
		this.term = term;
	}
	
	public String getOrganism ()
	{
		return organism;
	}

	public void setOrganism ( String organism )
	{
		this.organism = organism;
	}
	

	@Override
	public void run ()
	{	
		
	    
	    System.out.print("Retrieving PubMed IDs and result file elements.....");
	    PubMedTermSearch pubMedSearch = new PubMedTermSearch ();
	    pubMedSearch.setTerm ( this.getTerm() );
	        
	    System.out.print("Retrieving related UniProt resources.....");
	    UniprotEnricher uniProtEnricher = new UniprotEnricher ();
	    uniProtEnricher.setTerm ( this.getTerm() );
	    uniProtEnricher.setOrganism ( this.getOrganism () );
	    
	    Thread runPMSearch = ThreadManager.createThreadForCurrentRequest(pubMedSearch);
		Thread runUPSearch = ThreadManager.createThreadForCurrentRequest(uniProtEnricher);
		runPMSearch.start();
		runUPSearch.start();
		try {
			runPMSearch.join(27500);
			runUPSearch.join(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.err);
		}
		  catch (ConcurrentModificationException e){
			e.printStackTrace(System.err);
		}
	    
	  
		System.out.print("Merging PubMed model.....");
	    Utils.mergeGraphs ( pubMedSearch.getResultModel () );
	    System.out.println("Complete");

	    System.out.print("Merging UniProt result model.....");
		Utils.mergeGraphs ( uniProtEnricher.getResultModel () );
		System.out.println("Complete");
		
		System.out.print("Retrieving LLD resources using the PubMed IDs.....");
	    LLDPubMedTermEnricher lldEnricher = new LLDPubMedTermEnricher ();
	    lldEnricher.setPMIDs ( pubMedSearch.getPMIDs () );
	    lldEnricher.setTerm ( this.getTerm() );
	    lldEnricher.run(); 
		System.out.println("Complete");
		
	    
	    System.out.print("Merging LLD result model.....");
		Utils.mergeGraphs( lldEnricher.getResultModel () );
		System.out.println("Complete");
		
	    
	    System.out.printf("Result model for %s successfully created and merged into ouput model\n", term);

	}
}
