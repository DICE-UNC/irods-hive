package org.irods.jargon.hive.external.indexer;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import junit.framework.Assert;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.query.MetaDataAndDomainData;
import org.irods.jargon.core.query.MetaDataAndDomainData.MetadataDomain;
import org.irods.jargon.core.utils.IRODSUriUtils;
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
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

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
	private static File vocabFile = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesLoader = new TestingPropertiesHelper();
		testingProperties = testingPropertiesLoader.getTestProperties();
		irodsTestSetupUtilities = new org.irods.jargon.testutils.IRODSTestSetupUtilities();
		irodsTestSetupUtilities.initializeIrodsScratchDirectory();
		irodsTestSetupUtilities
				.initializeDirectoryForTest(IRODS_TEST_SUBDIR_PATH);
		irodsFileSystem = IRODSFileSystem.instance();

		ClassLoader loader = JenaHiveIndexerVisitorTest.class.getClassLoader();
		URL resc = loader.getResource("agrovoc.rdf");

		if (resc == null) {
			throw new Exception("unable to load agrovoc");
		}

		String vocabFileName = resc.getFile();

		vocabFile = new File(vocabFileName);

		if (!vocabFile.exists()) {
			throw new Exception("unable to load agrovoc test vocabulary");
		}

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
	public void testInvokeWithMetadata() throws Exception {
		String testCollection = "/xxx/home/testSaveOrUpdateVocabularyTerm";

		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory irodsAccessObjectFactory = Mockito
				.mock(IRODSAccessObjectFactory.class);

		URI irodsURI = IRODSUriUtils
				.buildURIForAnAccountWithNoUserInformationIncluded(
						irodsAccount, testCollection);

		@SuppressWarnings("unchecked")
		AbstractIRODSVisitorInvoker<MetaDataAndDomainData> invoker = Mockito
				.mock(AbstractIRODSVisitorInvoker.class);
		Mockito.when(invoker.getIrodsAccount()).thenReturn(irodsAccount);
		Mockito.when(invoker.getIrodsAccessObjectFactory()).thenReturn(
				irodsAccessObjectFactory);

		JenaHiveConfiguration configuration = new JenaHiveConfiguration();
		configuration.getVocabularyRDFFileNames().add(vocabFile.getPath());

		JenaHiveIndexerVisitor visitor = new JenaHiveIndexerVisitor(
				configuration);

		MetaDataAndDomainData metadata = MetaDataAndDomainData.instance(
				MetadataDomain.COLLECTION, "1", testCollection,
				"http://www.fao.org/aos/agrovoc#c_49830", "blah", "blah");

		VisitorDesiredAction action = visitor.invoke(metadata, invoker);
		Assert.assertEquals(VisitorDesiredAction.CONTINUE, action);
		Model jenaModel = visitor.getJenaModel();
		Assert.assertNotNull("null jena model", jenaModel);

		ResIterator iter = jenaModel.listSubjectsWithProperty(RDFS.label);
		Resource resc = null;
		while (iter.hasNext()) {
			resc = iter.nextResource();
			break;
		}

		Assert.assertNotNull("no resc found with label", resc);

		String uri = resc.getURI();
		Assert.assertEquals("did not get uri of collection",
				irodsURI.toString(), uri);
		jenaModel.close();

	}

	@SuppressWarnings("unchecked")
	@Test(expected = IllegalArgumentException.class)
	public void testInvokeNullMetadata() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory irodsAccessObjectFactory = Mockito
				.mock(IRODSAccessObjectFactory.class);

		AbstractIRODSVisitorInvoker<MetaDataAndDomainData> invoker = Mockito
				.mock(AbstractIRODSVisitorInvoker.class);
		Mockito.when(invoker.getIrodsAccount()).thenReturn(irodsAccount);
		Mockito.when(invoker.getIrodsAccessObjectFactory()).thenReturn(
				irodsAccessObjectFactory);

		JenaHiveConfiguration configuration = new JenaHiveConfiguration();

		JenaHiveIndexerVisitor visitor = new JenaHiveIndexerVisitor(
				configuration);

		visitor.invoke(null, invoker);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvokeNullInvoker() throws Exception {
		String testCollection = "/xxx/home/testSaveOrUpdateVocabularyTerm";

		JenaHiveConfiguration configuration = new JenaHiveConfiguration();

		JenaHiveIndexerVisitor visitor = new JenaHiveIndexerVisitor(
				configuration);

		MetaDataAndDomainData metadata = MetaDataAndDomainData.instance(
				MetadataDomain.COLLECTION, "1", testCollection,
				"http://www.fao.org/aos/agrovoc#c_49830", "blah", "blah");

		visitor.invoke(metadata, null);
	}
}
