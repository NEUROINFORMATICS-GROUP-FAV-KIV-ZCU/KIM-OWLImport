package cz.zcu.kiv.eeg.owlimport.model;

import cz.zcu.kiv.eeg.owlimport.RepositoryManager;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceFactory;
import cz.zcu.kiv.eeg.owlimport.model.sources.SourceImportException;
import cz.zcu.kiv.eeg.owlimport.project.ProjectReadException;
import cz.zcu.kiv.eeg.owlimport.project.ProjectReader;
import cz.zcu.kiv.eeg.owlimport.project.ProjectWriteException;
import cz.zcu.kiv.eeg.owlimport.project.ProjectWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class SourceManager {
	private List<ISourceFactory> sourceFactories;

	private List<AbstractSource> sources;

	private SourcesListener listener;

	public SourceManager() {
		sourceFactories = new LinkedList<>();
		sources = new ArrayList<>();
	}

	public SourcesListener getListener() {
		return listener;
	}

	public void setListener(SourcesListener listener) {
		this.listener = listener;
	}

	public void registerSourceFactory(ISourceFactory factory) {
		sourceFactories.add(factory);
	}


	public List<ISourceFactory> getSourceFactories() {
		return sourceFactories;
	}

	public ISourceFactory getFactoryFor(String className) {
		for (ISourceFactory factory : sourceFactories) {
			if (factory.getCreatedClass().getName().equals(className)) {
				return factory;
			}
		}
		return null;
	}


	public void addSource(AbstractSource source) {
		int newIndex = sources.size();
		sources.add(source);
		if (listener != null) {
			listener.sourceAdded(source, newIndex);
		}
	}


	public AbstractSource getSource(int index) {
		return sources.get(index);
	}


	public int getSourcesCount() {
		return sources.size();
	}


	public List<AbstractSource> getSources() {
		return sources;
	}

	public void loadProject(File inputFile, RuleManager rlManager) throws ProjectReadException {
		ProjectReader reader = new ProjectReader(this, rlManager, inputFile);

		if (listener != null && !sources.isEmpty()) {
			listener.sourcesRemoved(sources.size());
		}
		sources = reader.load();
		if (listener != null && !sources.isEmpty()) {
			listener.sourcesLoaded(sources.size());
		}
	}

	public void importSources(RepositoryManager repositoryManager) throws SourceImportException {
		for (AbstractSource source : sources) {
			if (!source.hasRepositoryAttached()) {
				repositoryManager.importSource(source);
			}
		}
	}

	public void saveProject(File outputFile) throws ProjectWriteException {
		ProjectWriter writer = new ProjectWriter(outputFile);
		writer.save(sources);
	}



	public interface SourcesListener {
		public void sourceAdded(AbstractSource source, int index);

		public void sourcesRemoved(int count);

		public void sourcesLoaded(int count);
	}
}
