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
	/**
	 * allows specification that this concept is 'selected' in the current
	 * context. e.g. this concept has been applied to a resource, such as an
	 * iRODS file or collection. This value is meant for custom client use, and
	 * is not set by HIVE itself.
	 */
	private boolean selected = false;

	public ConceptProxy() {

	}

	public ConceptProxy(final String prelabel, final String uri) {
		this.preLabel = prelabel;
		this.URI = uri;
	}

	public ConceptProxy(final String origin, final String prelabel,
			final String uri, final boolean isleaf) {
		this.origin = origin;
		this.preLabel = prelabel;
		this.URI = uri;
		this.isleaf = isleaf;
	}

	public ConceptProxy(final String origin, final String prelabel,
			final String uri) {
		this.origin = origin;
		this.preLabel = prelabel;
		this.URI = uri;
	}

	public ConceptProxy(final String origin, final String prelabel,
			final String uri, final double score) {
		this.origin = origin;
		this.preLabel = prelabel;
		this.URI = uri;
		this.score = score;
	}

	public ConceptProxy(final String origin, final String prelabel,
			final String uri, final String skosCode) {
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

	public void setOrigin(final String origin) {
		this.origin = origin;
	}

	public String getOrigin() {
		return this.origin;
	}

	public void setPreLabel(final String prelabel) {
		preLabel = prelabel;
	}

	public String getPreLabel() {
		return this.preLabel;
	}

	public void setURI(final String uri) {
		URI = uri;
	}

	public String getURI() {
		return URI;
	}

	public void setNarrower(final Map<String, String> map) {
		this.narrower = new HashMap<String, String>(map);
	}

	public Map<String, String> getNarrower() {
		return this.narrower;
	}

	public void setBroader(final Map<String, String> map) {
		this.broader = new HashMap<String, String>(map);
	}

	public Map<String, String> getBroader() {
		return this.broader;
	}

	public void setRelated(final HashMap<String, String> map) {
		this.related = new HashMap<String, String>(map);
	}

	public Map<String, String> getRelated() {
		return this.related;
	}

	public void setAltLabel(final List<String> altlabel) {
		this.altLabel = altlabel;
	}

	public List<String> getAltLabel() {
		return this.altLabel;
	}

	public void setScopeNotes(final List<String> notes) {
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

	public void put(final List<String> altlabel,
			final Map<String, String> broader,
			final Map<String, String> narrower,
			final Map<String, String> related, final List<String> scopeNote,
			final String skosCode) {
		this.altLabel = altlabel;
		this.broader = broader;
		this.narrower = narrower;
		this.related = related;
		this.scopeNotes = scopeNote;
		this.SKOSCode = skosCode;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(final boolean selected) {
		this.selected = selected;
	}
}