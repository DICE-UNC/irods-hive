/**
 * 
 */
package org.irods.jargon.indexing.hive.metadata.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.utils.LocalFileUtils;
import org.irods.jargon.indexing.wrapper.IndexerException;

/**
 * General utils for handling vocabularies
 * @author Mike Conway - DICE
 * 
 *
 */
public class VocabularyUtils {
	
	/**
	 * Given a path to a classpath resoruce, return that resource as a
	 * <code>File</code>
	 * 
	 * @param resourcePath
	 *            <code>String</code> with an absolute path to a resource in the
	 *            classpath
	 * @return <code>File</code> representing the resource in the classpath
	 * @throws JargonException
	 */
	public static File getClasspathResourceAsFile(final String resourcePath)
			throws IndexerException {

		if (resourcePath == null || resourcePath.isEmpty()) {
			throw new IllegalArgumentException("null or empty resourcePath");
		}
		// Load the directory as a resource
		URL resourceUrl = LocalFileUtils.class.getResource(resourcePath);

		if (resourceUrl == null) {
			throw new IndexerException("null resource, cannot find file");
		}

		// Turn the resource into a File object
		try {
			File resourceFile = new File(resourceUrl.toURI());
			if (!resourceFile.exists()) {
				throw new IndexerException("resource file does not exist");
			}
			return resourceFile;
		} catch (URISyntaxException e) {
			throw new IndexerException("unable to create uri from file path");
		}
	}

}
