package org.irods.jargon.hive.service;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.exception.VocabularyNotFoundException;
import org.unc.hive.client.ConceptProxy;

import edu.unc.ils.mrc.hive.api.ConceptNode;
import edu.unc.ils.mrc.hive.api.SKOSConcept;
import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.api.SKOSSearcher;
import edu.unc.ils.mrc.hive.api.SKOSServer;
import edu.unc.ils.mrc.hive.ir.lucene.search.AutocompleteTerm;

public class VocabService implements VocabularyService {
	private static final Log logger = LogFactory
			.getLog(VocabularyServiceImpl.class); // jpb
	private static VocabularyService instance = null;
	private HiveContainer hiveContainer = null;

	private final int DEFAULT_MIN_OCCUR = 2;
	
	public VocabService (final HiveContainer hiveContainer) {
		this.hiveContainer = hiveContainer;
	}
	
	private SKOSServer getSkosServer() {
		return hiveContainer.getSkosServer();
	}
	
	public void setHiveContainer(final HiveContainer hiveContainer) {
		this.hiveContainer = hiveContainer;
	}

	@Override
	public SKOSSearcher getSKOSSearcher() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getNumberOfConcept(String vocabularyName)
			throws VocabularyNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getNumerOfRelations(String vocabularyName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Date getLastUpdateDate(String vocabularyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<String>> getAllVocabularies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SKOSScheme getVocabularyByName(String vocabularyName)
			throws VocabularyNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllVocabularyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, HashMap<String, String>> getVocabularyProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConceptProxy> getSubTopConcept(String vocabulary,
			String letter, boolean brief) throws VocabularyNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConceptProxy> getChildConcept(String nameSpaceURI,
			String localPart) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ConceptProxy> searchConcept(String keyword,
			List<String> openedVocabularies) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConceptProxy getConceptByURI(String namespaceURI, String localPart) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConceptProxy> getTags(String input,
			List<String> openedVocabularies, int numTerms, String algorithm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConceptProxy> getTags(String input,
			List<String> openedVocabularies, int numTerms, int minPhraseOccur,
			String algorithm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getStringMap(Map<String, QName> qnameMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConceptProxy> getTags(URL url, List<String> openedVocabularies,
			int maxHops, int numTerms, boolean diff, int minOccur,
			String algorithm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConceptNode> getTagsAsTree(String text,
			List<String> openedVocabularies, int maxHops, int numTerms,
			String algorithm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConceptProxy getFirstConcept(String vocabulary) {
		// TODO Auto-generated method stub
		TreeMap<String, SKOSScheme> vocabularyMap = getSkosServer()
				.getSKOSSchemas();
		SKOSScheme voc = vocabularyMap.get(vocabulary.toLowerCase());
		List<SKOSConcept> top = voc.getSubTopConceptIndex("a");
		QName value = top.get(0).getQName();
		ConceptProxy cp = getConceptByURI(value.getNamespaceURI(),
				value.getLocalPart());
		return cp;
	}

	@Override
	public List<AutocompleteTerm> suggestTermsFor(String vocabulary,
			String str, int numTerms) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ConceptProxy getConceptProxyForTopOfVocabulary(String vocabulary,
			String letter, boolean brief) throws VocabularyNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	

}

