package edu.unc.ils.mrc.hive.unittest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.irods.jargon.testutils.TestingUtilsException;

/**
 * Helper classes for testing
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class HiveTestingPropertiesHelper {

	public static final String TEST_HIVE_PARENT_DIR = "test.hive.parent.dir";
	public static final String TEST_HIVE_SOURCE_DIR = "test.hive.source.dir";

	/**
	 * 
	 */
	public HiveTestingPropertiesHelper() {
	}

	/**
	 * Load the properties that control various tests from the
	 * testing.properties file on the code path
	 * 
	 * @return <code>Properties</code> class with the test values
	 * @throws TestingUtilsException
	 */
	public Properties getTestProperties() throws TestingUtilsException {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream in = loader.getResourceAsStream("hive.testing.properties");
		Properties properties = new Properties();

		try {
			properties.load(in);
		} catch (IOException ioe) {
			throw new TestingUtilsException("error loading test properties",
					ioe);
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				// ignore
			}
		}

		return properties;
	}

}
