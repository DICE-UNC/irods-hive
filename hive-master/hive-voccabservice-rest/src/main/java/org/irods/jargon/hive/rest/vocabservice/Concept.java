package org.irods.jargon.hive.rest.vocabservice;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * value holder that contains the label, uri, vacb name, list from conceptproxy
 * 
 * @author Mao
 *
 */
@XmlRootElement(name = "hiveConcept")
public class Concept {

	private String vocabName = "";
	private String uri = "";
	private String label = "";
	private List<String> altLabel = new ArrayList<String>();
	private List<ConceptListEntry> narrower = new ArrayList<ConceptListEntry>();
	private List<ConceptListEntry> broader = new ArrayList<ConceptListEntry>();
	private List<ConceptListEntry> related = new ArrayList<ConceptListEntry>();
	
	/**
	 * no-values constructor needed for XML binding
	 */
	public Concept() {
		
	}

	@XmlElement
	public String getVocabName() {
		return vocabName;
	}

	public void setVocabName(final String vocabName) {
		this.vocabName = vocabName;
	}

	@XmlAttribute
	public String getUri() {
		return uri;
	}

	public void setUri(final String uri) {
		this.uri = uri;
	}

	@XmlElement
	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	@XmlElement(name="altLabel")
	public List<String> getAltLabel() {
		return altLabel;
	}

	public void setAltLabel(final List<String> altLabel) {
		this.altLabel = altLabel;
	}

	@XmlElement(name="narrower")
	public List<ConceptListEntry> getNarrower() {
		return narrower;
	}

	public void setNarrower(final List<ConceptListEntry> narrower) {
		this.narrower = narrower;
	}

	@XmlElement(name="broader")
	public List<ConceptListEntry> getBroader() {
		return broader;
	}

	public void setBroader(final List<ConceptListEntry> broader) {
		this.broader = broader;
	}

	@XmlElement(name="related")
	public List<ConceptListEntry> getRelated() {
		return related;
	}

	public void setRelated(final List<ConceptListEntry> related) {
		this.related = related;
	}

}
