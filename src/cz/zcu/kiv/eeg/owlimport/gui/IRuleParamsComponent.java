package cz.zcu.kiv.eeg.owlimport.gui;

import javax.swing.*;

/**
 * Component which enables user to modify rule parameters.
 * @author Jan Smitka <jan@smitka.org>
 */
public interface IRuleParamsComponent {
	/**
	 * Gets the component panel.
	 * @return Panel.
	 */
	public JComponent getPanel();

	/**
	 * Reloads the data from rule parameters.
	 */
	public void refresh();
}
