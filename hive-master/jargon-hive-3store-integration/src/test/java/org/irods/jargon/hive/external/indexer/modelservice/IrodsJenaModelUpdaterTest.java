package org.irods.jargon.hive.external.indexer.modelservice;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hp.hpl.jena.ontology.OntModel;

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
		irodsTestSetupUtilities = new org.irods.jargon.testutils.IRODSTestSetupUtilities();
		irodsTestSetupUtilities.initializeIrodsScratchDirectory();
		irodsTestSetupUtilities
				.initializeDirectoryForTest(IRODS_TEST_SUBDIR_PATH);
		irodsFileSystem = IRODSFileSystem.instance();

		// initialize a testing model in a test subdir

		ClassLoader loader = JenaHiveIndexerVisitorTest.class.getClassLoader();
		URL resc = loader.getResource("agrovoc.rdf");

		if (resc == null) {
			throw new Exception("unable to load agrovoc");
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
		jenaHiveConfiguration.setJenaModelType(JenaModelType.MEMORY_ONT);
		jenaHiveConfiguration.setVocabularyRDFFileNames(vocabFileNames);

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@Test
	public void testAddIrodsTermToDataObject() throws Exception {
		String fileName = "testAddIrodsTermToDataObject.txt";
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

		// create an in memory ont
		HiveTripleStoreInitializer hiveTripleStoreInitializer = new HiveTripleStoreInitializerImpl(
				jenaHiveConfiguration);
		OntModel ontModel = hiveTripleStoreInitializer.initialize();
		String val = ontModel.toString();

	}
}
