package cz.zcu.kiv.eeg.owlimport.gui.model;

import cz.zcu.kiv.eeg.owlimport.model.SourceManager;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;

import javax.swing.*;

/**
 * Model for the list of sources in source manager.
 * @author Jan Smitka <jan@smitka.org>
 */
public class SourceListModel extends AbstractListModel<AbstractSource> {
	private SourceManager sourceManager;

	/**
	 * Initializes the source list model bound to specified source manager.
	 * @param manager
	 */
	public SourceListModel(SourceManager manager) {
		sourceManager = manager;
		injectListener();
	}

	/**
	 * Registers the source list listener.
	 */
	private void injectListener() {
		sourceManager.setListener(new SourceManager.SourceListListener() {
			@Override
			public void sourceAdded(AbstractSource source, int index) {
				fireIntervalAdded(SourceListModel.this, index, index);
			}

			@Override
			public void sourcesRemoved(int count) {
				fireIntervalRemoved(SourceListModel.this, 0, count - 1);
			}

			@Override
			public void sourcesLoaded(int count) {
				fireIntervalAdded(SourceListModel.this, 0, count - 1);
			}
		});
	}

	/**
	 * Gets the number of defined sources.
	 * @return Number of sources.
	 */
	@Override
	public int getSize() {
		return sourceManager.getSourcesCount();
	}

	/**
	 * Gets the ontology source at specified index in the list.
	 * @param index Index.
	 * @return Ontology source.
	 */
	@Override
	public AbstractSource getElementAt(int index) {
		return sourceManager.getSource(index);
	}
}
