/**
 * 
 */
package org.irods.jargon.hive.container;

import org.irods.jargon.hive.exception.JargonHiveException;

import edu.unc.ils.mrc.hive.api.SKOSServer;

/**
 * Main container for integrating iRODS and HIVE, contains the core HIVE
 * services and allows configuration and access to vocabularies and other
 * information.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 */
public class HiveContainerImpl implements HiveContainer {

	/**
	 * Flag that can check if startup was done
	 */
	private volatile boolean started = false;

	/**
	 * HIVE core service that picks up configured vocabularies and indexers.
	 * This is started up by this container class using the given config.
	 */
	private final SKOSServer skosServer;

	/**
	 * Configuration properties used to initialize HIVE services
	 */
	private HiveConfiguration hiveConfiguration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.HiveContainer#getHiveConfiguration()
	 */
	@Override
	public HiveConfiguration getHiveConfiguration() {
		return hiveConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.hive.HiveContainer#setHiveConfiguration(org.irods.jargon
	 * .hive.HiveConfiguration)
	 */
	@Override
	public void setHiveConfiguration(HiveConfiguration hiveConfiguration) {
		this.hiveConfiguration = hiveConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.HiveContainer#init()
	 */
	@Override
	public void init() throws JargonHiveException {

		if (hiveConfiguration == null) {
			throw new JargonHiveException("hiveConfiguration not provided");
		}

		if (started) {
			throw new IllegalArgumentException("hive already staretd");
		}

		startupSkosServer();
		started = true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.hive.container.HiveContainer#shutdown()
	 */
	@Override
	public void shutdown() {
		if (!started) {
			return;
		}

		skosServer.close();

	}

	private void startupSkosServer() throws JargonHiveException {

	}

	@Override
	public SKOSServer getSkosServer() {
		return skosServer;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("HiveContainerImpl");
		sb.append("\n\t hiveConfiguration:");
		sb.append(hiveConfiguration);
		return sb.toString();
	}

}
