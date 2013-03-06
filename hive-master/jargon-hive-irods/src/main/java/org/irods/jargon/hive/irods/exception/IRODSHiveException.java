package org.irods.jargon.hive.irods.exception;

import org.irods.jargon.core.exception.JargonException;

/**
 * An exception occurred in storing or retrieving HIVE information in iRODS.
 * This is a general catch-all exception.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class IRODSHiveException extends JargonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7319553692038887010L;

	/**
	 * @param message
	 */
	public IRODSHiveException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public IRODSHiveException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public IRODSHiveException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 * @param underlyingIRODSExceptionCode
	 */
	public IRODSHiveException(Throwable cause, int underlyingIRODSExceptionCode) {
		super(cause, underlyingIRODSExceptionCode);
	}

	/**
	 * @param message
	 * @param underlyingIRODSExceptionCode
	 */
	public IRODSHiveException(String message, int underlyingIRODSExceptionCode) {
		super(message, underlyingIRODSExceptionCode);
	}

	/**
	 * @param message
	 * @param cause
	 * @param underlyingIRODSExceptionCode
	 */
	public IRODSHiveException(String message, Throwable cause,
			int underlyingIRODSExceptionCode) {
		super(message, cause, underlyingIRODSExceptionCode);
	}

}
