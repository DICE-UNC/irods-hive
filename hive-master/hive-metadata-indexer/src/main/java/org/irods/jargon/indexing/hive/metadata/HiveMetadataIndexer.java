/**
 *
 */
package org.irods.jargon.indexing.hive.metadata;

import java.util.Properties;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.hive.external.indexer.HiveIndexerException;
import org.irods.jargon.hive.external.indexer.modelservice.IrodsJenaModelUpdater;
import org.irods.jargon.hive.external.utils.JenaHiveConfiguration;
import org.irods.jargon.hive.external.utils.JenaHiveConfigurationHelper;
import org.irods.jargon.hive.external.utils.JenaModelManager;
import org.irods.jargon.hive.irods.IRODSHiveService;
import org.irods.jargon.indexing.wrapper.GeneralIndexerRuntimeException;
import org.irods.jargon.indexing.wrapper.IndexerWrapper;
import org.irods.jargon.indexing.wrapper.event.MetadataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * Indexer responds to metadata updates to maintain a triple store of iRODS data
 * 
 * @author Mike Conway - DICE
 * 
 */
public class HiveMetadataIndexer extends IndexerWrapper {

	public static final Logger log = LoggerFactory
			.getLogger(HiveMetadataIndexer.class);

	private JenaHiveConfiguration jenaHiveConfiguration;
	private IRODSAccount indexerAccount;
	private OntModel ontModel;
	private IrodsJenaModelUpdater modelUpdater;
	private IRODSFileSystem irodsFileSystem;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.indexing.wrapper.IndexerWrapper#onStartup()
	 */
	@Override
	protected void onStartup() {
		log.info(">>>>>>>>> startup of hive metadata indexer");
		log.info("HIVE indexer version:{}", HiveIndexerVersion.VERSION);
		log.info("HIVE indexer build time:{}", HiveIndexerVersion.BUILD_TIME);

		log.info("starting up connection to Jena");
		try {
			irodsFileSystem = IRODSFileSystem.instance();
			Properties properties = JenaHiveConfigurationHelper
					.loadProperties("indexer.properties");

			jenaHiveConfiguration = JenaHiveConfigurationHelper
					.buildJenaHiveConfigurationFromProperties(properties);
			log.info("built jena hive configuration... now establish an indexing account");

			indexerAccount = JenaHiveConfigurationHelper
					.buildIRODSAccountFromProperties(properties);
			log.info("have irodsAccount, now initialize jena model service:{}",
					indexerAccount);

			JenaModelManager jenaModelManager = new JenaModelManager();
			ontModel = jenaModelManager
					.buildJenaDatabaseModel(jenaHiveConfiguration);
			log.info("set up ont model, now set up model updater service");
			modelUpdater = new IrodsJenaModelUpdater(
					irodsFileSystem.getIRODSAccessObjectFactory(),
					indexerAccount, ontModel, jenaHiveConfiguration);
			log.info("model updater ready:{}", modelUpdater);

		} catch (HiveIndexerException e) {
			log.error("error loading startup properties", e);
			throw new GeneralIndexerRuntimeException(
					"error loading properties at startup", e);
		} catch (JargonException e) {
			log.error("jargon exception starting up components", e);
			throw new GeneralIndexerRuntimeException(
					"jargon exception at component startup", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.irods.jargon.indexing.wrapper.IndexerWrapper#onShutdown()
	 */
	@Override
	protected void onShutdown() {
		log.info("<<<<<<<<<< shutdown of hive metadata indexer");

	}

	@Override
	protected void onMetadataAdd(final MetadataEvent addMetadataEvent) {
		log.info("HIVE avu add?");

		if (!isHiveAvu(addMetadataEvent)) {
			log.info("ignored...not a HIVE AVU");
			return;
		}

		log.info("process this as a HIVE AVU:{}", addMetadataEvent);

		try {
			modelUpdater.addIrodsTerm(addMetadataEvent.getIrodsAbsolutePath(),
					addMetadataEvent.getAvuData().getAttribute());
		} catch (HiveIndexerException e) {
			log.error("exception on add of term to iRODS", e);
			throw new GeneralIndexerRuntimeException(
					"exception adding vocabulary term to irods", e);

		}

	}

	private boolean isHiveAvu(final MetadataEvent addMetadataEvent) {
		if (addMetadataEvent.getAvuData().getUnit()
				.equals(IRODSHiveService.VOCABULARY_AVU_UNIT)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.irods.jargon.indexing.wrapper.IndexerWrapper#onMetadataDelete(org
	 * .irods.jargon.indexing.wrapper.event.MetadataEvent)
	 */
	@Override
	protected void onMetadataDelete(final MetadataEvent deleteMetadataEvent) {
		log.info("HIVE avu delete?");

		if (!isHiveAvu(deleteMetadataEvent)) {
			log.info("ignored...not a HIVE AVU");
			return;
		}

		log.info("process this as a HIVE AVU:{}", deleteMetadataEvent);
	}

	public JenaHiveConfiguration getJenaHiveConfiguration() {
		return jenaHiveConfiguration;
	}

	public void setJenaHiveConfiguration(
			final JenaHiveConfiguration jenaHiveConfiguration) {
		this.jenaHiveConfiguration = jenaHiveConfiguration;
	}

	public IRODSAccount getIndexerAccount() {
		return indexerAccount;
	}

	public void setIndexerAccount(final IRODSAccount indexerAccount) {
		this.indexerAccount = indexerAccount;
	}

	public OntModel getOntModel() {
		return ontModel;
	}

	public void setOntModel(final OntModel ontModel) {
		this.ontModel = ontModel;
	}

	public IrodsJenaModelUpdater getModelUpdater() {
		return modelUpdater;
	}

	public void setModelUpdater(final IrodsJenaModelUpdater modelUpdater) {
		this.modelUpdater = modelUpdater;
	}

	public IRODSFileSystem getIrodsFileSystem() {
		return irodsFileSystem;
	}

	public void setIrodsFileSystem(final IRODSFileSystem irodsFileSystem) {
		this.irodsFileSystem = irodsFileSystem;
	}

}
