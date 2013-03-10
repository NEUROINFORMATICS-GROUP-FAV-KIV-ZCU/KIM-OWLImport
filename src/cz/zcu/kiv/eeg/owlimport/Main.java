package cz.zcu.kiv.eeg.owlimport;

import cz.zcu.kiv.eeg.owlimport.gui.MainDialog;
import cz.zcu.kiv.eeg.owlimport.model.RuleManager;
import cz.zcu.kiv.eeg.owlimport.model.SourceManager;
import cz.zcu.kiv.eeg.owlimport.model.rules.export.ExportTitleRuleFactory;
import cz.zcu.kiv.eeg.owlimport.model.sources.local.FileSourceFactory;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class Main {


	public static void main(String[] args) {
		try {
			RepositoryManager repoManager = createRepositoryManager();
			SourceManager srcManager = createSourceManager();
			RuleManager rlManager = createRuleManager();

			MainDialog.run(repoManager, srcManager, rlManager);
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}


	private static RepositoryManager createRepositoryManager() throws RepositoryManagerException {
		return new RepositoryManager();
	}

	private static SourceManager createSourceManager() {
		SourceManager srcManager = new SourceManager();
		srcManager.registerSourceFactory(new FileSourceFactory());
		return srcManager;
	}

	private static RuleManager createRuleManager() {
		RuleManager rlManager = new RuleManager();
		rlManager.registerFactory(new ExportTitleRuleFactory());
		return rlManager;
	}
}
