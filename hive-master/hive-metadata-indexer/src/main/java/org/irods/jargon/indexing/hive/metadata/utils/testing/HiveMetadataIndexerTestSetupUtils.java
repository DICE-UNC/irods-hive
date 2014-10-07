/**
 * 
 */
package org.irods.jargon.indexing.hive.metadata.utils.testing;

import java.io.File;
import java.util.Properties;

import org.irods.jargon.indexing.hive.metadata.HiveIndexerPropertiesHelper;

/**
 * Handy utils for setting up unit tests
 * 
 * @author Mike Conway - DICE
 * 
 */
public class HiveMetadataIndexerTestSetupUtils {

	/**
	 * Delete a scratch jena subdir
	 * 
	 * @param testProperties
	 * @param testSubdir
	 */
	public static void deleteJenaSubdir(final Properties testProperties,
			final String testSubdir) {
		StringBuilder sb = new StringBuilder();
		sb.append(testProperties
				.get(HiveIndexerPropertiesHelper.TEST_HIVE_SCRATCH_DIR));
		sb.append("/");
		sb.append(testSubdir);

		File subdirFile = new File(sb.toString());
		subdirFile.delete();

	}

	/**
	 * Build a uri to a jena jdbc url based on provided properties for a local
	 * derby database
	 * 
	 * @param testProperties
	 * @param testSubdir
	 * @return
	 */
	public static String buildJenaUriForTempDirectory(
			final Properties testProperties, final String testSubdir) {
		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:derby:");
		sb.append(testProperties
				.get(HiveIndexerPropertiesHelper.TEST_HIVE_SCRATCH_DIR));
		sb.append("/");
		sb.append(testSubdir);
		sb.append(";create=true");
		return sb.toString();
	}

}
