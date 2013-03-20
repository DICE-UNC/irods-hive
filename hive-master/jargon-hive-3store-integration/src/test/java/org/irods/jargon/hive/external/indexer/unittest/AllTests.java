package org.irods.jargon.hive.external.indexer.unittest;

import org.irods.jargon.hive.external.indexer.JenaHiveIndexerInvokerTest;
import org.irods.jargon.hive.external.indexer.JenaHiveIndexerVisitorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite to run all tests
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ JenaHiveIndexerVisitorTest.class,
		JenaHiveIndexerInvokerTest.class })
public class AllTests {

}
