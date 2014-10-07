/**
 *
 */
package org.irods.jargon.indexing.hive.metadata.utils;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.hive.external.indexer.HiveIndexerException;
import org.irods.jargon.hive.external.indexer.HiveTripleStoreInitializer;
import org.irods.jargon.hive.external.indexer.HiveTripleStoreInitializerImpl;
import org.irods.jargon.hive.external.indexer.JenaHiveIndexer;
import org.irods.jargon.hive.external.indexer.JenaHiveIndexerServiceImpl;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * Convenience service that can initialize a HIVE based triple store for use by
 * the indexing framework. This indexer creates a Jena triple store and
 * initializes it with the iRODS ontology and selected vocabularies.
 * <p/>
 * Initialization can optionally include a batch mode indexing of iRODS.
 * <p/>
 * This convenience class wraps some of the services of the
 * jargon-hive-3store-integration library, see
 * https://github.com/DICE-UNC/irods-hive
 *
 * @author Mike Conway - DICE
 *
 */

public class HiveMetadataIndexerInitializerImpl implements
		HiveMetadataIndexerInitializer {

	private final JenaHiveConfiguration jenaHiveConfiguration;
	public static final Logger log = LoggerFactory
			.getLogger(HiveMetadataIndexerInitializerImpl.class);

	public HiveMetadataIndexerInitializerImpl(
			final JenaHiveConfiguration jenaHiveConfiguration) {

		if (jenaHiveConfiguration == null) {
			throw new IllegalArgumentException("null jenaHiveConfiguration");
		}

		this.jenaHiveConfiguration = jenaHiveConfiguration;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.irods.jargon.indexing.hive.metadata.utils.HiveMetadataIndexer#
	 * initializeAndBatchIndexOntologyModel
	 * (org.irods.jargon.core.pub.IRODSAccessObjectFactory,
	 * org.irods.jargon.core.connection.IRODSAccount)
	 */
	@Override
	public OntModel initializeAndBatchIndexOntologyModel(
			final IRODSAccessObjectFactory irodsAccessObjectFactory,
			final IRODSAccount irodsAccount) throws HiveIndexerException {

		log.info("initializeAndBatchIndexOntologyModel()");

		if (irodsAccessObjectFactory == null) {
			throw new IllegalArgumentException("null irodsAccessObjectFactory");
		}

		if (irodsAccount == null) {
			throw new IllegalArgumentException("null irodsAccount");
		}

		JenaHiveIndexer jenaHiveIndexerService = new JenaHiveIndexerServiceImpl(
				irodsAccessObjectFactory, irodsAccount, jenaHiveConfiguration);

		try {
			return jenaHiveIndexerService.execute();
		} catch (JargonException e) {
			log.error("jargon exception indexing to build jena model", e);
			throw new HiveIndexerException("error in batch indexing process", e);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.irods.jargon.indexing.hive.metadata.utils.HiveMetadataIndexer#
	 * initializeBareOntologyModel()
	 */
	@Override
	public OntModel initializeBareOntologyModel() throws HiveIndexerException {
		log.info("initializeinitializeBareOntologyModel()");
		HiveTripleStoreInitializer tripleStoreInitializer = new HiveTripleStoreInitializerImpl(
				jenaHiveConfiguration);
		return tripleStoreInitializer.initialize();
	}

}
