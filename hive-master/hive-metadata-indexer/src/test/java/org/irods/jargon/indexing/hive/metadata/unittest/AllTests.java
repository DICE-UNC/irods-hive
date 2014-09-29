package org.irods.jargon.indexing.hive.metadata.unittest;

import org.irods.jargon.indexing.hive.metadata.HiveMetadataIndexerTest;
import org.irods.jargon.indexing.hive.metadata.utils.HiveMetadataIndexerInitializerImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ HiveMetadataIndexerTest.class,
		HiveMetadataIndexerInitializerImplTest.class })
public class AllTests {

}
