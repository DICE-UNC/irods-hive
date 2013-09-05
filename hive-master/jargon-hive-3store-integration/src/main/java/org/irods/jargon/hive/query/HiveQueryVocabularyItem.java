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
 * @author Le
 *
 */
public class HiveQueryVocabularyItem implements Serializable {
	
	private String vocabularyName = "";
	private String vocaublaryTermURI = "";
	private String preferredLabel = "";

	public enum ConnectorTypeEnum {AND,OR}
	
	private ConnectorTypeEnum connectorType = ConnectorTypeEnum.AND;
	
	private List<VocabularyItemSearchType> searchTypes = new ArrayList<VocabularyItemSearchType>();
	

}
