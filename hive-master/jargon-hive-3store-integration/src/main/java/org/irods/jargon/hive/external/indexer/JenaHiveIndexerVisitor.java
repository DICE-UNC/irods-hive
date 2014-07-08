package org.irods.jargon.hive.external.indexer;

import java.net.URI;

import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.query.MetaDataAndDomainData;
import org.irods.jargon.core.query.MetaDataAndDomainData.MetadataDomain;
import org.irods.jargon.core.utils.IRODSUriUtils;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitor;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitorInvoker;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitorInvoker.VisitorDesiredAction;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;
import org.irods.jargon.hive.external.utils.JenaModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * 'strategy' type object that will be called for each item in a query of HIVE
 * RDF statements about iRODS collections and data objects.
 * <p/>
 * This implementation will build a Jena model based on the incoming data, using
 * information from the given configuration object.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class JenaHiveIndexerVisitor extends
		AbstractIRODSVisitor<MetaDataAndDomainData> {

	private JenaHiveConfiguration jenaHiveVisitorConfiguration = null;
	private final JenaModelManager jenaModelManager = null;
	private OntModel jenaModel;
	public static final String MODEL_KEY = "model";
	private final OntClass collOnt = null;
	private final OntClass dataOnt = null;

	public static final Logger log = LoggerFactory
			.getLogger(JenaHiveIndexerVisitor.class);

	/**
	 * Create a visitor with a given configuration, which dictates the Jena
	 * model type, other rdf to add to the model, etc
	 * <p/>
	 * This variant will load a new ontModel based on the configuration.
	 * 
	 * @param configuration
	 *            {@link JenaHiveConfiguration}
	 * @throws JargonException
	 */
	public JenaHiveIndexerVisitor(final JenaHiveConfiguration configuration)
			throws JargonException {

		if (configuration == null) {
			throw new IllegalArgumentException("null configuration");
		}

		jenaHiveVisitorConfiguration = configuration;
		initializeJena();
		log.info("jena initialized....");

	}

	/**
	 * Create a visitor based on a given <code>OntModel</code> that is
	 * pre-seeded with vocabulary and iRODS ontology data.
	 * 
	 * @param configuration
	 *            {@link JenaHiveConfiguration} that describes the
	 *            <code>OntModel</code>
	 * @param ontModel
	 *            {@link OntModel} that has been pre-built with required
	 *            vocabularies and ontologies
	 * @throws JargonException
	 */
	public JenaHiveIndexerVisitor(final JenaHiveConfiguration configuration,
			final OntModel ontModel) throws JargonException {

		if (configuration == null) {
			throw new IllegalArgumentException("null configuration");
		}

		if (ontModel == null) {
			throw new IllegalArgumentException("null ontModel");
		}

		jenaHiveVisitorConfiguration = configuration;
		this.jenaModel = ontModel;
		log.info("jena initialized....");

	}

	/**
	 * Initialize Jena data based on jena configuration set via
	 * <code>jenaHiveVisitorConfiguration</code>
	 */
	private void initializeJena() throws JargonException {
		log.info("initializeJena()");
		checkContracts();

		HiveTripleStoreInitializer tripleStoreInitializer = new HiveTripleStoreInitializerImpl(
				jenaHiveVisitorConfiguration);
		this.jenaModel = tripleStoreInitializer.initialize();
		log.info("initialized");
	}

	/**
	 * Called by invoker, will be passed a metadata entry that should be treated
	 * as a vocabulary term applied to a given iRODS collection or file.
	 * <p/>
	 * The validity of the metadata is assumed and not re-checked here
	 * 
	 * @param metadata
	 *            {@link MetaDataAndDomainData} entry with AVU encoded RDF
	 *            representing a HIVE vocabulary term
	 * @param invoker
	 *            {@link AbstractIRODVisitorInvoker} that called this method
	 */
	@Override
	public VisitorDesiredAction invoke(final MetaDataAndDomainData metadata,
			final AbstractIRODSVisitorInvoker<MetaDataAndDomainData> invoker)
			throws JargonException {

		log.info("invoke()");
		if (metadata == null) {
			throw new IllegalArgumentException("null metadata");
		}

		if (invoker == null) {
			throw new IllegalArgumentException("null invoker");
		}

		log.info("metadata:{}", metadata);

		if (metadata.getDomainObjectUniqueName().indexOf("/trash") > -1) {
			log.info("ignoring trash");
			return VisitorDesiredAction.CONTINUE;
		}

		URI irodsURI = IRODSUriUtils
				.buildURIForAnAccountWithNoUserInformationIncluded(
						invoker.getIrodsAccount(),
						metadata.getDomainObjectUniqueName());
		log.info("URI:{}", irodsURI);

		log.info("generating  ontology statements");
		Individual indiv;

		log.info("creating indiv...");
		if (metadata.getMetadataDomain() == MetadataDomain.COLLECTION) {
			indiv = jenaModel.createIndividual(irodsURI.toString(), collOnt);
		} else {
			indiv = jenaModel.createIndividual(irodsURI.toString(), dataOnt);
		}

		log.info("indiv done create prop");
		Property absPathProp = jenaModel.getProperty(JenaHiveConfiguration.NS,
				"absolutePath");
		indiv.addProperty(absPathProp, metadata.getDomainObjectUniqueName());
		Property conceptProp = jenaModel.getProperty(JenaHiveConfiguration.NS,
				"correspondingConcept");
		com.hp.hpl.jena.rdf.model.Resource concept = jenaModel
				.createResource(metadata.getAvuAttribute());
		indiv.addProperty(conceptProp, concept);

		/*
		 * if idrop context, add some links
		 */

		if (!jenaHiveVisitorConfiguration.getIdropContext().isEmpty()) {
			log.info("generating idrop links");
			StringBuilder publicLink = new StringBuilder();
			publicLink.append(jenaHiveVisitorConfiguration.getIdropContext());
			publicLink.append("/home/link?irodsURI=");
			publicLink.append(IRODSUriUtils
					.buildURIForAnAccountWithNoUserInformationIncluded(
							invoker.getIrodsAccount(),
							metadata.getDomainObjectUniqueName()));
			Property publicLinkProp = jenaModel.getProperty(
					JenaHiveConfiguration.NS, "hasWebInformationLink");
			concept = jenaModel.createResource(publicLink.toString());
			indiv.addProperty(publicLinkProp, concept);

			if (metadata.getMetadataDomain() == MetadataDomain.DATA) {
				log.info("adding download link");
				StringBuilder downloadLink = new StringBuilder();
				downloadLink.append(jenaHiveVisitorConfiguration
						.getIdropContext());
				downloadLink.append("/file/download");
				downloadLink.append(metadata.getDomainObjectUniqueName());
				Property downlaodProp = jenaModel.getProperty(
						JenaHiveConfiguration.NS, "hasDownloadLocation");
				concept = jenaModel.createResource(downloadLink.toString());
				indiv.addProperty(downlaodProp, concept);
			}

		}

		return VisitorDesiredAction.CONTINUE;

	}

	/**
	 * Signal from invoker that we have finished processing, useful for any
	 * general processing
	 */
	@Override
	public void complete() throws JargonException {
		log.info("complete()");
		if (jenaModelManager != null) {
			jenaModelManager.close();
		}
	}

	public JenaHiveConfiguration getJenaHiveVisitorConfiguration() {
		return jenaHiveVisitorConfiguration;
	}

	public void setJenaHiveVisitorConfiguration(
			final JenaHiveConfiguration jenaHiveVisitorConfiguration) {
		this.jenaHiveVisitorConfiguration = jenaHiveVisitorConfiguration;
	}

	/**
	 * Make sure all dependencies are set
	 * 
	 * @throws JargonException
	 */
	private void checkContracts() throws JargonException {
		if (jenaHiveVisitorConfiguration == null) {
			throw new JargonException("null jenaHiveVisitorConfiguration");
		}
	}

	/**
	 * Get the resulting Jena model, of the type specified in the configuration.
	 * Note that the model is not closed, and this close is the responsibiltiy
	 * of the caller!
	 * 
	 * @return <code>OntModel</code> generated by Jena
	 */
	public OntModel getJenaModel() {
		return jenaModel;
	}

}
