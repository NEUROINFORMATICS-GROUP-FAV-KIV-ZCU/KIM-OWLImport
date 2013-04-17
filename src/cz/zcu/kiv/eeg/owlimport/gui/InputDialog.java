package cz.zcu.kiv.eeg.owlimport.gui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class InputDialog extends JDialog {
	private DialogResult result = DialogResult.None;

	public void setIcon(String resourceUri) {
		ImageIcon icon = new ImageIcon(getClass().getResource(resourceUri));
		setIconImage(icon.getImage());
	}


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


	/**
	 * Sets the position of dialog to center of specified component and shows it.
	 * @param parent Component in which the dialog will be centered.
	 */
	public void showInCenterOf(Component parent) {
		setLocationRelativeTo(parent);
		setVisible(true);
	}


	protected void handleValidationException(ValidationException e) {
		if (e.hasInvalidComponent()) {
			e.getInvalidComponent().requestFocusInWindow();
		}
		JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}
}
