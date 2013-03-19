package org.irods.jargon.hive.external.indexer;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class that specifies handling of a Jena model that will be
 * build based on iRODS AVU data representing HIVE terms applied to iRODS data
 * objects and collections.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class JenaHiveVisitorConfiguration {

	/**
	 * Determines jena model type
	 * 
	 * @author Mike Conway - DICE (www.irods.org)
	 * 
	 */
	public enum JenaModelType {
		MEMORY
	}

	/**
	 * Type of Jena model to build
	 */
	private JenaModelType jenaModelType = JenaModelType.MEMORY;

	/**
	 * List of vocabulary file paths to load, this is loaded into the triple
	 * store
	 */
	private List<String> vocabularyRDFFileNames = new ArrayList<String>();

	/**
	 * File path to iRODS schema file, this is loaded into the triple store
	 */
	private String irodsRDFFileName = "";

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("JenaHiveVisitorConfiguration");
		sb.append("\n\t jenaModelType:");
		sb.append(jenaModelType);
		sb.append("\n\t irodsRDFFileName:");
		sb.append(irodsRDFFileName);

		if (vocabularyRDFFileNames != null) {
			sb.append("\n\t vocabularyFiles:");

			for (String fileName : vocabularyRDFFileNames) {
				sb.append("\n\t\t");
				sb.append(fileName);
			}

		}
		return sb.toString();
	}

	public JenaHiveVisitorConfiguration() {
	}

	public JenaModelType getJenaModelType() {
		return jenaModelType;
	}

	public void setJenaModelType(final JenaModelType jenaModelType) {
		this.jenaModelType = jenaModelType;
	}

	public List<String> getVocabularyRDFFileNames() {
		return vocabularyRDFFileNames;
	}

	public void setVocabularyRDFFileNames(
			final List<String> vocabularyRDFFileNames) {
		this.vocabularyRDFFileNames = vocabularyRDFFileNames;
	}

	public String getIrodsRDFFileName() {
		return irodsRDFFileName;
	}

	public void setIrodsRDFFileName(final String irodsRDFFileName) {
		this.irodsRDFFileName = irodsRDFFileName;
	}

}
