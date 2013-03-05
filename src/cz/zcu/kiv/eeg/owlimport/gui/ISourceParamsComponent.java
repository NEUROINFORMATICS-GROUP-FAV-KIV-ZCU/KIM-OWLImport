package cz.zcu.kiv.eeg.owlimport.gui;

import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceParams;

import javax.swing.*;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public interface ISourceParamsComponent {

	public void validate() throws ValidationException;

	public JComponent getPanel();

	public ISourceParams getParams();
}
