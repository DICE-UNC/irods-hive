/**
 * 
 */
package edu.unc.ils.mrc.hive.exception;

/**
 * General HIVE checked exception
 * 
 * @author Mike Conway - DICE
 * 
 */
public class HiveException extends Exception {

	private static final long serialVersionUID = 3418065388129022834L;

	/**
	 * 
	 */
	public HiveException() {
	}

	/**
	 * @param arg0
	 */
	public HiveException(final String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public HiveException(final Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public HiveException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

}
