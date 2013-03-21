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
public class JenaHiveConfiguration {

	public static String SOURCE = "http://www.irods.org/ontologies/2013/2/iRODS.owl";
	public static String NS = SOURCE + "#";

	/**
	 * Determines jena model type
	 * 
	 * @author Mike Conway - DICE (www.irods.org)
	 * 
	 */
	public enum JenaModelType {
		MEMORY, MEMORY_ONT
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
	 * File path to iRODS schema file, this is loaded into the triple store.
	 * NOTE: the presence of the schema file will cause additional RDF
	 * statements to be generated based on the available metadata
	 */
	private String irodsRDFFileName = "";

	/**
	 * Configuration tells indexer service to close the Jena model automatically
	 */
	private boolean autoCloseJenaModel = false;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("JenaHiveVisitorConfiguration");
		sb.append("\n\t jenaModelType:");
		sb.append(jenaModelType);
		sb.append("\n\t autoCloseJenaModel:");
		sb.append(autoCloseJenaModel);
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

	public JenaHiveConfiguration() {
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

	/**
	 * @return the autoCloseJenaModel
	 */
	public boolean isAutoCloseJenaModel() {
		return autoCloseJenaModel;
	}

	/**
	 * @param autoCloseJenaModel
	 *            the autoCloseJenaModel to set
	 */
	public void setAutoCloseJenaModel(final boolean autoCloseJenaModel) {
		this.autoCloseJenaModel = autoCloseJenaModel;
	}

	/**
	 * See if an iRODS ontology is configured, if so, statements generated will
	 * also utilize the given ontology to add additional catalog information
	 * 
	 * @return <code>boolean</code> of <code>true</code> if an ontology file is
	 *         present, and ontologies should be generated
	 */
	public boolean isIrodsOntologyConfigured() {
		if (irodsRDFFileName == null || irodsRDFFileName.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

}
