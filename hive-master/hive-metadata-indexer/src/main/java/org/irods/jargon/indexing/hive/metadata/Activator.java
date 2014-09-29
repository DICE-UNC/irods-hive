package org.irods.jargon.indexing.hive.metadata;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import databook.listener.Indexer;
import databook.listener.service.IndexingService;
import databook.local.model.RDFDatabase;

public class Activator implements BundleActivator {
	ServiceRegistration<IndexingService> inxSvc;
	Indexer i;
	RDFDatabase database;
	IndexingService ts;

	@Override
	public void start(BundleContext context) throws Exception {

		System.out.println("hive metadata indexer started");
	}

	@Override
	public void stop(BundleContext context) throws Exception {

		System.out.println("hive metadata indexer stopped");

	}

}
