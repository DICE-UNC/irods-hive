package org.irods.jargon.hive.rest.vocabservice;

import javax.inject.Named;
import javax.ws.rs.Path;

import org.irods.jargon.hive.service.VocabularyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for searching for vocabulary terms
 * 
 * @author Mike Conway - DICE (www.irods.org)
 *
 */
@Named
@Path("/search")
public class RestVocabularySearchService {

	/**
	 * Injected dependency on the HIVE vocabulary service
	 */
	private VocabularyService vocabularyService;

	public static final Logger log = LoggerFactory
			.getLogger(RestVocabularySearchService.class);

	public RestVocabularySearchService() {
	}

	/**
	 * @return the vocabularyService
	 */
	public VocabularyService getVocabularyService() {
		return vocabularyService;
	}

	/**
	 * @param vocabularyService
	 *            the vocabularyService to set
	 */
	public void setVocabularyService(final VocabularyService vocabularyService) {
		this.vocabularyService = vocabularyService;
	}

}
