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
import javax.ws.rs.QueryParam;

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

	@GET
	@Path("{vocabulary}/top")
	@Produces({ "application/xml", "application/json" })
	@Mapped(namespaceMap = { @XmlNsMap(namespace = "http://irods.org/hive", jsonName = "hive-vocabulary-service-rest") })
	public List<ConceptProxy> getConcept(
			
			@PathParam("vocabulary") final String vocabulary)
			throws JargonHiveException {
		log.info("getConcepts()");

		if (vocabulary == null || vocabulary.isEmpty()) {
			throw new IllegalArgumentException("null vocabulary");
		}
		log.info("vocabulary:{}", vocabulary);
		return vocabularyService.getSubTopConcept(vocabulary, "", true);
	}
	
	@GET
	@Path("{vocabulary}/top")
	@Produces({ "application/xml", "application/json" })
	@Mapped(namespaceMap = { @XmlNsMap(namespace = "http://irods.org/hive", jsonName = "hive-vocabulary-service-rest") })
	public List<ConceptProxy> getConceptQueries(
			@QueryParam("letter") final String letter,
			@PathParam("vocabulary") final String vocabulary)
			throws JargonHiveException {
		log.info("getConcepts()");

		if (vocabulary == null || vocabulary.isEmpty()) {
			throw new IllegalArgumentException("null vocabulary");
		}
		log.info("vocabulary:{}", vocabulary);
		return vocabularyService.getSubTopConcept(vocabulary, letter, true);
	}
}
