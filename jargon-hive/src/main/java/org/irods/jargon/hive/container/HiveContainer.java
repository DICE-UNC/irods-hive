package org.irods.jargon.hive.container;

import org.irods.jargon.hive.exception.JargonHiveException;

import edu.unc.ils.mrc.hive.api.SKOSServer;

/**
 * Interface for the core Jargon/HIVE service. This is the container for the
 * HIVE services and provides access to basic services needed by Jargon.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public interface HiveContainer {

	/**
	 * @return the hiveConfiguration
	 */
	HiveConfiguration getHiveConfiguration();

	/**
	 * @param hiveConfiguration
	 *            the hiveConfiguration to set
	 */
	void setHiveConfiguration(HiveConfiguration hiveConfiguration);

	/**
	 * Method called upon startup to initialize the <code>SKOSServer</code>
	 * components
	 * 
	 * @throws JargonHiveException
	 */
	void init() throws JargonHiveException;

	/**
	 * Get the <code>SKOSServer</code> that contains the HIVE vocabularies
	 * 
	 * @return
	 */
	SKOSServer getSkosServer();

	public abstract void shutdown();

}