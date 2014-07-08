/**
 * 
 */
package org.irods.jargon.hive.external.indexer;

import java.io.IOException;
import java.io.InputStream;

import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration.JenaModelType;
import org.irods.jargon.hive.external.utils.JenaModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

/**
 * Prepares a base triple store based on a Jena configuration with the iRODS
 * ontology and configured vocabularies
 * 
 * @author Mike Conway - DICE
 * 
 */
public class HiveTripleStoreInitializerImpl implements HiveTripleStoreInitializer {

	private final JenaHiveConfiguration jenaHiveConfiguration;
	private JenaModelManager jenaModelManager = null;
	private OntModel jenaModel;
	public static final String MODEL_KEY = "model";

	public static final Logger log = LoggerFactory
			.getLogger(HiveTripleStoreInitializerImpl.class);

	/**
	 * Given a configuration, prepare a Jena triple store with the iRODS
	 * ontology and vocabulary files
	 * 
	 * @param jenaHiveConfiguration
	 */
	public HiveTripleStoreInitializerImpl(
			final JenaHiveConfiguration jenaHiveConfiguration) {
		if (jenaHiveConfiguration == null) {
			throw new IllegalArgumentException("null jenaHiveConfiguration");
		}

		this.jenaHiveConfiguration = jenaHiveConfiguration;

	}

	/* (non-Javadoc)
	 * @see org.irods.jargon.hive.external.indexer.HiveTripleStoreInitializer#initialize()
	 */
	@Override
	public OntModel initialize() throws HiveIndexerException {
		log.info("initialize()");

		if (jenaHiveConfiguration.getJenaModelType() == JenaModelType.MEMORY_ONT) {
			log.info("building memory ont model...");
			jenaModel = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		} else {
			log.info("building jena database model...");
			jenaModelManager = new JenaModelManager();
			jenaModel = jenaModelManager
					.buildJenaDatabaseModel(jenaHiveConfiguration);
		}

		// load iRODS RDF

		log.info("loading iRODS ontology file");
		InputStream in = FileManager.get().open(
				jenaHiveConfiguration.getIrodsRDFFileName());
		if (in == null) {
			log.error(
					"not able to load ontology file for iRODS based on config:{}",
					jenaHiveConfiguration);
			throw new HiveIndexerException("unable to load ontology file");
		}

		// read the RDF/XML file
		jenaModel.read(in, null);
		try {
			in.close();
		} catch (IOException e) {
			log.error("io exception closing stream, ignored");
		}

		// load vocabulary files
		for (String vocabFileName : jenaHiveConfiguration
				.getVocabularyRDFFileNames()) {

			log.info("loading vocaublary file:{}", vocabFileName);
			in = FileManager.get().open(vocabFileName);
			if (in == null) {
				throw new IllegalArgumentException("File: " + vocabFileName
						+ " not found");
			}

			// read the RDF/XML file
			jenaModel.read(in, null);
			try {
				in.close();
			} catch (IOException e) {
				log.error("io exception closing stream, ignored");
			}

		}

		log.info("done...return jena model");
		return jenaModel;

	}

}
