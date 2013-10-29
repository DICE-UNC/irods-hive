package org.irods.jargon.hive.service;

import java.util.Properties;
import org.irods.jargon.hive.container.HiveConfiguration;
import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.container.HiveContainerImpl;
import org.irods.jargon.hive.testing.HiveConfigurationTestUtilities;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

/**
 * To run this test, download UAT from GForge, configure it and add to testing properties
 * @author Mike
 *
 */
public class VocabularyServiceKEATest {

	@SuppressWarnings("unused")
	private static  HiveConfiguration hiveConfiguration;
	private static  HiveContainer hiveContainer = new HiveContainerImpl();

	private static Properties testingProperties = new Properties();
	private static HiveTestingPropertiesHelper testingPropertiesHelper = new HiveTestingPropertiesHelper();


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testingProperties = testingPropertiesHelper.getTestProperties();

		hiveConfiguration = new HiveConfigurationTestUtilities(testingProperties).buildHiveConfiguration();
		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		hiveContainer.shutdown();
	}
	
	@Test
	public void testGetTagsViaKEA() throws Exception {
		
	}

	

}
