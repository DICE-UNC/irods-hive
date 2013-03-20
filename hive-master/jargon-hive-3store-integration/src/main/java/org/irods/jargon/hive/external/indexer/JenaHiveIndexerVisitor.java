package org.irods.jargon.hive.external.indexer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.query.MetaDataAndDomainData;
import org.irods.jargon.core.utils.IRODSUriUtils;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitor;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitorInvoker;
import org.irods.jargon.datautils.visitor.AbstractIRODSVisitorInvoker.VisitorDesiredAction;
import org.irods.jargon.hive.external.indexer.JenaHiveVisitorConfiguration.JenaModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Implementation of a 'visitor' or 'strategy' type object that will be called
 * for each item in a query of HIVE RDF statements about iRODS collections and
 * data objects.
 * <p/>
 * This implementation will build a Jena model based on the incoming data, using
 * information from the given configuration object.
 * 
 * @author Mike Conway - DICE (www.irods.org)
 * 
 */
public class JenaHiveIndexerVisitor extends
		AbstractIRODSVisitor<MetaDataAndDomainData, Model> {

	private JenaHiveVisitorConfiguration jenaHiveVisitorConfiguration = null;
	private com.hp.hpl.jena.rdf.model.Model jenaModel;
	public static final String MODEL_KEY = "model";

	public static final Logger log = LoggerFactory
			.getLogger(JenaHiveIndexerVisitor.class);

	/**
	 * Create a visitor with a given configuration, which dictates the Jena
	 * model type, other rdf to add to the model, etc
	 * 
	 * @param configuration
	 *            {@link JenaHiveVisitorConfiguration}
	 * @throws JargonException
	 */
	public JenaHiveIndexerVisitor(
			final JenaHiveVisitorConfiguration configuration)
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

			// load iRODS RDF

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

		} else {
			log.error("model type not currently supported:{}",
					jenaHiveVisitorConfiguration.getJenaModelType());
			throw new JargonException("unsupported jena model type");
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
	public VisitorDesiredAction invoke(
			final MetaDataAndDomainData metadata,
			final AbstractIRODSVisitorInvoker<MetaDataAndDomainData, Model> invoker)
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

		return VisitorDesiredAction.CONTINUE;
	}

	/**
	 * Finished processing, return the jena model (unclosed) to the caller
	 * 
	 * @param invoker
	 *            {@link AbstractIRODVisitorInvoker} that called this method
	 * @return model Jena <code>Model</code> representing RDF derived from the
	 *         AVU data, as well as any configured additional metadata
	 */
	@Override
	public Model complete(
			final AbstractIRODSVisitorInvoker<MetaDataAndDomainData, Model> invoker)
			throws JargonException {

		log.info("complete()");

		if (invoker == null) {
			throw new IllegalArgumentException("null invoker");
		}

		log.info("returning jena model I built");
		return jenaModel;

	}

	public JenaHiveVisitorConfiguration getJenaHiveVisitorConfiguration() {
		return jenaHiveVisitorConfiguration;
	}

	public void setJenaHiveVisitorConfiguration(
			final JenaHiveVisitorConfiguration jenaHiveVisitorConfiguration) {
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

}
