package org.irods.jargon.hive.service;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import edu.unc.ils.mrc.hive.api.SKOSTagger;
import edu.unc.ils.mrc.hive.ir.lucene.search.AutocompleteTerm;

/**
 * Refactored version originally found in hive-web from the original HIVE
 * project
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class VocabularyServiceImpl implements VocabularyService {
	private static final Log logger = LogFactory
			.getLog(VocabularyServiceImpl.class); // jpb
	private static VocabularyService instance = null;
	private HiveContainer hiveContainer = null;

	private final int DEFAULT_MIN_OCCUR = 2;

	@Override
	public void setHiveContainer(HiveContainer hiveContainer) {
		this.hiveContainer = hiveContainer;
	}

	public VocabularyServiceImpl() {

	}

	public VocabularyServiceImpl(final HiveContainer hiveContainer) {
		this.hiveContainer = hiveContainer;
	}

	private SKOSServer getSkosServer() {
		return this.hiveContainer.getSkosServer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.service.VocabularyService#getSKOSSearcher()
	 */
	@Override
	public SKOSSearcher getSKOSSearcher() {
		return this.hiveContainer.getSkosServer().getSKOSSearcher();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getNumberOfConcept(java
	 * .lang.String)
	 */
	@Override
	public long getNumberOfConcept(String vocabularyName)
			throws VocabularyNotFoundException {
		SKOSScheme vocab = getSkosServer().getSKOSSchemas().get(vocabularyName);
		if (vocab == null) {
			throw new VocabularyNotFoundException("did not find:"
					+ vocabularyName);
		}
		return vocab.getNumberOfConcepts();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getNumerOfRelations(java
	 * .lang.String)
	 */
	@Override
	public long getNumerOfRelations(String vocabularyName) {
		return getSkosServer().getSKOSSchemas().get(vocabularyName)
				.getNumberOfRelations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getLastUpdateDate(java
	 * .lang.String)
	 */
	@Override
	public Date getLastUpdateDate(String vocabularyName) {
		return this.getSkosServer().getSKOSSchemas().get(vocabularyName)
				.getLastUpdateDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.service.VocabularyService#getAllVocabularies()
	 */
	@Override
	public List<List<String>> getAllVocabularies() {
		logger.info("getAllVocabularies()");
		TreeMap<String, SKOSScheme> vocabularyMap = this.getSkosServer()
				.getSKOSSchemas();
		logger.info("vocabMap from skosServer}" + vocabularyMap);
		List<List<String>> vocabularyList = new ArrayList<List<String>>();
		Set<String> vnames = vocabularyMap.keySet();
		Iterator<String> it = vnames.iterator();
		logger.info("getting ready to iterate through vocabularies...");
		while (it.hasNext()) {
			logger.info("vocab:" + it);
			SKOSScheme vocabulary = vocabularyMap.get(it.next());

			List<String> vocabularyInfo = new ArrayList<String>();
			vocabularyInfo.add(vocabulary.getName());
			vocabularyInfo.add(Long.toString(vocabulary.getNumberOfConcepts()));
			vocabularyInfo
					.add(Long.toString(vocabulary.getNumberOfRelations()));
			Date lastUpdate = vocabulary.getLastUpdateDate();
			DateFormat df = new SimpleDateFormat("MMM d, yyyy");
			String date = df.format(lastUpdate);
			vocabularyInfo.add(date);
			vocabularyList.add(vocabularyInfo);
		}
		return vocabularyList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getVocabularyByName(java
	 * .lang.String)
	 */
	@Override
	public SKOSScheme getVocabularyByName(final String vocabularyName)
			throws VocabularyNotFoundException {
		logger.info("getVocabularyByName()");

		if (vocabularyName == null || vocabularyName.isEmpty()) {
			throw new IllegalArgumentException("null or empty vocabularyName");
		}

		logger.info("looking for name:" + vocabularyName);

		SKOSScheme vocab = this.getSkosServer().getSKOSSchemas()
				.get(vocabularyName);

		if (vocab == null) {
			throw new VocabularyNotFoundException(
					"did not find requested vocabulary in HIVE");
		}

		return vocab;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getAllVocabularyNames()
	 */
	@Override
	public List<String> getAllVocabularyNames() {
		logger.info("getAllVocabularyNames()");
		TreeMap<String, SKOSScheme> vocabularyMap = this.getSkosServer()
				.getSKOSSchemas();
		Set<String> keys = vocabularyMap.keySet();
		List<String> names = new ArrayList<String>();
		for (String key : keys) {
			names.add(key.toUpperCase());
		}

		logger.info("names:" + names);
		return names;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getVocabularyProperties()
	 */
	@Override
	public HashMap<String, HashMap<String, String>> getVocabularyProperties() {
		HashMap<String, HashMap<String, String>> props = new HashMap<String, HashMap<String, String>>();
		TreeMap<String, SKOSScheme> vocabularyMap = this.getSkosServer()
				.getSKOSSchemas();
		Set<String> keys = vocabularyMap.keySet();
		HashMap<String, String> propVals;
		for (String key : keys) {
			propVals = new HashMap<String, String>();
			SKOSScheme vocabulary = vocabularyMap.get(key);
			propVals.put("uri", vocabulary.getSchemaURI());
			props.put(key, propVals);
		}
		return props;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getSubTopConcept(java
	 * .lang.String, java.lang.String, boolean)
	 */
	@Override
	public List<ConceptProxy> getSubTopConcept(String vocabulary,
			String letter, boolean brief) {
		TreeMap<String, SKOSScheme> vocabularies = this.getSkosServer()
				.getSKOSSchemas();
		SKOSScheme targetVoc = vocabularies.get(vocabulary);

		List<SKOSConcept> top = targetVoc.getSubTopConceptIndex(letter);
		List<ConceptProxy> fatherList = new ArrayList<ConceptProxy>();
		for (SKOSConcept sc : top) {

			QName q = sc.getQName();
			boolean isleaf = sc.isLeaf();

			if (!brief) {
				SKOSConcept concept = this
						.getSkosServer()
						.getSKOSSearcher()
						.searchConceptByURI(q.getNamespaceURI(),
								q.getLocalPart());
				int numberOfChildren = concept.getNumberOfChildren();

				if (numberOfChildren == 0)
					isleaf = true;
			}
			String uri = q.getNamespaceURI();
			String localPart = q.getLocalPart();
			String URI = uri + " " + localPart;
			String prefLabel = sc.getPrefLabel();
			ConceptProxy father = new ConceptProxy(vocabulary, prefLabel, URI,
					isleaf);
			fatherList.add(father);
		}
		return fatherList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getChildConcept(java.
	 * lang.String, java.lang.String)
	 */

	@Override
	public List<ConceptProxy> getChildConcept(String nameSpaceURI,
			String localPart) {
		SKOSSearcher searcher = this.getSkosServer().getSKOSSearcher();
		TreeMap<String, QName> children = searcher.searchChildrenByURI(
				nameSpaceURI, localPart);
		List<ConceptProxy> childrenList = null;
		if (children.size() != 0) {
			childrenList = new ArrayList<ConceptProxy>();
			for (String cl : children.keySet()) {
				String origin = this.getSkosServer()
						.getOrigin(children.get(cl));
				String preLabel = cl;
				String namespace = children.get(cl).getNamespaceURI();
				String lp = children.get(cl).getLocalPart();
				SKOSConcept concept = this.getSkosServer().getSKOSSearcher()
						.searchConceptByURI(namespace, localPart);
				int numberOfChildren = concept.getNumberOfChildren();
				boolean isleaf = true;
				if (numberOfChildren != 0)
					isleaf = false;
				String URI = namespace + " " + lp;
				ConceptProxy cpr = new ConceptProxy(origin, preLabel, URI,
						isleaf);
				childrenList.add(cpr);
			}
		}
		return childrenList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#searchConcept(java.lang
	 * .String, java.util.List)
	 */

	@Override
	public List<ConceptProxy> searchConcept(String keyword,
			List<String> openedVocabularies) {

		// maintain the rank list
		SKOSSearcher searcher = this.getSkosServer().getSKOSSearcher();
		List<SKOSConcept> result = searcher.searchConceptByKeyword(keyword,
				true);
		List<ConceptProxy> rankedlist = new ArrayList<ConceptProxy>();
		for (String s : openedVocabularies) {
			System.out.println(s);
		}
		if (result.size() != 0) {
			for (SKOSConcept c : result) {
				String origin = getSkosServer().getOrigin(c.getQName());
				// shim for case problem in search of vocabulary service mcc
				if (openedVocabularies.contains(origin.toLowerCase())
						|| openedVocabularies.contains(origin.toUpperCase())) {
					String preLabel = c.getPrefLabel();
					QName qname = c.getQName();
					String namespace = qname.getNamespaceURI();
					String localPart = qname.getLocalPart();
					String uri = namespace + " " + localPart;
					ConceptProxy cp = new ConceptProxy(origin, preLabel, uri);
					rankedlist.add(cp);

				}
			}

		}
		return rankedlist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getConceptByURI(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public ConceptProxy getConceptByURI(String namespaceURI, String localPart) {
		SKOSSearcher searcher = this.getSkosServer().getSKOSSearcher();
		SKOSConcept concept = searcher.searchConceptByURI(namespaceURI,
				localPart);
		String preLabel = concept.getPrefLabel();
		QName q = concept.getQName();
		String origin = getSkosServer().getOrigin(q);
		String uri = q.getNamespaceURI() + q.getLocalPart();
		String skosCode = concept.getSKOSFormat();
		List<String> altLabel = concept.getAltLabels();
		List<String> scopeNotes = concept.getScopeNote();

		TreeMap<String, QName> broader = concept.getBroaders();
		Iterator<String> it = broader.keySet().iterator();
		HashMap<String, String> broaders = new HashMap<String, String>();
		while (it.hasNext()) {
			String key = it.next();
			QName qq = broader.get(key);
			String URI = qq.getNamespaceURI();
			String lp = qq.getLocalPart();
			String value = URI + " " + lp;
			broaders.put(key, value);
		}
		TreeMap<String, QName> narrower = concept.getNarrowers();
		Iterator<String> itn = narrower.keySet().iterator();
		HashMap<String, String> narrowers = new HashMap<String, String>();
		while (itn.hasNext()) {
			String key = itn.next();
			QName qq = narrower.get(key);
			String URI = qq.getNamespaceURI();
			String lp = qq.getLocalPart();
			String value = URI + " " + lp;
			narrowers.put(key, value);
		}

		TreeMap<String, QName> related = concept.getRelated();
		Iterator<String> itr = related.keySet().iterator();
		HashMap<String, String> relateds = new HashMap<String, String>();
		while (itr.hasNext()) {
			String key = itr.next();
			QName qq = related.get(key);
			String URI = qq.getNamespaceURI();
			String lp = qq.getLocalPart();
			String value = URI + " " + lp;
			relateds.put(key, value);
		}

		ConceptProxy cp = new ConceptProxy(origin, preLabel, uri);
		if (broaders.isEmpty())
			broaders = null;
		if (narrowers.isEmpty())
			narrowers = null;
		if (relateds.isEmpty())
			relateds = null;
		if (altLabel.isEmpty())
			altLabel = null;
		if (scopeNotes.isEmpty())
			scopeNotes = null;
		cp.put(altLabel, broaders, narrowers, relateds, scopeNotes, skosCode);
		return cp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getTags(java.lang.String,
	 * java.util.List, int, java.lang.String)
	 */
	@Override
	public List<ConceptProxy> getTags(String input,
			List<String> openedVocabularies, int numTerms, String algorithm) {
		logger.debug("getTags for " + input);

		SKOSTagger tagger = this.getSkosServer().getSKOSTagger(algorithm);
		List<SKOSConcept> candidates = tagger.getTags(input,
				openedVocabularies, this.getSKOSSearcher(), numTerms,
				DEFAULT_MIN_OCCUR);
		List<ConceptProxy> result = new ArrayList<ConceptProxy>();
		for (SKOSConcept concept : candidates) {
			String preLabel = concept.getPrefLabel();
			preLabel = preLabel.replaceAll("\\(", "&#40;");
			preLabel = preLabel.replaceAll("\\)", "&#41;");
			QName qname = concept.getQName();
			String namespace = qname.getNamespaceURI();
			String lp = qname.getLocalPart();
			String uri = namespace + " " + lp;
			double score = concept.getScore();
			String origin = getSkosServer().getOrigin(qname);
			ConceptProxy cp = new ConceptProxy(origin, preLabel, uri, score);
			result.add(cp);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getTags(java.lang.String,
	 * java.util.List, int, int, java.lang.String)
	 */
	@Override
	public List<ConceptProxy> getTags(String input,
			List<String> openedVocabularies, int numTerms, int minPhraseOccur,
			String algorithm) {
		SKOSTagger tagger = this.getSkosServer().getSKOSTagger(algorithm);
		List<SKOSConcept> candidates = tagger.getTags(input,
				openedVocabularies, this.getSKOSSearcher(), numTerms,
				minPhraseOccur);
		List<ConceptProxy> result = new ArrayList<ConceptProxy>();
		for (SKOSConcept concept : candidates) {
			String preLabel = concept.getPrefLabel();
			preLabel = preLabel.replaceAll("\\(", "&#40;");
			preLabel = preLabel.replaceAll("\\)", "&#41;");
			QName qname = concept.getQName();
			String namespace = qname.getNamespaceURI();
			String lp = qname.getLocalPart();
			String uri = namespace + " " + lp;
			double score = concept.getScore();
			String origin = getSkosServer().getOrigin(qname);
			ConceptProxy cp = new ConceptProxy(origin, preLabel, uri, score);

			Map<String, String> broaderMap = getStringMap(concept.getBroaders());
			cp.setBroader(broaderMap);

			Map<String, String> narrowerMap = getStringMap(concept
					.getNarrowers());
			cp.setNarrower(narrowerMap);
			cp.setAltLabel(concept.getAltLabels());
			result.add(cp);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getStringMap(java.util
	 * .Map)
	 */
	@Override
	public Map<String, String> getStringMap(Map<String, QName> qnameMap) {
		Map<String, String> stringMap = new HashMap<String, String>();
		for (String key : qnameMap.keySet()) {
			QName value = qnameMap.get(key);
			stringMap.put(key, value.getNamespaceURI() + value.getLocalPart());
		}
		return stringMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getTags(java.net.URL,
	 * java.util.List, int, int, boolean, int, java.lang.String)
	 */
	@Override
	public List<ConceptProxy> getTags(URL url, List<String> openedVocabularies,
			int maxHops, int numTerms, boolean diff, int minOccur,
			String algorithm) {
		SKOSTagger tagger = this.getSkosServer().getSKOSTagger(algorithm);
		List<SKOSConcept> candidates = tagger.getTags(url, openedVocabularies,
				this.getSKOSSearcher(), maxHops, numTerms, diff, minOccur);
		List<ConceptProxy> result = new ArrayList<ConceptProxy>();
		for (SKOSConcept concept : candidates) {
			String preLabel = concept.getPrefLabel();
			QName qname = concept.getQName();
			String namespace = qname.getNamespaceURI();
			String lp = qname.getLocalPart();
			String uri = namespace + " " + lp;
			double score = concept.getScore();
			String origin = getSkosServer().getOrigin(qname);
			ConceptProxy cp = new ConceptProxy(origin, preLabel, uri, score);
			result.add(cp);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getTagsAsTree(java.lang
	 * .String, java.util.List, int, int, java.lang.String)
	 */
	@Override
	public List<ConceptNode> getTagsAsTree(String text,
			List<String> openedVocabularies, int maxHops, int numTerms,
			String algorithm) {
		SKOSTagger tagger = this.getSkosServer().getSKOSTagger(algorithm);
		List<ConceptNode> tree = tagger.getTagsAsTree(text, openedVocabularies,
				this.getSKOSSearcher(), maxHops, numTerms);
		return tree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#getFirstConcept(java.
	 * lang.String)
	 */
	@Override
	public ConceptProxy getFirstConcept(String vocabulary) {
		TreeMap<String, SKOSScheme> vocabularyMap = this.getSkosServer()
				.getSKOSSchemas();
		SKOSScheme voc = vocabularyMap.get(vocabulary.toLowerCase());
		List<SKOSConcept> top = voc.getSubTopConceptIndex("a");
		QName value = top.get(0).getQName();
		ConceptProxy cp = this.getConceptByURI(value.getNamespaceURI(),
				value.getLocalPart());
		return cp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.service.VocabularyService#suggestTermsFor(java.
	 * lang.String, java.lang.String, int)
	 */
	@Override
	public List<AutocompleteTerm> suggestTermsFor(String vocabulary,
			String str, int numTerms) throws Exception {
		TreeMap<String, SKOSScheme> vocabularyMap = this.getSkosServer()
				.getSKOSSchemas();
		SKOSScheme voc = vocabularyMap.get(vocabulary.toLowerCase());
		return voc.suggestTermsFor(str, numTerms);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.service.VocabularyService#close()
	 */
	@Override
	public void close() {
		this.hiveContainer.shutdown();
	}

}