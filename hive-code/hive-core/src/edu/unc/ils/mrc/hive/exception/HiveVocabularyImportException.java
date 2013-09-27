/**
 * 
 */
package edu.unc.ils.mrc.hive.exception;

/**
 * Generic exception importing vocabularies into HIVE
 * @author Mike Conway - DICE 
 *
 */
public class HiveVocabularyImportException extends HiveException {

	
	private static final long serialVersionUID = -5201011855128541296L;

	public HiveVocabularyImportException() {
	}

	/**
	 * @param arg0
	 */
	public HiveVocabularyImportException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public HiveVocabularyImportException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public HiveVocabularyImportException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}



}
