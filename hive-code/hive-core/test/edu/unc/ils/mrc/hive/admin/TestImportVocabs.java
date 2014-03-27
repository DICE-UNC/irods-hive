package edu.unc.ils.mrc.hive.admin;

import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.api.impl.elmo.SKOSSchemeImpl;
import edu.unc.ils.mrc.hive.testframework.HiveScratchAreaCreator;
import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

/**
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class TestImportVocabs {

	private static Properties testingProperties = new Properties();
	private static HiveTestingPropertiesHelper testingPropertiesHelper = new HiveTestingPropertiesHelper();
	private static HiveScratchAreaCreator hiveScratchAreaCreator = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testingProperties = testingPropertiesHelper.getTestProperties();
		if (testingPropertiesHelper.checkTestHiveFuntionalSetup() == false) {
			throw new Exception(
					"test hive not set up, run HiveTestInstanceSetup");
		}

		hiveScratchAreaCreator = new HiveScratchAreaCreator(testingProperties);

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadMesh() throws Exception {

		String testSubdir = "testLoadMesh";
		String vocabName = "mesh";

		String parentOfTest = hiveScratchAreaCreator
				.clearAndInitializeScratchAreaWithTestVocab(testSubdir,
						vocabName);

		SKOSScheme schema = new SKOSSchemeImpl(parentOfTest, "mesh", true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, false,
				true);

		// right now looks for success

	}

	@Test
	public void testLoadAgrovoc() throws Exception {

		String testSubdir = "testLoadAgrovoc";
		String vocabName = "agrovoc";

		String parentOfTest = hiveScratchAreaCreator
				.clearAndInitializeScratchAreaWithTestVocab(testSubdir,
						vocabName);

		SKOSScheme schema = new SKOSSchemeImpl(parentOfTest, "agrovoc", true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, false,
				true);

		// right now looks for success

	}

	/**
	 * Test method for
	 * {@link edu.unc.ils.mrc.hive.admin.TaggerTrainer#trainKEAAutomaticIndexingModule()}
	 * .
	 */
	@Test
	public void testLoadUat() throws Exception {

		String testSubdir = "testLoadUat";
		String vocabName = "uat";

		String parentOfTest = hiveScratchAreaCreator
				.clearAndInitializeScratchAreaWithTestVocab(testSubdir,
						vocabName);

		SKOSScheme schema = new SKOSSchemeImpl(parentOfTest, "uat", true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, false,
				true);

		// right now looks for success

	}
	/*
	 * @Test public void testLoadMesh() throws Exception {
	 * 
	 * String hivePath = testingProperties
	 * .getProperty(HiveTestingPropertiesHelper.TEST_HIVE_PARENT_DIR);
	 * SKOSScheme schema = new SKOSSchemeImpl(hivePath, "mesh", true);
	 * 
	 * schema.importConcepts(schema.getRdfPath(), true, true, true, false,
	 * true);
	 * 
	 * }
	 * 
	 * @Test public void testLoadAgrovoc() throws Exception {
	 * 
	 * String hivePath = testingProperties
	 * .getProperty(HiveTestingPropertiesHelper.TEST_HIVE_PARENT_DIR);
	 * SKOSScheme schema = new SKOSSchemeImpl(hivePath, "agrovoc", true);
	 * 
	 * schema.importConcepts(schema.getRdfPath(), true, true, true, false,
	 * true);
	 * 
	 * }
	 */

}
