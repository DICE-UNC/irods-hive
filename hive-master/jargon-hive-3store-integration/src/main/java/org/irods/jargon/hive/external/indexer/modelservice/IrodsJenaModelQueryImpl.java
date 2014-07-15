/**
 * 
 */
package org.irods.jargon.hive.external.indexer.modelservice;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.irods.jargon.hive.external.query.JargonHiveQueryServiceImpl;
import org.irods.jargon.hive.external.sparql.HiveQueryException;
import org.irods.jargon.hive.external.utils.template.HiveTemplateException;
import org.irods.jargon.hive.external.utils.template.SPARQLTemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.shared.Lock;

/**
 * Handy query of model service for vocabulary terms and other operations
 * 
 * @author Mike Conway - DICE
 */
public class IrodsJenaModelQueryImpl implements IrodsJenaModelQuery {

	private final OntModel jenaModel;

	public static final Logger log = LoggerFactory
			.getLogger(IrodsJenaModelQueryImpl.class);

	/**
	 * Constructor takes an open Jena model (this class will not do any close).
	 * 
	 * @param jenaModel
	 *            {@link OntModel} that is opened, and will be used for any
	 *            queries
	 */
	public IrodsJenaModelQueryImpl(final OntModel jenaModel) {
		if (jenaModel == null) {
			throw new IllegalArgumentException("null jenaModel");
		}

		this.jenaModel = jenaModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.external.indexer.modelservice.IrodsJenaModelQuery
	 * #queryAllOnVocabularyTerm(java.lang.String)
	 */
	@Override
	public ResultSet queryAllOnVocabularyTerm(final String vocabularyUri)
			throws HiveQueryException {
		Map<String, String> params = new HashMap<String, String>();

		params.put(JargonHiveQueryServiceImpl.TERM, vocabularyUri);
		try {
			log.info("getting sparql template for query all");

			String query = SPARQLTemplateUtils
					.getSPARQLTemplateAndSubstituteValues(
							"/sparql-template/queryAllForTerm.txt", params);
			log.info("built query:{}", query);
			getJenaModel().enterCriticalSection(Lock.READ);
			return queryAndReturnResultSet(query);
		} catch (HiveTemplateException e) {
			throw new HiveQueryException(
					"error making query from sparql template", e);
		} finally {
			getJenaModel().leaveCriticalSection();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.external.indexer.modelservice.IrodsJenaModelQuery
	 * #queryAndReturnJSONAsString(java.lang.String)
	 */
	@Override
	public String queryAndReturnJSONAsString(final String sparqlString)
			throws HiveQueryException {
		log.info("queryAndReturnJSONAsString()");
		if (sparqlString == null || sparqlString.isEmpty()) {
			throw new IllegalArgumentException("null or empty sparqlString");
		}
		log.info("sparqlString:{}", sparqlString);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			getJenaModel().enterCriticalSection(Lock.READ);

			ResultSet resultSet = queryAndReturnResultSet(sparqlString);
			log.info("outputting as JSON");
			ResultSetFormatter.outputAsJSON(bos, resultSet);
			log.info("json in stream, now output to string");
			return bos.toString("UTF-8");
		} catch (Exception e) {
			log.error("exception processing query", e);
			throw new HiveQueryException(e);
		} finally {
			getJenaModel().leaveCriticalSection();

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.external.indexer.modelservice.IrodsJenaModelQuery
	 * #queryAndReturnResultSet(java.lang.String)
	 */

	@Override
	public ResultSet queryAndReturnResultSet(final String sparqlString)
			throws HiveQueryException {
		log.info("queryAndReturnJSONAsString()");
		if (sparqlString == null || sparqlString.isEmpty()) {
			throw new IllegalArgumentException("null or empty sparqlString");
		}
		log.info("sparqlString:{}", sparqlString);
		QueryExecution qexec = null;
		try {
			Query query = QueryFactory.create(sparqlString);
			qexec = QueryExecutionFactory.create(query, jenaModel);
			log.info("running query...");
			getJenaModel().enterCriticalSection(Lock.READ);

			return qexec.execSelect();
		} catch (Exception e) {
			log.error("exception processing query", e);
			throw new HiveQueryException(e);
		} finally {
			getJenaModel().leaveCriticalSection();
		}
	}

	public OntModel getJenaModel() {
		return jenaModel;
	}

}
