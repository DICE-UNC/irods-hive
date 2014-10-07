/**
 * 
 */
package org.irods.jargon.indexing.hive.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;

/**
 * Helper class for understanding the indexer.properties file for HIVE
 * 
 * @author Mike Conway - DICE
 * 
 */
public class HiveIndexerPropertiesHelper {

	private final Properties properties;

	public static String KEY_IRODS_RDF = "indexer.irodsRdfPath";
	public static String KEY_JENA_DB_DRIVER = "indexer.jena.db.driver.class";
	public static String KEY_JENA_DB_PASSWORD = "indexer.jena.db.password";
	public static String KEY_JENA_DB_USER = "indexer.jena.db.user";
	public static String KEY_JENA_DB_URI = "indexer.jena.db.uri";
	public static String KEY_JENA_DB_TYPE = "indexer.jena.db.type";
	public static String KEY_IDROP_CONTEXT = "indexer.idrop.context";

	/**
	 * Dir for the preconfigured 'functional' testing hive built using the
	 * HiveTestInstanceSetup utility
	 */
	public static final String TEST_HIVE_PARENT_DIR = "test.hive.parent.dir";

	/**
	 * Location of a source HIVE set of RDF and template config properties
	 */
	public static final String TEST_HIVE_SOURCE_DIR = "test.hive.source.dir";

	/**
	 * Location of a temp scratch directory under which any number of test HIVEs
	 * can be built in different unit and funtional tests
	 */
	public static final String TEST_HIVE_SCRATCH_DIR = "test.hive.scratch.dir";

	/**
	 * @throws HiveIndexerConfigException
	 * 
	 */
	public HiveIndexerPropertiesHelper() throws HiveIndexerConfigException {
		// load props from classpath
		this.properties = loadProps();

	}

	/**
	 * Build a <code>JenaHiveConfiguration</code> object based on provided
	 * properties
	 * 
	 * @return {@link JenaHiveConfiguration} object that defines the connection
	 *         to jena
	 * @throws HiveIndexerConfigException
	 */
	public JenaHiveConfiguration buildJenaHiveConfiguration()
			throws HiveIndexerConfigException {
		JenaHiveConfiguration jenaHiveConfiguration = new JenaHiveConfiguration();

		jenaHiveConfiguration
				.setIdropContext(validateAndReturnPropValue(KEY_IDROP_CONTEXT));
		jenaHiveConfiguration
				.setIrodsRDFFileName(validateAndReturnPropValue(KEY_IRODS_RDF));
		jenaHiveConfiguration
				.setJenaDbDriverClass(validateAndReturnPropValue(KEY_JENA_DB_DRIVER));
		jenaHiveConfiguration
				.setJenaDbPassword(validateAndReturnPropValue(KEY_JENA_DB_PASSWORD));
		jenaHiveConfiguration
				.setJenaDbUri(validateAndReturnPropValue(KEY_JENA_DB_URI));
		jenaHiveConfiguration
				.setJenaDbUser(validateAndReturnPropValue(KEY_JENA_DB_USER));
		jenaHiveConfiguration
				.setJenaDbType(JenaHiveConfiguration.JENA_DERBY_DB_TYPE);
		jenaHiveConfiguration.setAutoCloseJenaModel(false);
		return jenaHiveConfiguration;
	}

	private Properties loadProps() throws HiveIndexerConfigException {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream in = loader.getResourceAsStream("testing.properties");
		Properties properties = new Properties();

		try {
			properties.load(in);
		} catch (IOException ioe) {
			throw new HiveIndexerConfigException(
					"error loading testing.properties", ioe);
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				// ignore
			}
		}
		return properties;
	}

	public Properties getProperties() {
		return properties;
	}

	/**
	 * Pull a property out by key and make sure it's there
	 * 
	 * @param key
	 * @return
	 * @throws HiveIndexerConfigException
	 *             if the prop is not available
	 */
	private String validateAndReturnPropValue(final String key)
			throws HiveIndexerConfigException {
		String val = properties.getProperty(key);
		if (val == null || val.isEmpty()) {
			throw new HiveIndexerConfigException(
					"cannot find property for key:" + key);
		}

		return val;
	}

}
