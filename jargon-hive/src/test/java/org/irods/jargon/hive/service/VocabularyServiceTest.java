package org.irods.jargon.hive.service;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.irods.jargon.hive.container.HiveConfiguration;
import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.container.HiveContainerImpl;
import org.irods.jargon.hive.exception.VocabularyNotFoundException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.unc.hive.client.ConceptProxy;

import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.ir.lucene.search.AutocompleteTerm;

public class VocabularyServiceTest {

	static final HiveConfiguration hiveConfiguration = new HiveConfiguration();
	static final HiveContainer hiveContainer = new HiveContainerImpl();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		hiveConfiguration
				.setHiveConfigLocation("/Users/mikeconway/temp/hive-data/hive.properties");
		// hiveConfiguration
		// .setHiveConfigLocation("/Users/zhangle/temp/hive-data/hive.properties");

		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		hiveContainer.shutdown();
	}

	// tested
	@Test
	public void testGetAgrovoc() throws Exception {
		// Assert.assertNotNull("did not start skos server",
		// hiveContainer.getSkosServer());
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);

		SKOSScheme actual = vocabularyService.getVocabularyByName("agrovoc");
		TestCase.assertNotNull("did not get vocab", actual);
	}
	
	@Test
	public void testGetUAT() throws Exception {
		// Assert.assertNotNull("did not start skos server",
		// hiveContainer.getSkosServer());
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);

		SKOSScheme actual = vocabularyService.getVocabularyByName("uat");
		TestCase.assertNotNull("did not get vocab", actual);
	}
	

	// tested
	@Test
	public void testGetFirstConcept() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		ConceptProxy x = vocabularyService.getFirstConcept("agrovoc");
		TestCase.assertNotNull("did not load first concept", x);
	}

	// tested
	@Test
	public void testSuggestTermsFor() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		List<AutocompleteTerm> x = null;
		x = vocabularyService.suggestTermsFor("agrovoc", "ability", 3);
		TestCase.assertFalse("did not find suggested terms", x == null);
	}

	// tested
	@Test
	public void testListVocabularyNames() throws Exception {
		// Assert.assertNotNull("did not start skos server",
		// hiveContainer.getSkosServer());
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);

		List<String> allVocabNames = vocabularyService.getAllVocabularyNames();
		TestCase.assertFalse("did not load vocabularies",
				allVocabNames.isEmpty());
	}

	// tested
	@Test
//	@Ignore
	public void testgetConceptByURI() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		ConceptProxy X = vocabularyService.getConceptByURI(
				"http://www.fao.org/aos/agrovoc#", "c_49830");
		TestCase.assertFalse("did not get concept by URI", X == null);
	}

	@Test
	public void testSearchConcept() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		Set<ConceptProxy> rankedSet = null;
		List<String> openVocabularies = vocabularyService
				.getAllVocabularyNames();
		rankedSet = vocabularyService.searchConcept("ability",
				openVocabularies);
		TestCase.assertFalse("did not find concept", rankedSet.isEmpty());
	}

	// tested
	@Test
//	@Ignore
	public void testGetChildConcept() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		List<ConceptProxy> ChildrenList = null;
		ChildrenList = vocabularyService.getChildConcept(
				"http://www.fao.org/aos/agrovoc#", "c_49830");
		TestCase.assertFalse("did not load child concept",
				ChildrenList.isEmpty());
	}

	// tested
	@Test
	public void testGetSubTopConcept() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		List<ConceptProxy> fatherList = null;
		fatherList = vocabularyService.getSubTopConcept("agrovoc", "A", true);
		TestCase.assertFalse("did not load sub top concept",
				fatherList.isEmpty());
	}

	@Test
	public void testGetSubTopConceptWithBriefSettoFalse() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		List<ConceptProxy> fatherList = null;
		fatherList = vocabularyService.getSubTopConcept("agrovoc", "A", false);
		TestCase.assertFalse("did not load sub top concept",
				fatherList.isEmpty());
	}

	@Test
	public void testGetConceptProxyForTopOfVocabulary() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		ConceptProxy proxy = vocabularyService
				.getConceptProxyForTopOfVocabulary("agrovoc", "", true);
		Assert.assertNotNull("null proxy", proxy);
		Assert.assertTrue("did not set as top level", proxy.isTopLevel());
		Assert.assertEquals("did not set vocab name", "uat",
				proxy.getOrigin());
		Assert.assertFalse("did not set child (narrower) terms", proxy
				.getNarrower().isEmpty());
	}

	@Test(expected = VocabularyNotFoundException.class)
	public void testGetConceptProxyForTopOfVocabularyNotExists()
			throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		vocabularyService.getConceptProxyForTopOfVocabulary("bogusvocabhere",
				"", true);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetConceptProxyForTopOfVocabularyNullVocabNullName()
			throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		vocabularyService.getConceptProxyForTopOfVocabulary(null, "", true);
	}

	// @Test(expected = IllegalArgumentException.class)
	// public void testGetConceptProxyForTopOfVocabularyNullVocabEmptyName()
	// throws Exception {
	// VocabularyService vocabularyService = new VocabularyServiceImpl(
	// hiveContainer);
	// vocabularyService.getConceptProxyForTopOfVocabulary("", "", true);
	// }

	// tested
	@Test
	public void testGetNumberOfConceptAndRelations() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		long x = vocabularyService.getNumberOfConcept("agrovoc");
		long y = vocabularyService.getNumerOfRelations("agrovoc");
		System.out.println(x);
		System.out.println(y);
	}

	// tested
	@Test
	public void testGetLastUpdate() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		vocabularyService.getLastUpdateDate("agrovoc");
	}

	@Test
	public void testGetAllVocabulary() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		List<List<String>> allVocabs = vocabularyService.getAllVocabularies();

		TestCase.assertFalse(
				"did not load vocabularies with properties to List",
				allVocabs.isEmpty());
		// TestCase.assertTrue("did not load all vocabularies", allVocabs.size()
		// == vocabularyService.getNumberOfConcept("agrovoc"));
	}

	@Test
	public void testGetVocabularyProperties() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		HashMap<String, HashMap<String, String>> vocabProps = vocabularyService
				.getVocabularyProperties();
		TestCase.assertFalse("did not load vocabulary properties",
				vocabProps.isEmpty());
	}

	//@Test
	@Ignore
	public void testGetTagsFromInput() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		List<String> openVocabularies = vocabularyService
				.getAllVocabularyNames();
		List<ConceptProxy> cp = vocabularyService.getTags(
				"/Users/zhangle/temp/hive-data/agrovoc/agrovoc.rdf",
				openVocabularies, 28174, "kea");
		TestCase.assertFalse("did not get Tags", cp.isEmpty());
	}

	//@Test
	@Ignore
	public void testGetTagsFromURL() throws Exception {
		VocabularyService vocabularyService = new VocabularyServiceImpl(
				hiveContainer);
		List<String> openVocabularies = vocabularyService
				.getAllVocabularyNames();
		List<ConceptProxy> cp = vocabularyService.getTags(new URL(
				"http://en.wikipedia.org/wiki/Rice"), openVocabularies, 2, 200000,
				true, 2, "kea");
		TestCase.assertFalse("did not get Tags", cp.isEmpty());
	}

}
