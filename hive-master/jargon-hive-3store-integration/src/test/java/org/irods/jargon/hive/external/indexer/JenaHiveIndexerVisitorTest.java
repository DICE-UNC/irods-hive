package org.irods.jargon.hive.external.indexer;

import java.util.Properties;

import junit.framework.Assert;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.query.MetaDataAndDomainData;
import org.irods.jargon.core.query.MetaDataAndDomainData.MetadataDomain;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitorInvoker;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitorInvoker.VisitorDesiredAction;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class JenaHiveIndexerVisitorTest {

	private static Properties testingProperties = new Properties();
	private static org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();
	public static final String IRODS_TEST_SUBDIR_PATH = "JenaHiveIndexerVisitorTest";
	private static org.irods.jargon.testutils.IRODSTestSetupUtilities irodsTestSetupUtilities = null;
	private static IRODSFileSystem irodsFileSystem = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesLoader = new TestingPropertiesHelper();
		testingProperties = testingPropertiesLoader.getTestProperties();
		irodsTestSetupUtilities = new org.irods.jargon.testutils.IRODSTestSetupUtilities();
		irodsTestSetupUtilities.initializeIrodsScratchDirectory();
		irodsTestSetupUtilities
				.initializeDirectoryForTest(IRODS_TEST_SUBDIR_PATH);
		irodsFileSystem = IRODSFileSystem.instance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
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
	 * {@link org.irods.jargon.hive.external.indexer.JenaHiveIndexerVisitor#invoke(org.irods.jargon.core.query.MetaDataAndDomainData, org.irods.jargon.datautils.visitor.AbstractIRODSVisitorInvoker)}
	 * .
	 */
	@Test
	public void testInvokeMetaDataAndDomainDataAbstractIllweRODSVisitorInvokerOfMetaDataAndDomainDataModel()
			throws Exception {
		String testCollection = "/xxx/home/testSaveOrUpdateVocabularyTerm";
		String testVocabTerm = testCollection;
		String testURI = "http://a.vocabulary#term";
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory irodsAccessObjectFactory = Mockito
				.mock(IRODSAccessObjectFactory.class);

		AbstractIRODSVisitorInvoker<MetaDataAndDomainData, Model> invoker = Mockito
				.mock(AbstractIRODSVisitorInvoker.class);
		Mockito.when(invoker.getIrodsAccount()).thenReturn(irodsAccount);
		Mockito.when(invoker.getIrodsAccessObjectFactory()).thenReturn(
				irodsAccessObjectFactory);

		JenaHiveVisitorConfiguration configuration = new JenaHiveVisitorConfiguration();

		JenaHiveIndexerVisitor visitor = new JenaHiveIndexerVisitor(
				configuration);

		MetaDataAndDomainData metadata = MetaDataAndDomainData.instance(
				MetadataDomain.COLLECTION, "1", testCollection,
				"http://www.fao.org/aos/agrovoc#c_49830", "blah", "blah");

		VisitorDesiredAction action = visitor.invoke(metadata, invoker);
		Assert.assertEquals(VisitorDesiredAction.CONTINUE, action);
		Model jenaModel = visitor.complete(invoker);
		Assert.assertNotNull("null jena model", jenaModel);

	}

}
