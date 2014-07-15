package org.irods.jargon.hive.external.indexer.modelservice;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.dataprofile.DataProfileService;
import org.irods.jargon.dataprofile.DataProfileServiceImpl;
import org.irods.jargon.dataprofile.DataTypeResolutionService;
import org.irods.jargon.dataprofile.DataTypeResolutionServiceImpl;
import org.irods.jargon.hive.external.indexer.HiveTripleStoreInitializer;
import org.irods.jargon.hive.external.indexer.HiveTripleStoreInitializerImpl;
import org.irods.jargon.hive.external.indexer.JenaHiveIndexerVisitorTest;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration.JenaModelType;
import org.irods.jargon.hive.external.utils.test.Jargon3StoreTestingHelper;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.shared.Lock;

import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

public class IrodsJenaModelUpdaterTest {

	private static Properties testingProperties = new Properties();
	private static TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();
	private static org.irods.jargon.testutils.filemanip.ScratchFileUtils scratchFileUtils = null;
	public static final String IRODS_TEST_SUBDIR_PATH = "IrodsJenaModelUpdaterTest";
	private static org.irods.jargon.testutils.IRODSTestSetupUtilities irodsTestSetupUtilities = null;
	private static IRODSFileSystem irodsFileSystem = null;
	private static HiveTestingPropertiesHelper hiveTestingPropertiesHelper;
	private static File jenaVocabFile = null;
	private static File ontFile = null;
	private static JenaHiveConfiguration jenaHiveConfiguration;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		hiveTestingPropertiesHelper = new HiveTestingPropertiesHelper();
		testingProperties = testingPropertiesHelper.getTestProperties();
		scratchFileUtils = new org.irods.jargon.testutils.filemanip.ScratchFileUtils(
				testingProperties);
		scratchFileUtils
				.clearAndReinitializeScratchDirectory(IRODS_TEST_SUBDIR_PATH);
		irodsTestSetupUtilities = new org.irods.jargon.testutils.IRODSTestSetupUtilities();
		irodsTestSetupUtilities.initializeIrodsScratchDirectory();
		irodsTestSetupUtilities
				.initializeDirectoryForTest(IRODS_TEST_SUBDIR_PATH);
		irodsFileSystem = IRODSFileSystem.instance();

		// initialize a testing model in a test subdir

		ClassLoader loader = JenaHiveIndexerVisitorTest.class.getClassLoader();
		URL resc = loader.getResource("uat.rdf");

		if (resc == null) {
			throw new Exception("unable to load uat");
		}

		String vocabFileName = resc.getFile();

		jenaVocabFile = new File(vocabFileName);

		if (!jenaVocabFile.exists()) {
			throw new Exception("unable to load agrovoc test vocabulary");
		}

		URL ont = loader.getResource("irodsSchema.xml");

		if (ont == null) {
			throw new Exception("unable to load ont");
		}

		String ontFileName = ont.getFile();

		ontFile = new File(ontFileName);

		if (!ontFile.exists()) {
			throw new Exception("unable to load irods ontology");
		}

		List<String> vocabFileNames = new ArrayList<String>();
		vocabFileNames.add(vocabFileName);

		jenaHiveConfiguration = new JenaHiveConfiguration();
		jenaHiveConfiguration.setAutoCloseJenaModel(false);
		jenaHiveConfiguration
				.setIdropContext("http://localhost:8080/idrop-web/");
		jenaHiveConfiguration.setIrodsRDFFileName(ontFileName);
		jenaHiveConfiguration.setJenaModelType(JenaModelType.DATABASE_ONT);
		jenaHiveConfiguration.setVocabularyRDFFileNames(vocabFileNames);
		jenaHiveConfiguration
				.setJenaDbDriverClass(testingProperties
						.getProperty(Jargon3StoreTestingHelper.INDEXER_DB_DRIVER_CLASS));
		jenaHiveConfiguration.setJenaDbPassword(testingProperties
				.getProperty(Jargon3StoreTestingHelper.INDEXER_DB_PASSWORD));
		jenaHiveConfiguration.setJenaDbType(testingProperties
				.getProperty(Jargon3StoreTestingHelper.INDEXER_DB_TYPE));
		jenaHiveConfiguration.setJenaDbUri(Jargon3StoreTestingHelper
				.buildJdbcUriFromProperties(testingProperties,
						IRODS_TEST_SUBDIR_PATH + "/database/"));
		jenaHiveConfiguration.setJenaDbUser(testingProperties
				.getProperty(Jargon3StoreTestingHelper.INDEXER_DB_USER));

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@Test
	public void testAddIrodsTermToDataObjectThenQuery() throws Exception {
		String fileName = "testAddIrodsTermToDataObjectThenQuery.txt";
		String vocabTerm = "http://purl.org/astronomy/uat#T351";
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		DataTypeResolutionService dataTypeResolutionService = new DataTypeResolutionServiceImpl();

		DataProfileService dataProfileService = new DataProfileServiceImpl(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount,
				dataTypeResolutionService);

		String absPath = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(
						testingProperties, IRODS_TEST_SUBDIR_PATH)
				+ "/" + fileName;

		IRODSFile irodsFile = irodsFileSystem.getIRODSAccessObjectFactory()
				.getIRODSFileFactory(irodsAccount).instanceIRODSFile(absPath);
		irodsFile.createNewFile();

		// create an in memory ont
		HiveTripleStoreInitializer hiveTripleStoreInitializer = new HiveTripleStoreInitializerImpl(
				jenaHiveConfiguration);
		OntModel ontModel = hiveTripleStoreInitializer.initialize();

		IrodsJenaModelUpdater irodsJenaModelUpdater = new IrodsJenaModelUpdater(
				irodsFileSystem.getIRODSAccessObjectFactory(), irodsAccount,
				ontModel, jenaHiveConfiguration);
		irodsJenaModelUpdater.addIrodsTerm(absPath, vocabTerm);

		// now sparql query the term
		IrodsJenaModelQuery irodsJenaModelQuery = new IrodsJenaModelQueryImpl(
				ontModel);
		ResultSet queryResult = irodsJenaModelQuery
				.queryAllOnVocabularyTerm(vocabTerm);
		Assert.assertNotNull("null result set from query", queryResult);
		boolean foundit = false;

		try {

			ontModel.enterCriticalSection(Lock.READ);

			if (!queryResult.hasNext()) {
				Assert.fail("empty results");
			}

			while (queryResult.hasNext()) {
				QuerySolution nextSolution = queryResult.next();
				foundit = true;
			}
		} finally {
			ontModel.leaveCriticalSection();
		}

		Assert.assertTrue("didn't find any result", foundit);

	}
}
