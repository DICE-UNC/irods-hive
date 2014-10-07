package org.irods.jargon.hive.external.indexer.modelservice;

import org.irods.jargon.hive.external.sparql.HiveQueryException;

import com.hp.hpl.jena.query.ResultSet;

/**
 * Interface provides handy query wrappers to support the operation of the model updater package services
 * @author Mike Conway - DICE
 *
 */
public interface IrodsJenaModelQuery {

	/**
	 * Do a query given a vocabulary URI and the appropriate sparql template
	 * expecting to substitute the term value
	 * 
	 * @param vocabularyUri
	 * @param sparqlTemplate
	 * @return
	 * @throws HiveQueryException
	 */
	public abstract ResultSet queryAllOnVocabularyTerm(String vocabularyUri)
			throws HiveQueryException;

	/**
	 * Given a set of complete SPARQL, query and return a <code>ResultSet</code> formatted into a JSON <code>String</code>
	 * @param sparqlString
	 * @return
	 * @throws HiveQueryException
	 */
	public abstract String queryAndReturnJSONAsString(String sparqlString)
			throws HiveQueryException;

	/**
	 * Given a complete SPARQL query, return a <code>ResultSet</code> that is the answer
	 * @param sparqlString
	 * @return {@link ResultSet}
	 * @throws HiveQueryException
	 */
	public abstract ResultSet queryAndReturnResultSet(String sparqlString)
			throws HiveQueryException;

}