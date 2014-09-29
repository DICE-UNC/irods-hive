/**
 * 
 */
package org.irods.jargon.indexing.hive.metadata;

import org.irods.jargon.indexing.wrapper.IndexerException;

/**
 * Indicates an issue configuring or starting up the hive indexer (database or
 * triple store issues, etc)
 * 
 * @author Mike Conway - DICE
 *
 */
public class HiveIndexerConfigException extends IndexerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7210136120406295247L;

	/**
	 * 
	 */
	public HiveIndexerConfigException() {
	}

	/**
	 * @param message
	 */
	public HiveIndexerConfigException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public HiveIndexerConfigException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public HiveIndexerConfigException(String message, Throwable cause) {
		super(message, cause);
	}

}
