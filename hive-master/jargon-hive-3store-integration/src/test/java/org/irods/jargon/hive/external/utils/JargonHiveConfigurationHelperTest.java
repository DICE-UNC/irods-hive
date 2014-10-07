package org.irods.jargon.hive.external.utils;

import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.hive.external.indexer.HiveIndexerException;
import org.junit.Test;

public class JargonHiveConfigurationHelperTest {

	@Test
	public void testBuildIrodsAccountFromProperties()
			throws HiveIndexerException {
		Properties properties = new Properties();
		String host = "host";
		String password = "password";
		String user = "user";
		String port = "1247";
		String zone = "zone";

		properties.put(JenaHiveConfigurationHelper.IRODS_HOST, host);
		properties.put(JenaHiveConfigurationHelper.IRODS_PORT, port);
		properties.put(JenaHiveConfigurationHelper.IRODS_USER, user);
		properties.put(JenaHiveConfigurationHelper.IRODS_PASSWORD, password);
		properties.put(JenaHiveConfigurationHelper.IRODS_ZONE, zone);

		IRODSAccount actual = JenaHiveConfigurationHelper
				.buildIRODSAccountFromProperties(properties);

		Assert.assertNotNull("null irodsAccount", actual);
		Assert.assertEquals(host, actual.getHost());
		Assert.assertEquals(password, actual.getPassword());
		Assert.assertEquals(user, actual.getUserName());
		Assert.assertEquals(Integer.parseInt(port), actual.getPort());
		Assert.assertEquals(zone, actual.getZone());

	}

	@Test
	public void testCreateListVocabs() {
		String vocabString = "/a/vocab1/vocab.rdf,/a/vocab2/vocab2.rdf,/another/vocab/here";
		List<String> vocabList = JenaHiveConfigurationHelper
				.buildListOfVocabularyFileNamesFromCommaDelimitedString(vocabString);
		Assert.assertFalse("empty vocabList", vocabList.isEmpty());
	}

	@Test
	public void testBuildJenaHiveConfigurationFromProperties() {
		Properties properties = new Properties();
		String url = "url";
		String password = "password";
		String user = "user";
		String driver = "driver";
		String type = "type";
		String context = "context";

		properties.put(JenaHiveConfigurationHelper.INDEXER_DB_DRIVER_CLASS,
				driver);
		properties.put(JenaHiveConfigurationHelper.INDEXER_DB_PASSWORD,
				password);
		properties.put(JenaHiveConfigurationHelper.INDEXER_DB_TYPE, type);
		properties.put(JenaHiveConfigurationHelper.INDEXER_DB_URI, url);
		properties.put(JenaHiveConfigurationHelper.INDEXER_DB_USER, user);
		properties.put(JenaHiveConfigurationHelper.INDEXER_IDROP_CONTEXT,
				context);

		JenaHiveConfiguration actual = JenaHiveConfigurationHelper
				.buildJenaHiveConfigurationFromProperties(properties);
		Assert.assertNotNull("null config", actual);
		Assert.assertEquals(url, actual.getJenaDbUri());
		Assert.assertEquals(password, actual.getJenaDbPassword());
		Assert.assertEquals(user, actual.getJenaDbUser());
		Assert.assertEquals(driver, actual.getJenaDbDriverClass());
		Assert.assertEquals(type, actual.getJenaDbType());
		Assert.assertEquals(context, actual.getIdropContext());

	}

}
