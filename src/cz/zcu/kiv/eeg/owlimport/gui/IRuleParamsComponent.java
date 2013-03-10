package cz.zcu.kiv.eeg.owlimport.gui;

import javax.swing.*;

/**
 *
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public interface IRuleParamsComponent {
	public void validate() throws ValidationException;

	public JComponent getPanel();

	public void refresh();
}
