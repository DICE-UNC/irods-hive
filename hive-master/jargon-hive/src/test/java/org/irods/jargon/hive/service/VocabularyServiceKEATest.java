package org.irods.jargon.hive.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.irods.jargon.core.utils.LocalFileUtils;
import org.irods.jargon.hive.container.HiveConfiguration;
import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.container.HiveContainerImpl;
import org.irods.jargon.hive.testing.HiveConfigurationTestUtilities;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.unc.hive.client.ConceptProxy;

import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

/**
 * To run this test, download UAT from GForge, configure it and add to testing
 * properties
 * 
 * @author Mike
 * 
 */
public class VocabularyServiceKEATest {

	private static HiveConfiguration hiveConfiguration;
	private static HiveContainer hiveContainer = new HiveContainerImpl();

	private static Properties testingProperties = new Properties();
	private static HiveTestingPropertiesHelper testingPropertiesHelper = new HiveTestingPropertiesHelper();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testingProperties = testingPropertiesHelper.getTestProperties();

		hiveConfiguration = new HiveConfigurationTestUtilities(
				testingProperties).buildHiveConfiguration();
		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		hiveContainer.shutdown();
	}

	@Test
	public void testGetTagsViaKEAAgainstAgrovoc() throws Exception {

		File testFile = LocalFileUtils
				.getClasspathResourceAsFile("/testdocs/winrock_wi10ce.txt");

		if (testFile == null) {
			Assert.fail("unable to find agrovoc doc");
		}

		List<String> openedVocabularies = new ArrayList<String>();
		openedVocabularies.add("agrovoc");

		VocabularyService vocabularyService = hiveContainer
				.instanceVocabularyService();
		List<ConceptProxy> actual = vocabularyService.getTagsBasedOnFilePath(
				testFile.getAbsolutePath(), openedVocabularies, 100, "kea");

		Assert.assertNotNull("null response", actual);

	}

	@Test
	public void testGetTagsViaKEA() throws Exception {

		File testFile = LocalFileUtils
				.getClasspathResourceAsFile("/testdocs/bostid_b02moe.txt");

		if (testFile == null) {
			Assert.fail("unable to find UAT doc");
		}

		List<String> openedVocabularies = new ArrayList<String>();
		openedVocabularies.add("uat");

		VocabularyService vocabularyService = hiveContainer
				.instanceVocabularyService();
		List<ConceptProxy> actual = vocabularyService.getTagsBasedOnFilePath(
				testFile.getAbsolutePath(), openedVocabularies, 100, "kea");

		Assert.assertNotNull("null response", actual);

	}

}
