package org.irods.jargon.hive.service;

import static org.junit.Assert.*;

import java.util.List;

import org.irods.jargon.hive.container.HiveConfiguration;
import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.container.HiveContainerImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unc.hive.client.ConceptProxy;

public class VocabularyServiceTest {

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
		//hiveConfiguration
		//		.setHiveConfigLocation("/Users/mikeconway/temp/hive-data/hive.properties");
	    hiveConfiguration.setHiveConfigLocation("C:/Users/Koushyar/Documents/hive/irodshive/hive-code/hive-web/war/WEB-INF/conf/hive.properties");
		HiveContainer hiveContainer = new HiveContainerImpl();
		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();
		//Assert.assertNotNull("did not start skos server",
		//		hiveContainer.getSkosServer());
		VocabularyService vocabularyService= new VocabularyService(hiveContainer);
		vocabularyService.getNumberOfConcept("AGROVOC");
		vocabularyService.getNumerOfRelations("AGROVOC");
		vocabularyService.getAllVocabularyNames();
		
		List<ConceptProxy> fatherList = null;
		fatherList = vocabularyService.getSubTopConcept("AGROVOC", "A", true);
		int i=0;
		for (i=0;i<fatherList.size();i++)
			System.out.println(fatherList.get(i));
		
		
		
		
	}

}
