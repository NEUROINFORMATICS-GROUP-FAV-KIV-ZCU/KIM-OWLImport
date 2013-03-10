package cz.zcu.kiv.eeg.owlimport.gui;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.lang.Override;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
abstract public class ListSingleSelectionAdapter implements ListSelectionListener {
	private JList list;

	public ListSingleSelectionAdapter(JList jList) {
		list = jList;
	}

	public ListSelectionModel getModel() {
		return list.getSelectionModel();
	}

	public int getSelectedIndex(int min, int max) {
		ListSelectionModel model = getModel();
		for (int i = min; i <= max; i++) {
			if (model.isSelectedIndex(i)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public final void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}

		int selectedIndex = getSelectedIndex(e.getFirstIndex(), e.getLastIndex());
		if (selectedIndex >= 0) {
			selectionSelected(selectedIndex, e);
		} else {
			selectionCanceled(e);
		}
	}

	public void selectionChanged(ListSelectionEvent e) {

	}

	public abstract void selectionSelected(int selectedIndex, ListSelectionEvent e);

	public abstract void selectionCanceled(ListSelectionEvent e);
}
