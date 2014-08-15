package com.appspot.BioGrapher;

import java.io.IOException;
import com.google.appengine.api.ThreadManager;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;


import com.appspot.BioGrapher.NS;

@SuppressWarnings("serial")
public class BioGrapherServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
        //local Variables
		HashMap<String, Object> link = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> json = 
				new ArrayList<HashMap<String,Object>>();
		Map<String, Integer> resources = new HashMap<String, Integer>();
		String sameOrganism = null;

        //Get input terms from webform
		String term1 = req.getParameter("term1");
		String organism1 = req.getParameter("org1");
		String term2 = req.getParameter("term2");
		String organism2 = req.getParameter("org2");
		
		if (organism1.equalsIgnoreCase(organism2)){
			sameOrganism = organism1;
		}
		
		OntModel newModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
		Resources.setDirectoryModel(newModel);
		
		BioTermEnricher btEnricher = new BioTermEnricher(term1, organism1);
		BioTermEnricher btEnricher2 = new BioTermEnricher(term2, organism2);
		
		Thread runTerm1 = ThreadManager.createThreadForCurrentRequest(btEnricher);
		Thread runTerm2 = ThreadManager.createThreadForCurrentRequest(btEnricher2);
		runTerm1.start();
		runTerm2.start();
		try {
			runTerm1.join(48000);
			runTerm2.join(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.err);
		}
		  catch (ConcurrentModificationException e){
			e.printStackTrace(System.err);
		}
		Model finalModel = Resources.getDirectoryModel ();
		
		String t2 = term2.replaceAll(" ","%20");
		String t1 = term1.replaceAll(" ","%20");
		
		
		//Get all ternary relationships (publications, proteins)
		String sparqlQuery=
			"SELECT *" +
			"WHERE { "+
			"<https://bio-grapher.appspot.com/"+t1+"> ?p ?ter." +
			"<https://bio-grapher.appspot.com/"+t2+"> ?p2 ?ter;" +
			"} ";
		
		Query query = QueryFactory.create(sparqlQuery);
		QueryExecution qexec = QueryExecutionFactory.create(query,finalModel);
		ResultSet results = qexec.execSelect();
		
		while (results.hasNext())
		{
			
			QuerySolution solution = results.nextSolution();
			RDFNode predicate = solution.get("p");
			RDFNode ternaryPredicate = solution.get("p2");
			RDFNode object = solution.get("ter");
			String edge = predicate.toString();
			String ternaryEdge = ternaryPredicate.toString();
			String target = object.toString();
			
			//Store ternary resources
			resources.put(target,1);
			
			link = new HashMap<String, Object>();
			
			link.put("name", NS.PREFIX.get(edge));
			link.put("source",term1);
			link.put("target",target);
			json.add(link);
			
			link = new HashMap<String, Object>();
				
			link.put("name", NS.PREFIX.get(ternaryEdge));
			link.put("source",term2);
			link.put("target",target);
			json.add(link);
			
		}
		qexec.close();
		
		
		//Get 5-ary relationships (Go terms, publication term labels, pathways, KEGG, etc.)
        String sparqlQuery2=
				"SELECT distinct *" +
				"WHERE { "+
				"<https://bio-grapher.appspot.com/"+t1+"> ?p ?ter." +
				"?ter ?p2 ?quin." +
				"<https://bio-grapher.appspot.com/"+t2+"> ?p3 ?ter2." +
				"?ter2 ?p4 ?quin;" +
				"} ";
        
        Query query2 = QueryFactory.create(sparqlQuery2);
		QueryExecution qexec2 = QueryExecutionFactory.create(query2,finalModel);
		ResultSet results2 = qexec2.execSelect();
		
		while (results2.hasNext())
		{
			QuerySolution solution = results2.nextSolution();
			
			RDFNode predicate = solution.get("p");
			RDFNode quinaryPredicate = solution.get("p2");
			RDFNode object = solution.get("ter");
			
			RDFNode quinaryObject = solution.get("quin");
			
			RDFNode t2Predicate = solution.get("p3");
			RDFNode t2QuinaryPredicate = solution.get("p4");
			RDFNode t2Object = solution.get("ter2");
			
			String edge = predicate.toString();
			String quinaryEdge = quinaryPredicate.toString();
			String target = object.toString();
			
			String quinaryTarget = quinaryObject.toString();
			
			String t2Edge = t2Predicate.toString();
			String t2QuinaryEdge = t2QuinaryPredicate.toString();
			String t2Target = t2Object.toString();
	
			/*Ignoring organism links for two terms with the same organism
			 * For example, publications with a common UMLS term label "Escherichia coli"
			 * for two E coli proteins
			 */
			if ( (resources.get(target) != null) || (resources.get(t2Target) != null)
				 || (quinaryTarget.equalsIgnoreCase(sameOrganism))
			   )
			{
				continue;
			}
			
			
            link = new HashMap<String, Object>();

			link.put("name", NS.PREFIX.get(edge));
			link.put("source",term1);
			link.put("target",target);
			json.add(link);
			
			link = new HashMap<String, Object>();
				
			link.put("name", NS.PREFIX.get(quinaryEdge));
			link.put("source",target);
			link.put("target",quinaryTarget);
			json.add(link);
			
			link = new HashMap<String, Object>();

			link.put("name", NS.PREFIX.get(t2Edge));
			link.put("source",term2);
			link.put("target",t2Target);
			json.add(link);
			
			link = new HashMap<String, Object>();
				
			link.put("name", NS.PREFIX.get(t2QuinaryEdge));
			link.put("source",t2Target);
			link.put("target",quinaryTarget);
			json.add(link);	
	
		}
		
		qexec2.close();
		
		Object gObj = new Gson().toJson(json);
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(new Gson().toJson(json));
		System.err.println(gObj);

	}
}
