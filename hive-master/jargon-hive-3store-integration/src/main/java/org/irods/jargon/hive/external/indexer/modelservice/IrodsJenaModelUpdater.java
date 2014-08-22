/**
 *
 */
package org.irods.jargon.hive.external.indexer.modelservice;

import java.net.URI;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.domain.Collection;
import org.irods.jargon.core.pub.domain.DataObject;
import org.irods.jargon.core.service.AbstractJargonService;
import org.irods.jargon.core.utils.IRODSUriUtils;
import org.irods.jargon.dataprofile.DataProfile;
import org.irods.jargon.dataprofile.DataProfileService;
import org.irods.jargon.dataprofile.DataProfileServiceImpl;
import org.irods.jargon.dataprofile.DataTypeResolutionService;
import org.irods.jargon.dataprofile.DataTypeResolutionServiceImpl;
import org.irods.jargon.hive.external.indexer.HiveIndexerException;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.Lock;

/**
 * Maintains a Jena representation of iRODS data, including HIVE vocabularies
 * 
 * @author Mike Conway - DICE
 * 
 * 
 */
public class IrodsJenaModelUpdater extends AbstractJargonService {

	private final OntModel ontModel;
	public static final String MODEL_KEY = "model";
	public static final String NS = "http://www.irods.org/ontologies/2013/2/iRODS.owl#";
	private final DataProfileService dataProfileService;
	private final JenaHiveConfiguration jenaHiveConfiguration;
	private OntClass dataOnt;
	private OntClass collOnt;

	public static final Logger log = LoggerFactory
			.getLogger(IrodsJenaModelUpdater.class);

	/**
	 * @param irodsAccessObjectFactory
	 * @param irodsAccount
	 */
	public IrodsJenaModelUpdater(
			final IRODSAccessObjectFactory irodsAccessObjectFactory,
			final IRODSAccount irodsAccount, final OntModel ontModel,
			final JenaHiveConfiguration jenaHiveConfiguration) {
		super(irodsAccessObjectFactory, irodsAccount);

		if (ontModel == null) {
			throw new IllegalArgumentException("null ontModel");
		}

		if (jenaHiveConfiguration == null) {
			throw new IllegalArgumentException("null jenaHiveConfiguration");
		}
		this.jenaHiveConfiguration = jenaHiveConfiguration;

		this.ontModel = ontModel;

		log.info("creating data profile service components..");
		DataTypeResolutionService dataTypeResolutionService = new DataTypeResolutionServiceImpl(
				irodsAccessObjectFactory, irodsAccount);
		dataProfileService = new DataProfileServiceImpl(
				irodsAccessObjectFactory, irodsAccount,
				dataTypeResolutionService);
		/*
		 * log.info("adding resources for data object"); Resource r =
		 * ontModel.getResource(NS + "DataObject"); dataOnt =
		 * r.as(OntClass.class); log.info("adding resources for collection"); r
		 * = ontModel.getResource(NS + "Collection"); collOnt =
		 * r.as(OntClass.class);
		 */
		log.info("done...");

	}

	public OntModel getOntModel() {
		return ontModel;
	}

	@SuppressWarnings("unchecked")
	public void addIrodsTerm(final String irodsAbsolutePath,
			final String vocabularyUri) throws HiveIndexerException {
		log.info("addIrodsPathToModel()");

		if (irodsAbsolutePath == null || irodsAbsolutePath.isEmpty()) {
			throw new IllegalArgumentException("null irodsAbsolutePath");
		}

		if (vocabularyUri == null || vocabularyUri.isEmpty()) {
			throw new IllegalArgumentException("null vocabularyUri");
		}

		log.info("adding term:{}", vocabularyUri);
		log.info("to irodsPath:{}", irodsAbsolutePath);

		try {
			@SuppressWarnings("rawtypes")
			DataProfile dataProfile = dataProfileService
					.retrieveDataProfile(irodsAbsolutePath);
			log.info("got dataProfile:{}", dataProfile);

			log.info("generating  ontology statements");
			log.info("creating indiv...");
			if (dataProfile.isFile()) {

				addDataObject(dataProfile, vocabularyUri);

			} else {
				addCollectionObject(dataProfile, vocabularyUri);
			}
		} catch (JargonException e) {
			log.error("Jargon error building index", e);
			throw new HiveIndexerException("exception in hive indexer", e);
		}

	}

