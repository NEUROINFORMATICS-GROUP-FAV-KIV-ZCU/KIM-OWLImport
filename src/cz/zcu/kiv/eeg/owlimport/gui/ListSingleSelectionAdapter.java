package cz.zcu.kiv.eeg.owlimport.gui;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.lang.Override;

/**
 * Abstract listener which simplifies handling of value changed event for lists in single selection mode.
 * @author Jan Smitka <jan@smitka.org>
 */
abstract public class ListSingleSelectionAdapter implements ListSelectionListener {
	private JList list;

	/**
	 * Creates adapter for specified list.
	 * @param jList List in single selection mode.
	 */
	public ListSingleSelectionAdapter(JList jList) {
		list = jList;
	}

	/**
	 * Gets the selection model of the list.
	 * @return Selection model.
	 */
	public ListSelectionModel getModel() {
		return list.getSelectionModel();
	}

	/**
	 * Gets the currently selected index in specified range.
	 * @param min Lowest index of the range.
	 * @param max Highest index of the range.
	 * @return Currently selected index or -1 when there is no selected item.
	 */
	public int getSelectedIndex(int min, int max) {
		ListSelectionModel model = getModel();
		for (int i = min; i <= max; i++) {
			if (model.isSelectedIndex(i)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Handles the value changed event. Calls either {@code selectionSelected()} or {@code selectionCanceled()} handlers.
	 * @param e Event.
	 */
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

	/**
	 * Called when the user selects an item.
	 * @param selectedIndex Selected item.
	 * @param e Original {@code valueChanged} event.
	 */
	public abstract void selectionSelected(int selectedIndex, ListSelectionEvent e);

	/**
	 * Called when user deselects an item.
	 * @param e Original {@code valueChanged} event.
	 */
	public abstract void selectionCanceled(ListSelectionEvent e);
}
