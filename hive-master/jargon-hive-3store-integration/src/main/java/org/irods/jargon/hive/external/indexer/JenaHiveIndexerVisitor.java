package org.irods.jargon.hive.external.indexer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.query.MetaDataAndDomainData;
import org.irods.jargon.core.query.MetaDataAndDomainData.MetadataDomain;
import org.irods.jargon.core.utils.IRODSUriUtils;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitor;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitorInvoker;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitorInvoker.VisitorDesiredAction;
import org.irods.jargon.hive.external.indexer.JenaHiveConfiguration.JenaModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDFS;

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
	private com.hp.hpl.jena.rdf.model.Model jenaModel;
	public static final String MODEL_KEY = "model";
	private boolean ontConfigured = false;
	private OntClass collOnt = null;
	private OntClass dataOnt = null;

	public static final Logger log = LoggerFactory
			.getLogger(JenaHiveIndexerVisitor.class);

	/**
	 * Create a visitor with a given configuration, which dictates the Jena
	 * model type, other rdf to add to the model, etc
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
	 * Initialize Jena data based on jena configuration set via
	 * <code>jenaHiveVisitorConfiguration</code>
	 */
	private void initializeJena() throws JargonException {
		log.info("initializeJena()");
		checkContracts();

		if (jenaHiveVisitorConfiguration.getJenaModelType() == JenaModelType.MEMORY) {
			log.info("building memory model...");

			/*
			 * Note this will eventually be parameterized
			 */

			jenaModel = ModelFactory.createDefaultModel();

		} else if (jenaHiveVisitorConfiguration.getJenaModelType() == JenaModelType.MEMORY_ONT) {
			log.info("building memory ont model...");
			jenaModel = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		} else {
			log.error("model type not currently supported:{}",
					jenaHiveVisitorConfiguration.getJenaModelType());
			throw new JargonException("unsupported jena model type");
		}

		// load iRODS RDF

		if (!getJenaHiveVisitorConfiguration().isIrodsOntologyConfigured()) {

			ontConfigured = false;
			log.info("no ontology file for iRODS, skipped");
		} else {
			log.info("loading iRODS ontology file");
			ontConfigured = true;
			InputStream in = FileManager.get().open(
					jenaHiveVisitorConfiguration.getIrodsRDFFileName());
			if (in == null) {
				log.error(
						"not able to load ontology file for iRODS based on config:{}",
						jenaHiveVisitorConfiguration);
				throw new JargonException("unable to load ontology file");
			}

			// read the RDF/XML file
			jenaModel.read(in, null);
			try {
				in.close();
			} catch (IOException e) {
				log.error("io exception closing stream, ignored");
			}

			OntModel ontModel = (OntModel) jenaModel;

			com.hp.hpl.jena.rdf.model.Resource r = ontModel
					.getResource(JenaHiveConfiguration.NS + "Collection");
			collOnt = r.as(OntClass.class);

			r = ontModel.getResource(JenaHiveConfiguration.NS + "DataObject");

			dataOnt = r.as(OntClass.class);

		}

		// load vocabulary files
		for (String vocabFileName : jenaHiveVisitorConfiguration
				.getVocabularyRDFFileNames()) {
			InputStream in = FileManager.get().open(vocabFileName);
			if (in == null) {
				throw new IllegalArgumentException("File: " + vocabFileName
						+ " not found");
			}

			// read the RDF/XML file
			jenaModel.read(in, null);
			try {
				in.close();
			} catch (IOException e) {
				log.error("io exception closing stream, ignored");
			}

		}
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

		URI irodsURI = IRODSUriUtils
				.buildURIForAnAccountWithNoUserInformationIncluded(
						invoker.getIrodsAccount(),
						metadata.getDomainObjectUniqueName());
		log.info("URI:{}", irodsURI);

		// build the triple
		jenaModel.createResource(irodsURI.toString()).addProperty(RDFS.label,
				metadata.getAvuAttribute());
		log.info("created resource in model");

		if (!ontConfigured) {
			return VisitorDesiredAction.CONTINUE;
		}

		/*
		 * If ontConfigured is true, and I have an irods ontology file, generate
		 * additional iRODS statements
		 */

		log.info("generating additional ontology statements");
		Individual indiv;
		log.info("cast to ontmodel");
		OntModel ontModel = (OntModel) jenaModel;
		log.info("cast done, creating indiv...");
		if (metadata.getMetadataDomain() == MetadataDomain.COLLECTION) {
			indiv = ontModel.createIndividual(irodsURI.toString(), collOnt);
		} else {
			indiv = ontModel.createIndividual(irodsURI.toString(), dataOnt);
		}
		log.info("indiv done create prop");
		Property absPathProp = ontModel.getProperty(JenaHiveConfiguration.NS,
				"absolutePath");
		indiv.addProperty(absPathProp, metadata.getDomainObjectUniqueName());

		return VisitorDesiredAction.CONTINUE;

	}

	/**
	 * Signal from invoker that we have finished processing, useful for any
	 * general processing
	 */
	@Override
	public void complete() throws JargonException {
		log.info("complete()");
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
	 * @return <code>Model</code> generated by Jena
	 */
	public com.hp.hpl.jena.rdf.model.Model getJenaModel() {
		return jenaModel;
	}

}
