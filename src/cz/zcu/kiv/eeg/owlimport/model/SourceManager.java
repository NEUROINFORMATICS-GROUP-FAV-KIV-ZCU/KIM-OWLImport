package cz.zcu.kiv.eeg.owlimport.model;

import cz.zcu.kiv.eeg.owlimport.model.source.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.source.ISourceFactory;

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



	public interface SourcesListener {
		public void sourceAdded(AbstractSource source, int index);
	}
}
