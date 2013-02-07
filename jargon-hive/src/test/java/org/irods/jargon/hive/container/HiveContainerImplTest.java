package org.irods.jargon.hive.container;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HiveContainerImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInit() throws Exception {
		HiveConfiguration hiveConfiguration = new HiveConfiguration();
		hiveConfiguration
				.setHiveConfigLocation("/Users/mikeconway/temp/hive-data/hive.properties");
		HiveContainer hiveContainer = new HiveContainerImpl();
		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();
		Assert.assertNotNull("did not start skos server",
				hiveContainer.getSkosServer());

	}

	@Test
	public void testShutdown() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSkosServer() {
		fail("Not yet implemented");
	}

}
