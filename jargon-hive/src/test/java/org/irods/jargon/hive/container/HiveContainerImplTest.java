package org.irods.jargon.hive.container;

import org.irods.jargon.hive.service.VocabularyService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HiveContainerImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInit() throws Exception {
		HiveConfiguration hiveConfiguration = new HiveConfiguration();
		hiveConfiguration
				.setHiveConfigLocation("/Users/mikeconway/temp/hive-data/hive.properties");
		// hiveConfiguration.setHiveConfigLocation("C:/Users/Koushyar/Documents/hive/irodshive/hive-code/hive-web/war/WEB-INF/conf/hive.properties");
		HiveContainer hiveContainer = new HiveContainerImpl();
		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();
		Assert.assertNotNull("did not start skos server",
				hiveContainer.getSkosServer());

	}

	@Test
	public void testInstanceVocabularyServer() throws Exception {
		HiveConfiguration hiveConfiguration = new HiveConfiguration();
		hiveConfiguration
				.setHiveConfigLocation("/Users/mikeconway/temp/hive-data/hive.properties");
		// hiveConfiguration.setHiveConfigLocation("C:/Users/Koushyar/Documents/hive/irodshive/hive-code/hive-web/war/WEB-INF/conf/hive.properties");
		HiveContainer hiveContainer = new HiveContainerImpl();
		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();
		VocabularyService vocabularyService = hiveContainer
				.instanceVocabularyService();
		Assert.assertNotNull("did not get vocab service", vocabularyService);
	}

}
