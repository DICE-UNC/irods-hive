/**
 * 
 */
package org.irods.jargon.indexing.hive.metadata.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.irods.jargon.hive.external.indexer.HiveIndexerException;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;
import org.irods.jargon.hive.external.utils.JenaHiveConfigurationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * command line utility to intialize a hive indexer triple store by specifying a
 * properties file. This utility does not do batch indexing of iRODS data.
 * 
 * @author Mike Conway - DICE
 * 
 */
public class HiveIndexerAdminTool {

	public static final Logger log = LoggerFactory
			.getLogger(HiveIndexerAdminTool.class);

	private final Properties props;

	/**
	 * 
	 */
	public HiveIndexerAdminTool(final Properties properties)
			throws HiveIndexerException {
		if (properties == null) {
			throw new IllegalArgumentException("null properties");
		}

		this.props = properties;
	}

	public OntModel initialize() throws HiveIndexerException {
		log.info("initialize()");
		JenaHiveConfiguration jenaHiveConfiguration = JenaHiveConfigurationHelper
				.buildJenaHiveConfigurationFromProperties(props);
		log.info("jenaHiveConfiguration:{}", jenaHiveConfiguration);
		HiveMetadataIndexerInitializer initializer = new HiveMetadataIndexerInitializerImpl(
				jenaHiveConfiguration);
		log.info("initializing");
		OntModel ontModel = initializer.initializeBareOntologyModel();
		log.info("complete");
		return ontModel;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			log.error("should have 1 argument which is the path to the properties file");
			throw new RuntimeException("should have 1 path argument");
		}

		String path = args[0];

		log.info("properties file path:{}", path);
		try {
			InputStream propsStream = new FileInputStream(path);
			Properties hiveProperties = new Properties();
			hiveProperties.load(propsStream);
			HiveIndexerAdminTool hiveIndexerAdminTool = new HiveIndexerAdminTool(
					hiveProperties);
			OntModel model = hiveIndexerAdminTool.initialize();
			log.info("initialized...now close");
			model.close();
		} catch (FileNotFoundException e) {
			log.error("file not found reading props", e);
			throw new RuntimeException("could not read properties file");
		} catch (IOException e) {
			log.error("io exception reading props", e);
			throw new RuntimeException("io exception reading properties file");
		} catch (HiveIndexerException e) {
			log.error("hive indexer exception", e);
			throw new RuntimeException("hive indexer exception on initialize",
					e);
		}
	}

}