	private void addCollectionObject(final DataProfile<Collection> dataProfile,
			final String vocabularyUri) throws JargonException {
		Collection collection = dataProfile.getDomainObject();
		URI irodsURI = IRODSUriUtils
				.buildURIForAnAccountWithNoUserInformationIncluded(
						getIrodsAccount(), collection.getAbsolutePath());
		log.info("URI:{}", irodsURI);

		ontModel.enterCriticalSection(Lock.WRITE);
		try {

			log.info("look for an individual with that iRODS uri...");
			Individual indiv = ontModel.getIndividual(irodsURI.toString());

			if (indiv == null) {
				log.info("no individual found, so create base ont class");
				indiv = ontModel.createIndividual(irodsURI.toString(), collOnt);
				Property absPathProp = ontModel.getProperty(
						JenaHiveConfiguration.NS, "absolutePath");
				indiv.addProperty(absPathProp, collection.getAbsolutePath());

			}
			log.info("indiv done create prop");

			Property conceptProp = ontModel.getProperty(
					JenaHiveConfiguration.NS, "correspondingConcept");
			Resource concept = ontModel.getResource(vocabularyUri);
			Selector conceptSelector = new SimpleSelector(indiv, conceptProp,
					concept);
			StmtIterator iter = ontModel.listStatements(conceptSelector);

			if (iter.hasNext()) {
				log.info("concept already present");
			} else {
				log.info("concept added");
				indiv.addProperty(conceptProp, concept);
			}

		} finally {
			ontModel.leaveCriticalSection();
		}

	}

	private void addDataObject(final DataProfile<DataObject> dataProfile,
			final String vocabularyUri) throws JargonException {
		ontModel.enterCriticalSection(Lock.WRITE);
		try {
			DataObject dataObject = dataProfile.getDomainObject();
			URI irodsURI = IRODSUriUtils
					.buildURIForAnAccountWithNoUserInformationIncluded(
							getIrodsAccount(), dataObject.getAbsolutePath());
			log.info("URI:{}", irodsURI);

			log.info("look for an individual with that iRODS uri...");
			Individual indiv = ontModel.getIndividual(irodsURI.toString());

			if (indiv == null) {
				log.info("no individual found, so create base ont class");
				indiv = ontModel.createIndividual(irodsURI.toString(), dataOnt);
				Property absPathProp = ontModel.getProperty(
						JenaHiveConfiguration.NS, "absolutePath");
				indiv.addProperty(absPathProp, dataObject.getAbsolutePath());
				if (!jenaHiveConfiguration.getIdropContext().isEmpty()) {
					log.info("generating idrop links");
					StringBuilder publicLink = new StringBuilder();
					publicLink.append(jenaHiveConfiguration.getIdropContext());
					publicLink.append("/home/link?irodsURI=");
					publicLink.append(IRODSUriUtils
							.buildURIForAnAccountWithNoUserInformationIncluded(
									getIrodsAccount(),
									dataObject.getAbsolutePath()));
					Property publicLinkProp = ontModel.getProperty(
							JenaHiveConfiguration.NS, "hasWebInformationLink");
					Resource concept = ontModel.createResource(publicLink
							.toString());
					indiv.addProperty(publicLinkProp, concept);

					log.info("adding download link");
					StringBuilder downloadLink = new StringBuilder();
					downloadLink
							.append(jenaHiveConfiguration.getIdropContext());
					downloadLink.append("file/download?uri=");
					downloadLink.append(IRODSUriUtils
							.buildURIForAnAccountWithNoUserInformationIncluded(
									getIrodsAccount(),
									dataObject.getAbsolutePath()));
					Property downlaodProp = ontModel.getProperty(
							JenaHiveConfiguration.NS, "hasDownloadLocation");
					concept = ontModel.createResource(downloadLink.toString());
					indiv.addProperty(downlaodProp, concept);

				}
			}
			log.info("indiv done create prop");

			Property conceptProp = ontModel.getProperty(
					JenaHiveConfiguration.NS, "correspondingConcept");
			Resource concept = ontModel.getResource(vocabularyUri);
			Selector conceptSelector = new SimpleSelector(indiv, conceptProp,
					concept);
			StmtIterator iter = ontModel.listStatements(conceptSelector);

			if (iter.hasNext()) {
				log.info("concept already present");
			} else {
				log.info("concept added");
				indiv.addProperty(conceptProp, concept);
			}

		} finally {
			ontModel.leaveCriticalSection();
		}

	}

}
