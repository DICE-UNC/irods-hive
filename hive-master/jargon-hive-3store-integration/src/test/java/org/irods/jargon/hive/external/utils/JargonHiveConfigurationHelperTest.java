package org.irods.jargon.hive.external.utils;

import java.util.Properties;

import junit.framework.Assert;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.hive.external.indexer.HiveIndexerException;
import org.junit.Test;

public class JargonHiveConfigurationHelperTest {
	
	
	@Test
	public void testBuildIrodsAccountFromProperties() throws HiveIndexerException {
		Properties properties = new Properties();
		String host = "host";
		String password = "password";
		String user = "user";
		String port = "1247";
		String zone = "zone";
		
		properties.put(JargonHiveConfigurationHelper.IRODS_HOST, host);
		properties.put(JargonHiveConfigurationHelper.IRODS_PORT, port);
		properties.put(JargonHiveConfigurationHelper.IRODS_USER, user);
		properties.put(JargonHiveConfigurationHelper.IRODS_PASSWORD, password);
		properties.put(JargonHiveConfigurationHelper.IRODS_ZONE, zone);
		
		IRODSAccount actual = JargonHiveConfigurationHelper.buildIRODSAccountFromProperties(properties);
		
		Assert.assertNotNull("null irodsAccount", actual);
		Assert.assertEquals(host, actual.getHost());
		Assert.assertEquals(password, actual.getPassword());
		Assert.assertEquals(user, actual.getUserName());
		Assert.assertEquals(Integer.parseInt(port), actual.getPort());
		Assert.assertEquals(zone, actual.getZone());
		
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
		
		properties.put(JargonHiveConfigurationHelper.INDEXER_DB_DRIVER_CLASS, driver);
		properties.put(JargonHiveConfigurationHelper.INDEXER_DB_PASSWORD, password);
		properties.put(JargonHiveConfigurationHelper.INDEXER_DB_TYPE, type);
		properties.put(JargonHiveConfigurationHelper.INDEXER_DB_URI, url);
		properties.put(JargonHiveConfigurationHelper.INDEXER_DB_USER, user);
		properties.put(JargonHiveConfigurationHelper.INDEXER_IDROP_CONTEXT, context);
		
		JenaHiveConfiguration actual = JargonHiveConfigurationHelper.buildJenaHiveConfigurationFromProperties(properties);
		Assert.assertNotNull("null config", actual);
		Assert.assertEquals(url, actual.getJenaDbUri());
		Assert.assertEquals(password, actual.getJenaDbPassword());
		Assert.assertEquals(user, actual.getJenaDbUser());
		Assert.assertEquals(driver, actual.getJenaDbDriverClass());
		Assert.assertEquals(type, actual.getJenaDbType());
		Assert.assertEquals(context, actual.getIdropContext());
		
	}

}
