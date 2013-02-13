/**
 * 
 */
package org.irods.jargon.hive.exception;

/**
 * Generic exception with HIVE Jargon processing
 * @author Mike Conway - DICE (www.irods.org)
 *
 */
public class JargonHiveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7832176388771321426L;

	/**
	 * 
	 */
	public JargonHiveException() {
	}

	/**
	 * @param message
	 */
	public JargonHiveException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public JargonHiveException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public JargonHiveException(String message, Throwable cause) {
		super(message, cause);
	}

	
}
