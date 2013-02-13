package org.irods.jargon.hive.service;

import java.util.List;

import org.irods.jargon.hive.container.HiveConfiguration;
import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.container.HiveContainerImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unc.hive.client.ConceptProxy;

public class VocabularyServiceTest {

	static final HiveConfiguration hiveConfiguration = new HiveConfiguration();
	static final HiveContainer hiveContainer = new HiveContainerImpl();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		hiveConfiguration
				.setHiveConfigLocation("/Users/mikeconway/temp/hive-data/hive.properties");
		// hiveConfiguration.setHiveConfigLocation("C:/Users/Koushyar/Documents/hive/irodshive/hive-code/hive-web/war/WEB-INF/conf/hive.properties");

		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		hiveContainer.shutdown();
	}

	@Test
	public void testInit() throws Exception {
		// Assert.assertNotNull("did not start skos server",
		// hiveContainer.getSkosServer());
		VocabularyService vocabularyService = new VocabularyService(
				hiveContainer);
		vocabularyService.getNumberOfConcept("AGROVOC");
		vocabularyService.getNumerOfRelations("AGROVOC");
		vocabularyService.getAllVocabularyNames();

		List<ConceptProxy> fatherList = null;
		fatherList = vocabularyService.getSubTopConcept("AGROVOC", "A", true);
		int i = 0;
		for (i = 0; i < fatherList.size(); i++)
			System.out.println(fatherList.get(i));

	}

}
