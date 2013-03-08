package org.irods.jargon.hive.service;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.exception.VocabularyNotFoundException;
import org.unc.hive.client.ConceptProxy;

import edu.unc.ils.mrc.hive.api.ConceptNode;
import edu.unc.ils.mrc.hive.api.SKOSScheme;
import edu.unc.ils.mrc.hive.api.SKOSSearcher;
import edu.unc.ils.mrc.hive.ir.lucene.search.AutocompleteTerm;

public interface VocabularyService {

	public abstract SKOSSearcher getSKOSSearcher();

	public abstract long getNumberOfConcept(String vocabularyName)
			throws VocabularyNotFoundException;

	public abstract long getNumerOfRelations(String vocabularyName);

	public abstract Date getLastUpdateDate(String vocabularyName);

	public abstract List<List<String>> getAllVocabularies();

	/**
	 * Return a vocabulary (as a <code>SKOSScheme</code>) based on the name
	 * 
	 * @param vocabularyName
	 *            <code>String</code> with the vocabulary name
	 * @return {@link SKOSScheme} that represents the vocabulary with the given
	 *         name
	 * @throws VocabularyNotFoundException
	 */
	public abstract SKOSScheme getVocabularyByName(String vocabularyName)
			throws VocabularyNotFoundException;

	public abstract List<String> getAllVocabularyNames();

	public abstract HashMap<String, HashMap<String, String>> getVocabularyProperties();

	public abstract List<ConceptProxy> getSubTopConcept(String vocabulary,
			String letter, boolean brief) throws VocabularyNotFoundException;

	/**
	 * @gwt.typeArgs <client.ConceptProxy>
	 * 
	 * */

	public abstract List<ConceptProxy> getChildConcept(String nameSpaceURI,
			String localPart);

	/**
	 * @gwt.typeArgs <client.ConceptProxy>
	 * 
	 * */

	public abstract List<ConceptProxy> searchConcept(String keyword,
			List<String> openedVocabularies);

	public abstract ConceptProxy getConceptByURI(String namespaceURI,
			String localPart);

	/**
	 * @gwt.typeArgs <client.ConceptProxy>
	 * 
	 * */
	public abstract List<ConceptProxy> getTags(String input,
			List<String> openedVocabularies, int numTerms, String algorithm);

	public abstract List<ConceptProxy> getTags(String input,
			List<String> openedVocabularies, int numTerms, int minPhraseOccur,
			String algorithm);

	public abstract Map<String, String> getStringMap(Map<String, QName> qnameMap);

	public abstract List<ConceptProxy> getTags(URL url,
			List<String> openedVocabularies, int maxHops, int numTerms,
			boolean diff, int minOccur, String algorithm);

	public abstract List<ConceptNode> getTagsAsTree(String text,
			List<String> openedVocabularies, int maxHops, int numTerms,
			String algorithm);

	public abstract ConceptProxy getFirstConcept(String vocabulary);

	public abstract List<AutocompleteTerm> suggestTermsFor(String vocabulary,
			String str, int numTerms) throws Exception;

	public abstract void close();

	void setHiveContainer(HiveContainer hiveContainer);

	/**
	 * Create a <code>ConceptProxy</code> that represents the top level of a
	 * given vocabulary. This wraps the <code>getTopSubConcept</code> method to
	 * give a consistent view as a <code>ConceptProxy</code>.
	 * 
	 * @param vocabulary
	 *            <code>String</code> with a vocabulary name as stored in HIVE,
	 *            required
	 * @param letter
	 *            <code>String</code> with the index letter. If not specified
	 *            will default to 'A'
	 * @param brief
	 *            <code>boolean</code> that indicates that only basic data is
	 *            retrieved
	 * @return {@link ConceptProxy} marked as 'topLevel'
	 * @throws VocabularyNotFoundException
	 */
	ConceptProxy getConceptProxyForTopOfVocabulary(String vocabulary,
			String letter, boolean brief) throws VocabularyNotFoundException;

}