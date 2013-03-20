package org.irods.jargon.hive.external.indexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.CollectionAO;
import org.irods.jargon.core.pub.DataObjectAO;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.query.AVUQueryElement;
import org.irods.jargon.core.query.MetaDataAndDomainData;
import org.irods.jargon.core.query.MetaDataAndDomainData.MetadataDomain;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitorInvoker.VisitorDesiredAction;
import org.irods.jargon.hive.irods.IRODSHiveServiceImpl;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class JenaHiveIndexerInvokerTest {
	private static Properties testingProperties = new Properties();
	private static org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesLoader = new TestingPropertiesHelper();
		testingProperties = testingPropertiesLoader.getTestProperties();
	}

	@Test
	public void testOnePageCollsOnePageDataObjects() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory irodsAccessObjectFactory = Mockito
				.mock(IRODSAccessObjectFactory.class);
		JenaHiveIndexerVisitor visitor = Mockito
				.mock(JenaHiveIndexerVisitor.class);
		Mockito.when(
				visitor.invoke(Mockito.any(MetaDataAndDomainData.class),
						Mockito.any(JenaHiveIndexerInvoker.class))).thenReturn(
				VisitorDesiredAction.CONTINUE);

		List<MetaDataAndDomainData> collectionMetadata = new ArrayList<MetaDataAndDomainData>();
		MetaDataAndDomainData collectionMetadataValue = MetaDataAndDomainData
				.instance(MetadataDomain.COLLECTION, "1", "/a/collection",
						"http://www.fao.org/aos/agrovoc#c_49830", "blah",
						"blah");
		collectionMetadataValue.setCount(1);
		collectionMetadataValue.setLastResult(true);
		collectionMetadata.add(collectionMetadataValue);
		CollectionAO collectionAO = Mockito.mock(CollectionAO.class);
		List<AVUQueryElement> query = IRODSHiveServiceImpl
				.buildQueryToFindHiveMetadata();
		Mockito.when(collectionAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(collectionMetadata);

		List<MetaDataAndDomainData> dataObjectMetadata = new ArrayList<MetaDataAndDomainData>();
		MetaDataAndDomainData dataObjectMetadataValue = MetaDataAndDomainData
				.instance(MetadataDomain.DATA, "1", "/a/collection",
						"http://www.fao.org/aos/agrovoc#c_49830", "blah",
						"blah");
		dataObjectMetadataValue.setCount(1);
		dataObjectMetadataValue.setLastResult(true);
		dataObjectMetadata.add(dataObjectMetadataValue);
		DataObjectAO dataObjectAO = Mockito.mock(DataObjectAO.class);

		Mockito.when(dataObjectAO.findMetadataValuesByMetadataQuery(query, 0))
				.thenReturn(dataObjectMetadata);

		Mockito.when(irodsAccessObjectFactory.getDataObjectAO(irodsAccount))
				.thenReturn(dataObjectAO);
		Mockito.when(irodsAccessObjectFactory.getCollectionAO(irodsAccount))
				.thenReturn(collectionAO);

		JenaHiveIndexerInvoker invoker = new JenaHiveIndexerInvoker(
				irodsAccessObjectFactory, irodsAccount, visitor);
		invoker.execute();

		Assert.assertEquals(1, invoker.getCollectionsProcessed());
		Assert.assertEquals(1, invoker.getDataObjectsProcessed());

	}
}
