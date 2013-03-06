package org.irods.jargon.hive.irods;

import java.util.ArrayList;
import java.util.List;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.FileNotFoundException;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.CollectionAO;
import org.irods.jargon.core.pub.CollectionAndDataObjectListAndSearchAO;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.domain.ObjStat;
import org.irods.jargon.core.query.AVUQueryElement;
import org.irods.jargon.core.query.AVUQueryElement.AVUQueryPart;
import org.irods.jargon.core.query.AVUQueryOperatorEnum;
import org.irods.jargon.core.query.JargonQueryException;
import org.irods.jargon.hive.irods.exception.IRODSHiveException;
import org.irods.jargon.usertagging.AbstractIRODSTaggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class IRODSHiveServiceImpl extends AbstractIRODSTaggingService {

	public static final Logger log = LoggerFactory
			.getLogger(IRODSHiveServiceImpl.class);

	public static final String VOCABULARY_AVU_UNIT = "iRODSUserTagging:HIVE:Vocabulary";

	/**
	 * @param irodsAccessObjectFactory
	 * @param irodsAccount
	 */
	protected IRODSHiveServiceImpl(
			IRODSAccessObjectFactory irodsAccessObjectFactory,
			IRODSAccount irodsAccount) {
		super(irodsAccessObjectFactory, irodsAccount);
	}

	public void markSelectedVocabularies(final String irodsAbsolutePath,
			final List<String> vocabularyNames) throws IRODSHiveException {

		log.info("markSelectedVocabularies");

		if (irodsAbsolutePath == null || irodsAbsolutePath.isEmpty()) {
			throw new IllegalArgumentException(
					"null or empty irodsAbsolutePath");
		}

		if (vocabularyNames == null) {
			throw new IllegalArgumentException(
					"null vocabularyNames, give an empty list if no names are desired");
		}

	}

	public List<String> listVocabulariesMarkedForIRODSAbsolutePath(
			final String irodsAbsolutePath) throws FileNotFoundException,
			IRODSHiveException {

		log.info("markSelectedVocabularies");

		if (irodsAbsolutePath == null || irodsAbsolutePath.isEmpty()) {
			throw new IllegalArgumentException(
					"null or empty irodsAbsolutePath");
		}

		log.info("listing vocabularies for absPath:{}", irodsAbsolutePath);

		try {
			CollectionAndDataObjectListAndSearchAO collectionAndDataObjectListAndSearchAO = this
					.getIrodsAccessObjectFactory()
					.getCollectionAndDataObjectListAndSearchAO(
							getIrodsAccount());
			ObjStat objStat = collectionAndDataObjectListAndSearchAO
					.retrieveObjectStatForPath(irodsAbsolutePath);
			log.info("got objStat:{}", objStat);

			if (objStat.isSomeTypeOfCollection()) {
				log.info("is a collection...");
				CollectionAO collectionAO = this.getIrodsAccessObjectFactory()
						.getCollectionAO(getIrodsAccount());

				List<AVUQueryElement> avuQueryElements = new ArrayList<AVUQueryElement>(
						1);
				avuQueryElements.add(AVUQueryElement.instanceForValueQuery(
						AVUQueryPart.UNITS, AVUQueryOperatorEnum.EQUAL,
						VOCABULARY_AVU_UNIT));

			}

		} catch (JargonException e) {
			log.error("error getting access object", e);
			throw new IRODSHiveException(e);
		} catch (JargonQueryException e) {
			log.error("query exception getting vocabularies", e);
			throw new JargonException(e);
		}

	}
}
