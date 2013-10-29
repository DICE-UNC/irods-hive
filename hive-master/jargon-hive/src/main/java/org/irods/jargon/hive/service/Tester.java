package org.irods.jargon.hive.service;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.irods.jargon.hive.container.HiveConfiguration;
import org.irods.jargon.hive.container.HiveContainer;
import org.irods.jargon.hive.container.HiveContainerImpl;
import org.irods.jargon.hive.exception.JargonHiveException;

import org.unc.hive.client.ConceptProxy;


public class Tester {
	
	public static void main (String[] args) throws JargonHiveException {
	
		final HiveConfiguration hiveConfiguration = new HiveConfiguration();
		final HiveContainer hiveContainer = new HiveContainerImpl();
		
		hiveConfiguration.setHiveConfigLocation("/Users/zhangle/temp/hive-data/hive.properties");
		hiveContainer.setHiveConfiguration(hiveConfiguration);
		hiveContainer.init();
		
		VocabularyService vocabularyService = new VocabularyServiceImpl(hiveContainer);
		List<String> openVocabularies = new ArrayList<String>();
		
		openVocabularies = vocabularyService
				.getAllVocabularyNames();
		for(String v : openVocabularies) {
			System.out.println(v);
		}
//		ArrayList<String> openedVocabularies = new ArrayList<String>();
//		openedVocabularies.add("agrovoc");
		
		Set<ConceptProxy> sc = new HashSet<ConceptProxy>();
		sc = vocabularyService.searchConcept("cat", openVocabularies);
		int i = 1;
		for(ConceptProxy cp: sc) {
			System.out.println(i++ + " " + cp.getPreLabel());
		}
		
		hiveContainer.shutdown();
	
	}

}
