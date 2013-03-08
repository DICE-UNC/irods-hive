package org.unc.hive.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Refactoring of <code>ConceptProxy</code> in hive-web.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class ConceptProxy implements IsSerializable {
	private boolean topLevel = false;
	private String preLabel;
	private String URI;
	private String origin;
	private String SKOSCode;
	private Map<String, String> narrower = new HashMap<String, String>();
	private Map<String, String> broader = new HashMap<String, String>();
	private Map<String, String> related = new HashMap<String, String>();
	private List<String> altLabel = new ArrayList<String>();
	private List<String> scopeNotes = new ArrayList<String>();
	private boolean isleaf = false;
	private double score;

	public ConceptProxy() {

	}

	public ConceptProxy(String prelabel, String uri) {
		this.preLabel = prelabel;
		this.URI = uri;
	}

	public ConceptProxy(String origin, String prelabel, String uri,
			boolean isleaf) {
		this.origin = origin;
		this.preLabel = prelabel;
		this.URI = uri;
		this.isleaf = isleaf;
	}

	public ConceptProxy(String origin, String prelabel, String uri) {
		this.origin = origin;
		this.preLabel = prelabel;
		this.URI = uri;
	}

	public ConceptProxy(String origin, String prelabel, String uri, double score) {
		this.origin = origin;
		this.preLabel = prelabel;
		this.URI = uri;
		this.score = score;
	}

	public ConceptProxy(String origin, String prelabel, String uri,
			String skosCode) {
		this.origin = origin;
		this.preLabel = prelabel;
		this.URI = uri;
		this.SKOSCode = skosCode;
	}

	public double getScore() {
		return this.score;
	}

	public String getSkosCode() {
		return this.SKOSCode;
	}

	public boolean getIsLeaf() {
		return this.isleaf;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getOrigin() {
		return this.origin;
	}

	public void setPreLabel(String prelabel) {
		preLabel = prelabel;
	}

	public String getPreLabel() {
		return this.preLabel;
	}

	public void setURI(String uri) {
		URI = uri;
	}

	public String getURI() {
		return URI;
	}

	public void setNarrower(Map<String, String> map) {
		this.narrower = new HashMap<String, String>(map);
	}

	public Map<String, String> getNarrower() {
		return this.narrower;
	}

	public void setBroader(Map<String, String> map) {
		this.broader = new HashMap<String, String>(map);
	}

	public Map<String, String> getBroader() {
		return this.broader;
	}

	public void setRelated(HashMap<String, String> map) {
		this.related = new HashMap<String, String>(map);
	}

	public Map<String, String> getRelated() {
		return this.related;
	}

	public void setAltLabel(List<String> altlabel) {
		this.altLabel = altlabel;
	}

	public List<String> getAltLabel() {
		return this.altLabel;
	}

	public void setScopeNotes(List<String> notes) {
		this.scopeNotes = notes;
	}

	public List<String> getScopeNotes() {
		return this.scopeNotes;
	}

	/**
	 * Is this concept the 'top level' set of terms for a vocabulary? In this
	 * case there is no URI
	 * 
	 * @return
	 */
	public boolean isTopLevel() {
		return topLevel;
	}

	/**
	 * Indicate that this concept proxy is the top level of a vocabulary 'tree'
	 * 
	 * @param topLevel
	 */
	public void setTopLevel(final boolean topLevel) {
		this.topLevel = topLevel;
	}

	public void put(List<String> altlabel, Map<String, String> broader,
			Map<String, String> narrower, Map<String, String> related,
			List<String> scopeNote, String skosCode) {
		this.altLabel = altlabel;
		this.broader = broader;
		this.narrower = narrower;
		this.related = related;
		this.scopeNotes = scopeNote;
		this.SKOSCode = skosCode;
	}
}