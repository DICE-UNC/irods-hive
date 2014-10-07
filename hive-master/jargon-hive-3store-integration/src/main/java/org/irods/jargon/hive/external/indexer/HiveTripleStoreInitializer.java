package org.irods.jargon.hive.external.indexer;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * Defines a service to initialize a Jena triple store based on a configuration
 * for batch index creation or for use with the indexing framework.
 * 
 * @author Mike Conway - DICE
 * 
 */
public interface HiveTripleStoreInitializer {

	/**
	 * Initialize a given model and return the Jena <code>OntModel</code> that
	 * results. Note that this model is not closed by the initializer.
	 * 
	 * @return {@link OntModel} with iRODS ontology data and vocabulary data
	 *         loaded
	 * @throws HiveIndexerException
	 */
	public abstract OntModel initialize() throws HiveIndexerException;

}