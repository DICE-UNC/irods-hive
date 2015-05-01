/**
 * 
 */
package org.irods.jargon.hive.rest.vocabservice;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * value holder that contains the label and uri from conceptproxy
 * @author Mao
 *
 */
@XmlRootElement(name = "dataObject")

public class ConceptListEntry {
	
	private String label = "";
	private String uri = "";
	
	/**
	 * no-values constructor needed for xml binding
	 */
	public ConceptListEntry() {
		
	}
	
	public ConceptListEntry(String uri, String label) {
		this.label = label;
		this.uri = uri;
	}
	@XmlElement

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@XmlAttribute
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	

}
