package org.irods.jargon.hive.service;

import junit.framework.TestCase;

import org.irods.jargon.hive.container.HiveConfiguration;
import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.container.HiveContainerImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unc.hive.client.ConceptProxy;

//import edu.unc.ils.mrc.hive.api.SKOSScheme;

public class VocabServiceTest {
	
	static final HiveConfiguration hiveConfiguration = new HiveConfiguration();
	static final HiveContainer hiveContainer = new HiveContainerImpl();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		hiveConfiguration.setHiveConfigLocation("/Users/zhangle/temp/hive-data/hive.properties");
		
		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		hiveContainer.shutdown();
	}
	
	@Test
	public void testGetFirstConcept() throws Exception {
		VocabularyService vocabularyService = new VocabService(
				hiveContainer);
		ConceptProxy x = vocabularyService.getFirstConcept("agrovoc");
		TestCase.assertNotNull("did not load first concept", x);
	}
	
	

	


}
