/**
 * 
 */
package org.irods.jargon.hive.external.utils;

import java.util.Properties;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.hive.external.indexer.HiveIndexerException;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration.JenaModelType;
import org.irods.jargon.testutils.TestingPropertiesHelper;

/**
 * General helper functions for unit testing
 * 
 * @author Mike Conway - DICE
 */
public class JargonHiveConfigurationHelper {

	public static final String INDEXER_RDF_PATH = "indexer.irodsRdfPath";
	public static final String INDEXER_DB_DRIVER_CLASS = "indexer.jena.db.driver.class";
	public static final String INDEXER_DB_PASSWORD = "indexer.jena.db.password";
	public static final String INDEXER_DB_URI = "indexer.jena.db.uri";
	public static final String INDEXER_DB_USER = "indexer.jena.db.user";
	public static final String INDEXER_DB_TYPE = "indexer.jena.db.type";
	public static final String INDEXER_IDROP_CONTEXT = "indexer.idrop.context";
	public static final String INDEXER_DB_URI_PREFIX = "indexer.jena.db.uri.prefix";
	public static final String INDEXER_DB_URI_SUFFIX = "indexer.jena.db.uri.suffix";
	
	public static final String IRODS_HOST = "irods.host";
	public static final String IRODS_PORT = "irods.port";
	public static final String IRODS_ZONE = "irods.zone";
	public static final String IRODS_USER = "irods.user";
	public static final String IRODS_PASSWORD = "irods.password";
	
	
	/**
	 * Build an iRODS account from information in a properties file
	 * @param properties {@link Properties} containing name/value pairs used to build the iRODS account
	 * @return {@link IRODSAccount} based on the properties
	 * @throws HiveIndexerException
	 */
	public static IRODSAccount buildIRODSAccountFromProperties(final Properties properties) throws HiveIndexerException {
		
		if (properties == null) {
			throw new IllegalArgumentException("null properties");
		}
		
		try {
			return IRODSAccount.instance(
					properties.getProperty(IRODS_HOST),
					Integer.parseInt(properties.getProperty(IRODS_PORT)),
					properties.getProperty(IRODS_USER), 
					properties.getProperty(IRODS_PASSWORD), 
					"",
					properties.getProperty(IRODS_ZONE), 
					"");
		} catch (NumberFormatException e) {
			throw new HiveIndexerException("number format error for iRODS port", e);
		} catch (JargonException e) {
			throw new HiveIndexerException("unable to create iRODS account from properties", e);
		}
		
	}
	
	
	/**
	 * This method will take a properties file and build a <code>JenaHiveConfiguration</code> for a database ont type
	 * connection.  This will not contain hive initialization of RDF or vocabularies, assuming that a triple store has already been
	 * initialized.
	 * 
	 * @param properties {@link Properties} containing expected HIVE config info
	 * @return
	 */
	public static JenaHiveConfiguration buildJenaHiveConfigurationFromProperties(final Properties properties) {
		if (properties == null) {
			throw new IllegalArgumentException("null properties");
		}
		
		JenaHiveConfiguration jenaHiveConfiguration = new JenaHiveConfiguration();
		jenaHiveConfiguration.setAutoCloseJenaModel(false);
		jenaHiveConfiguration.setIdropContext(properties.getProperty(INDEXER_IDROP_CONTEXT));
		jenaHiveConfiguration.setIrodsRDFFileName(properties.getProperty(INDEXER_RDF_PATH));
		jenaHiveConfiguration.setJenaDbDriverClass(properties.getProperty(INDEXER_DB_DRIVER_CLASS));
		jenaHiveConfiguration.setJenaDbPassword(properties.getProperty(INDEXER_DB_PASSWORD));
		jenaHiveConfiguration.setJenaDbType(properties.getProperty(INDEXER_DB_TYPE));
		jenaHiveConfiguration.setJenaDbUri(properties.getProperty(INDEXER_DB_URI));
		jenaHiveConfiguration.setJenaDbUser(properties.getProperty(INDEXER_DB_USER));
		jenaHiveConfiguration.setJenaModelType(JenaModelType.DATABASE_ONT);
		return jenaHiveConfiguration;
	}

	/**
	 * Appropriate for local db's like derby, append a prefix and suffix to a
	 * local path or uri to create a JDBC URL
	 * 
	 * @param properties
	 *            <code>Properties</code> derived from test setup properties
	 * @param scratchParentDir
	 *            <code>String</code> with a subdirectory under the specified
	 *            scratch directory as configured in jargon.properties, under
	 *            which test databases are created
	 * @return <code>String</code> with a jdbc URI
	 */
	public static String buildJdbcUriFromProperties(
			final Properties properties, final String scratchParentDir) {

		StringBuilder sb = new StringBuilder();
		sb.append(properties.get(INDEXER_DB_URI_PREFIX));
		sb.append(properties
				.getProperty(TestingPropertiesHelper.GENERATED_FILE_DIRECTORY_KEY));
		sb.append(scratchParentDir);
		sb.append(properties.get(INDEXER_DB_URI_SUFFIX));
		return sb.toString();

	}

}
