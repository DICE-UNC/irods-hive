package edu.unc.ils.mrc.hive.admin;

import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.api.impl.elmo.SKOSSchemeImpl;
import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

/**
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class TaggerTrainerTestForLoadKEA {

	private static Properties testingProperties = new Properties();
	private static HiveTestingPropertiesHelper testingPropertiesHelper = new HiveTestingPropertiesHelper();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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

	/**
	 * Test method for
	 * {@link edu.unc.ils.mrc.hive.admin.TaggerTrainer#trainKEAAutomaticIndexingModule()}
	 * .
	 */
	@Test
	public void testTrainKEAAutomaticIndexingModule() throws Exception {
		SKOSScheme schema = new SKOSSchemeImpl();

		// fill in some values from the test properties

		TaggerTrainer trainer = new TaggerTrainer(schema);

		trainer.trainKEAAutomaticIndexingModule();

		// test some stuff to see if it worked

	}

}
