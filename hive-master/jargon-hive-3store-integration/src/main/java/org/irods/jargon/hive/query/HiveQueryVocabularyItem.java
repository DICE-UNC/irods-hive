/**
 * 
 */
package org.irods.jargon.hive.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an item in the query
 * 
 * @author Le Zhang
 *
 */
public class HiveQueryVocabularyItem implements Serializable {
	
	private String vocabularyName = "";
	private String vocaublaryTermURI = "";
	private String preferredLabel = "";
	
	
    public enum ConnectorTypeEnum {AND,OR}
	
	private ConnectorTypeEnum connectorType = ConnectorTypeEnum.AND;
	
	private List<VocabularyItemSearchType> searchTypes = new ArrayList<VocabularyItemSearchType>();
	
	public String getVocabularyName() {
		return vocabularyName;
	}

	public void setVocabularyName(String vocabularyName) {
		this.vocabularyName = vocabularyName;
	}

	public String getVocaublaryTermURI() {
		return vocaublaryTermURI;
	}

	public void setVocaublaryTermURI(String vocaublaryTermURI) {
		this.vocaublaryTermURI = vocaublaryTermURI;
	}

	public String getPreferredLabel() {
		return preferredLabel;
	}

	public void setPreferredLabel(String preferredLabel) {
		this.preferredLabel = preferredLabel;
	}

	public ConnectorTypeEnum getConnectorType() {
		return connectorType;
	}

	public void setConnectorType(ConnectorTypeEnum connectorType) {
		this.connectorType = connectorType;
	}

	public List<VocabularyItemSearchType> getSearchTypes() {
		return searchTypes;
	}

	public void setSearchTypes(List<VocabularyItemSearchType> searchTypes) {
		this.searchTypes = searchTypes;
	}



	

}
