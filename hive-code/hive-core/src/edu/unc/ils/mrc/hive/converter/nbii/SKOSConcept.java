package edu.unc.ils.mrc.hive.converter.nbii;

import java.util.ArrayList;

import java.util.List;

public class SKOSConcept implements Concept{
	
	private String uri;
	private String prefLabel;
	private String scopeNote;
	private List<String> altLabel;
	private List<String> hiddenLabel;
	private List<String> narrower;
	private List<String> narrowerURI;
	private List<String> broader;
	private List<String> broaderURI;
	private List<String> related;
	private List<String> realtedURI;
	
	public SKOSConcept(String uri) {
		this.uri = uri;
		this.narrower = new ArrayList<String>();
		this.narrowerURI = new ArrayList<String>();
		this.broader = new ArrayList<String>();
		this.broaderURI = new ArrayList<String>();
		this.altLabel = new ArrayList<String>();
		this.hiddenLabel = new ArrayList<String>();
		this.related = new ArrayList<String>();
		this.realtedURI = new ArrayList<String>();
		this.scopeNote  = "";
	}

	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	@Override
	public String getUri() {
		return this.uri;
	}

	@Override
	public String getPrefLabel() {
		return this.prefLabel;
	}

	@Override
	public void setPrefLabel(String prefLabel) {
		this.prefLabel = prefLabel;
	}

	@Override
	public String getScopeNote() {
		return this.scopeNote;
	}

	@Override
	public void setScopeNote(String scopeNote) {
		this.scopeNote = this.scopeNote.concat(" " + scopeNote);
	}

	@Override
	public List<String> getAltLabel() {
		return this.altLabel;
	}

	@Override
	public void setAltLabel(String altLabel) {
		this.altLabel.add(altLabel);
	}
	
	@Override
	public List<String> getHiddenLabel() {
		return this.hiddenLabel;
	}

	@Override
	public void setHiddenLabel(String hiddenLabel) {
		this.hiddenLabel.add(hiddenLabel);
	}

	@Override
	public List<String> getNarrower() {
		return this.narrower;
	}
	
	@Override
	public List<String> getNarrowerURI() {
		return this.narrowerURI;
	}

	@Override
	public void setNarrower(String narrower) {
		this.narrower.add(narrower);
	}
	
	@Override
	public void setNarrowerURI(String narrowerURI) {
		this.narrowerURI.add(narrowerURI);
	}

	@Override
	public List<String> getBroader() {
		return this.broader;
	}
	
	@Override
	public List<String> getBroaderURI() {
		return this.broaderURI;
	}

	@Override
	public void setBroader(String broader) {
		this.broader.add(broader);
	}
	
	@Override
	public void setBroaderURI(String broaderURI) {
		this.broaderURI.add(broaderURI);
	}
	
	@Override
	public List<String> getRelated() {
		return this.related;
	}
	
	@Override
	public List<String> getRelatedURI() {
		return this.realtedURI;
	}

	@Override
	public void setRelated(String related) {
		this.related.add(related);
	}
	
	@Override
	public void setRelatedURI(String relatedURI) {
		this.realtedURI.add(relatedURI);
	}

}
