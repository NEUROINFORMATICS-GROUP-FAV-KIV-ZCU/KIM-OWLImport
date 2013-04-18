package cz.zcu.kiv.eeg.owlimport.gui;

import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceParams;

import javax.swing.*;

/**
 * Component which allows user to specify ontology source location.
 * @author Jan Smitka <jan@smitka.org>
 */
public interface ISourceParamsComponent {
	/**
	 * Validates the user data in component.
	 * @throws ValidationException when the user data is invalid.
	 */
	public void validate() throws ValidationException;

	/**
	 * Gets the components root panel.
	 * @return Panel.
	 */
	public JComponent getPanel();

	/**
	 * Creates the parameters from user entered data.
	 * @return Source parameters.
	 */
	public ISourceParams getParams();
}
