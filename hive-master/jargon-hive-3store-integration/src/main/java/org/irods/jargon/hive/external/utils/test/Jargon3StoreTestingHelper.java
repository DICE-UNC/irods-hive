/**
 * 
 */
package org.irods.jargon.hive.external.utils.test;

import java.util.Properties;

import org.irods.jargon.testutils.TestingPropertiesHelper;

/**
 * General helper functions for unit testing
 * 
 * @author Mike Conway - DICE
 */
public class Jargon3StoreTestingHelper {

	public static final String INDEXER_RDF_PATH = "indexer.irodsRdfPath";
	public static final String INDEXER_DB_DRIVER_CLASS = "indexer.jena.db.driver.class";
	public static final String INDEXER_DB_PASSWORD = "indexer.jena.db.password";
	public static final String INDEXER_DB_URI = "indexer.jena.db.uri";
	public static final String INDEXER_DB_USER = "indexer.jena.db.user";
	public static final String INDEXER_DB_TYPE = "indexer.jena.db.type";
	public static final String INDEXER_IDROP_CONTEXT = "indexer.idrop.context";
	public static final String INDEXER_DB_URI_PREFIX = "indexer.jena.db.uri.prefix";
	public static final String INDEXER_DB_URI_SUFFIX = "indexer.jena.db.uri.suffix";

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
