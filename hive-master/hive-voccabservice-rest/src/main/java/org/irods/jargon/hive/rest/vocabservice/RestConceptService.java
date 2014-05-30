/**
 * 
 */
package org.irods.jargon.hive.rest.vocabservice;

import java.util.List;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.irods.jargon.hive.exception.JargonHiveException;
import org.irods.jargon.hive.service.VocabularyService;
import org.jboss.resteasy.annotations.providers.jaxb.json.Mapped;
import org.jboss.resteasy.annotations.providers.jaxb.json.XmlNsMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.unc.hive.client.ConceptProxy;

/**
 * REST interface for interacting with concepts
 * 
 * @author Mike Conway - DICE
 * 
 */
@Named
@Path("/concept")
public class RestConceptService {

	/**
	 * Injected dependency on the HIVE vocabulary service
	 */
	private VocabularyService vocabularyService;

	public static final Logger log = LoggerFactory
			.getLogger(RestConceptService.class);

	public VocabularyService getVocabularyService() {
		return vocabularyService;
	}

	@Autowired
	public void setVocabularyService(VocabularyService vocabularyService) {
		this.vocabularyService = vocabularyService;
	}

	/**
	 * respond to a get request and return a list of concepts
	 * 
	 * @return <code>List<String></code> with vocabulary names in the HIVE
	 * @throws JargonHiveException
	 *             TODO: just returning top concepts now, add start letter
	 *             first, then worry about navigating based on term? or add
	 *             broader/narrower/etc?
	 */
	@GET
	@Path("{vocabulary}/top")
	@Produces({ "application/xml", "application/json" })
	@Mapped(namespaceMap = { @XmlNsMap(namespace = "http://irods.org/hive", jsonName = "hive-vocabulary-service-rest") })
	public List<ConceptProxy> getConcepts(
			@PathParam("vocabulary") final String vocabulary)
			throws JargonHiveException {
		log.info("getConcepts()");

		if (vocabulary == null || vocabulary.isEmpty()) {
			throw new IllegalArgumentException("null vocabulary");
		}
		log.info("vocabulary:{}", vocabulary);
		return vocabularyService.getSubTopConcept(vocabulary, "", true);
	}
}
