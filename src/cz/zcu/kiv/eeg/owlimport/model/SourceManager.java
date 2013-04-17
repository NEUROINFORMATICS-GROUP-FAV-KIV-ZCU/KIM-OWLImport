package cz.zcu.kiv.eeg.owlimport.model;

import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceFactory;
import cz.zcu.kiv.eeg.owlimport.project.ProjectReadException;
import cz.zcu.kiv.eeg.owlimport.project.ProjectReader;
import cz.zcu.kiv.eeg.owlimport.project.ProjectWriteException;
import cz.zcu.kiv.eeg.owlimport.project.ProjectWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * SourceManager manages source factories and ontology sources.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public class SourceManager {
	/** List of source factories. */
	private List<ISourceFactory> sourceFactories;
	/** List of ontology sources. */
	private List<AbstractSource> sources;
	/** Sources list listener. */
	private SourceListListener listener;

	/**
	 * Initializes empty sources manager.
	 */
	public SourceManager() {
		sourceFactories = new LinkedList<>();
		sources = new ArrayList<>();
	}

	/**
	 * Gets the currently registered source list listener.
	 * @return Source list listener.
	 */
	public SourceListListener getListener() {
		return listener;
	}

	/**
	 * Sets the source list listener. Only one listener can be attached at the same time.
	 *
	 * Source listener is notified when the list of sources is modified.
	 * @param listener Source list listener.
	 */
	public void setListener(SourceListListener listener) {
		this.listener = listener;
	}

	/**
	 * Registers new source factory.
	 * @param factory Source factory.
	 */
	public void registerSourceFactory(ISourceFactory factory) {
		sourceFactories.add(factory);
	}

	/**
	 * Gets the list of source factories.
	 * @return List of source factories.
	 */
	public List<ISourceFactory> getSourceFactories() {
		return sourceFactories;
	}

	/**
	 * Gets the source factory which creates sources of the given class.
	 * @param className Fully qualified class name.
	 * @return Source factory or {@code null} when the factory for the given class is not found.
	 */
	public ISourceFactory getFactoryFor(String className) {
		for (ISourceFactory factory : sourceFactories) {
			if (factory.getCreatedClass().getName().equals(className)) {
				return factory;
			}
		}
		return null;
	}

	/**
	 * Adds a new source to the source list.
	 * @param source Ontology source.
	 */
	public void addSource(AbstractSource source) {
		int newIndex = sources.size();
		sources.add(source);
		if (listener != null) {
			listener.sourceAdded(source, newIndex);
		}
	}

	/**
	 * Gets the source with given index in the list.
	 * @param index Index.
	 * @return Source at given index.
	 */
	public AbstractSource getSource(int index) {
		return sources.get(index);
	}

	/**
	 * Returns number of all sources.
	 * @return Number of sources.
	 */
	public int getSourcesCount() {
		return sources.size();
	}

	/**
	 * Gets the list of all sources.
	 *
	 * Beware: the list is mutable, but modifications will not trigger SourceListListener events!
	 * @return List of all sources.
	 */
	public List<AbstractSource> getSources() {
		return sources;
	}

	/**
	 * Loads project from XML file and imports all ontology sources into the repository manager.
	 * @param inputFile Input file.
	 * @param rlManager Rule manager for rule creation.
	 * @throws ProjectReadException when the file could not be read.
	 */
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

	/**
	 * Saves the currently defined sources and their rules to XML file.
	 * @param outputFile Output file.
	 * @throws ProjectWriteException when the project could not be written.
	 */
	public void saveProject(File outputFile) throws ProjectWriteException {
		ProjectWriter writer = new ProjectWriter(outputFile);
		writer.save(sources);
	}


	/**
	 * SourceListListener is notified when the internal source list is modified through the SourceManager.
	 */
	public interface SourceListListener {
		/**
		 * Occurs when a new source is added.
		 * @param source New source.
		 * @param index Index of the new source.
		 */
		public void sourceAdded(AbstractSource source, int index);

		/**
		 * Occurs when all sources are removed.
		 * @param count Number of sources removed.
		 */
		public void sourcesRemoved(int count);

		/**
		 * Occurs when list of sources are loaded.
		 * @param count Number of sources loaded.
		 */
		public void sourcesLoaded(int count);
	}
}
