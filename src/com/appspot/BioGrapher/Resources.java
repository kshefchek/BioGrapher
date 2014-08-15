package com.appspot.BioGrapher;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;


public class Resources
{
	private static OntModel directoryModel = null;
	
	
	public static void setDirectoryModel (OntModel newModel)
	{
		directoryModel = newModel;
	}
	
	public static OntModel getDirectoryModel ()
	{	
		if ( directoryModel != null ){
			return directoryModel;
			}

			/*
			* ResourceEnricher.isResourceSupported() requires a minimum degree of inference, i.e., automatic reasoning.
			* Probably the minimum that we need in this application is OWL_DL_MEM_TRANS_INF (OWL_LITE_MEM_TRANS_INF if
			* that's too slow during updates). Likely OWL_MEM is not enough.
			*
			*/
			directoryModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
			
			//Leaving this code here in case we eventually want to add ontologies to the model
			
			/*
			log.info ( "Loading Ontologies" );
			// Let's go with manual loading, this is less flexible than automatic import-closure, but faster and more reliable.
			// Moreover, auto-import would need to remap a few invalid URIs
			//
			directoryModel.getDocumentManager ().setProcessImports ( false );
			File ontoDir = new File ( "./ontology" );
			File owlFiles[] = ontoDir.listFiles ( new FileFilter() { public boolean accept ( File file ) {
			return file.getName ().toLowerCase ().endsWith ( ".owl" );
			}
			});

			for ( File owlFile: owlFiles ) {
			log.info ( "Loading ontology: '" + owlFile.getName () );
			directoryModel.read ( owlFile.toURI ().toASCIIString () );
			}

			log.info ( "Knowledge base loaded!" );
            */
			// Then return it
			return directoryModel;
			}

}
