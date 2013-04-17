package cz.zcu.kiv.eeg.owlimport;

import cz.zcu.kiv.eeg.owlimport.gui.MainDialog;
import cz.zcu.kiv.eeg.owlimport.model.RuleManager;
import cz.zcu.kiv.eeg.owlimport.model.SourceManager;
import cz.zcu.kiv.eeg.owlimport.model.rules.export.*;
import cz.zcu.kiv.eeg.owlimport.model.sources.local.FileSourceFactory;
import cz.zcu.kiv.eeg.owlimport.model.sources.remote.UrlSourceFactory;
import cz.zcu.kiv.eeg.owlimport.repository.RepositoryManager;
import cz.zcu.kiv.eeg.owlimport.repository.RepositoryManagerException;

/**
 * Main application.
 * @author Jan Smitka <jan@smitka.org>
 */
public class OWLImport {
	/**
	 * Application entry-point.
	 *
	 * Initializes the repositories and managers and runs the GUI.
	 *
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		try {
			RepositoryManager repoManager = createRepositoryManager();
			SourceManager srcManager = createSourceManager();
			RuleManager rlManager = createRuleManager();

			MainDialog.run(repoManager, srcManager, rlManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Creates the repository manager.
	 * @return New repository manager.
	 * @throws RepositoryManagerException when the repository manager could not be initialized.
	 */
	private static RepositoryManager createRepositoryManager() throws RepositoryManagerException {
		return new RepositoryManager();
	}


	/**
	 * Creates the ontology source manager.
	 * @return New source manager.
	 */
	private static SourceManager createSourceManager() {
		SourceManager srcManager = new SourceManager();
		srcManager.registerSourceFactory(new FileSourceFactory());
		srcManager.registerSourceFactory(new UrlSourceFactory());
		return srcManager;
	}


	/**
	 * Creates the export/transformation rule manager.
	 * @return New rule manager.
	 */
	private static RuleManager createRuleManager() {
		RuleManager rlManager = new RuleManager();
		rlManager.registerFactory(new ExportTitleRuleFactory());
		rlManager.registerFactory(new ExportClassesRuleFactory());
		rlManager.registerFactory(new ProtonAlignRuleFactory());
		rlManager.registerFactory(new TrustedSourceRuleFactory());
		rlManager.registerFactory(new ExportInstancesRuleFactory());
		return rlManager;
	}
}
