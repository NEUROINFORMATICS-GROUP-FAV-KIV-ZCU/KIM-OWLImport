package cz.zcu.kiv.eeg.owlimport.gui.model;

import cz.zcu.kiv.eeg.owlimport.model.SourceManager;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;

import javax.swing.*;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class SourceListModel extends AbstractListModel<AbstractSource> {
	private SourceManager sourceManager;

	public SourceListModel(SourceManager manager) {
		sourceManager = manager;
		injectListener();
	}

	private void injectListener() {
		sourceManager.setListener(new SourceManager.SourcesListener() {
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

	@Override
	public int getSize() {
		return sourceManager.getSourcesCount();
	}

	@Override
	public AbstractSource getElementAt(int index) {
		return sourceManager.getSource(index);
	}
}
