package cz.zcu.kiv.eeg.owlimport.gui;

import javax.swing.*;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class InputDialog extends JDialog {
	private DialogResult result = DialogResult.None;


	protected void setDialogResult(DialogResult dialogResult) {
		result = dialogResult;
	}

	/**
	 * Shortcut for successful submission result.
	 */
	protected void setDialogResultOk() {
		setDialogResult(DialogResult.OK);
	}

	/**
	 * Shortcut for user cancel result.
	 */
	protected void setDialogResultCancel() {
		setDialogResult(DialogResult.Cancel);
	}


	/**
	 * Gets the dialog operation result.
	 * @return
	 */
	public DialogResult getDialogResult() {
		return result;
	}
}
