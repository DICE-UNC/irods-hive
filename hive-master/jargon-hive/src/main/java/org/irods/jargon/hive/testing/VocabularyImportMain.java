package org.irods.jargon.hive.testing;

import java.util.Properties;

import kea.vocab.VocabularyH2;

import org.irods.jargon.hive.container.HiveConfiguration;
import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.container.HiveContainerImpl;
import org.irods.jargon.testutils.TestingUtilsException;

import edu.unc.ils.mrc.hive.HiveException;
import edu.unc.ils.mrc.hive.admin.TaggerTrainer;
import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.api.impl.elmo.SKOSSchemeImpl;
import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

public class VocabularyImportMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		Properties testingProperties = new Properties();
		//final HiveTestingPropertiesHelper testingPropertiesHelper = new HiveTestingPropertiesHelper();
		
		//testingProperties = testingPropertiesHelper.getTestProperties();
		
		//String hivePath = "/usr/local/hive/hive-data";
		String hivePath = "/Users/zhangle/temp/hive-data";
		
		//String hivePath = testingProperties
				//.getProperty(HiveTestingPropertiesHelper.TEST_HIVE_PARENT_DIR);
		SKOSScheme schema = new SKOSSchemeImpl(hivePath, "uat", true);

		schema.importConcepts(schema.getRdfPath(), true, true, true, true, true);

		VocabularyH2 keaH2 = new VocabularyH2(schema, "en");
		keaH2.initialize();

		// fill in some values from the test properties

		TaggerTrainer trainer = new TaggerTrainer(schema);

		trainer.trainKEAAutomaticIndexingModule();
		// test some stuff to see if it worked

		HiveConfiguration hiveConfiguration;
		HiveContainer hiveContainer = new HiveContainerImpl();

		hiveConfiguration = new HiveConfiguration();
		hiveConfiguration.setHiveConfigLocation(hivePath + "/hive.properties");
		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();
		
		
		
		
	}

}
