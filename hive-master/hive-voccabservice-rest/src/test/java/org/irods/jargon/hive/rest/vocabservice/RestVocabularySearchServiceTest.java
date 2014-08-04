package org.irods.jargon.hive.rest.vocabservice;

import static org.junit.Assert.fail;

import java.util.Properties;

import org.irods.jargon.hive.rest.vocabservice.utils.RestTestingProperties;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.jboss.resteasy.plugins.spring.SpringBeanProcessor;
import org.jboss.resteasy.plugins.spring.SpringResourceFactory;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import edu.unc.ils.mrc.hive.testframework.HiveScratchAreaCreator;
import edu.unc.ils.mrc.hive.unittest.utils.HiveTestingPropertiesHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:jargon-beans.xml",
		"classpath:rest-servlet.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class })
public class RestVocabularySearchServiceTest implements ApplicationContextAware {

	@SuppressWarnings("unused")
	private static HiveScratchAreaCreator hiveScratchAreaCreator = null;
	private static TJWSEmbeddedJaxrsServer server;
	private static ApplicationContext applicationContext;
	private static Properties testingProperties = new Properties();
	private static HiveTestingPropertiesHelper testingPropertiesHelper = new HiveTestingPropertiesHelper();
	public static final String IRODS_TEST_SUBDIR_PATH = "RestVocabularySearchServiceTest";

	@Override
	public void setApplicationContext(final ApplicationContext context)
			throws BeansException {
		applicationContext = context;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testingProperties = testingPropertiesHelper.getTestProperties();
		hiveScratchAreaCreator = new HiveScratchAreaCreator(testingProperties);

		if (testingPropertiesHelper.checkTestHiveFuntionalSetup() == false) {
			throw new Exception(
					"test hive not set up, run HiveTestInstanceSetup");
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (server != null) {
			server.stop();
		}
	}

	@Before
	public void setUp() throws Exception {

		if (server != null) {
			return;
		}

		int port = testingPropertiesHelper.getPropertyValueAsInt(
				testingProperties, RestTestingProperties.REST_PORT_PROPERTY);
		server = new TJWSEmbeddedJaxrsServer();
		server.setPort(port);
		ResteasyDeployment deployment = server.getDeployment();

		server.start();
		Dispatcher dispatcher = deployment.getDispatcher();
		SpringBeanProcessor processor = new SpringBeanProcessor(dispatcher,
				deployment.getRegistry(), deployment.getProviderFactory());
		((ConfigurableApplicationContext) applicationContext)
				.addBeanFactoryPostProcessor(processor);

		SpringResourceFactory noDefaults = new SpringResourceFactory(
				"restVocabularySearchService", applicationContext,
				RestVocabularySearchService.class);
		dispatcher.getRegistry().addResourceFactory(noDefaults);

	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
