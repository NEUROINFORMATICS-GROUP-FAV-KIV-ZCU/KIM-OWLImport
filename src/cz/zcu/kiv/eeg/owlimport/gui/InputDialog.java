package cz.zcu.kiv.eeg.owlimport.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Abstract class for input dialogs.
 * @author Jan Smitka <jan@smitka.org>
 */
public abstract class InputDialog extends JDialog {
	private DialogResult result = DialogResult.None;

	/**
	 * Sets the dialog window icon from resource.
	 * @param resourceUri URI of the resource. The URI has to be either absolute or relative to the dialog class.
	 */
	public final void setIcon(String resourceUri) {
		ImageIcon icon = new ImageIcon(getClass().getResource(resourceUri));
		setIconImage(icon.getImage());
	}

	/**
	 * Sets the dialog result.
	 * @param dialogResult New dialog result.
	 */
	protected final void setDialogResult(DialogResult dialogResult) {
		result = dialogResult;
	}

	/**
	 * Shortcut for successful submission result.
	 */
	protected final void setDialogResultOk() {
		setDialogResult(DialogResult.OK);
	}

	/**
	 * Shortcut for user cancel result.
	 */
	protected final void setDialogResultCancel() {
		setDialogResult(DialogResult.Cancel);
	}


	/**
	 * Gets the dialog operation result.
	 * @return Dialog result.
	 */
	public final DialogResult getDialogResult() {
		return result;
	}


	/**
	 * Registers keyboard and user-close actions for form disposal.
	 *
	 * It has to be called from descendant after the layout has been initialized in order to be used.
	 */
	protected void registerCancelActions() {
		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		getRootPane().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}


	/**
	 * Confirms the input operation. Must be implemented and used in descendants.
	 */
	protected abstract void onOK();


	/**
	 * Cancels the input operation and closes the form.
	 */
	protected void onCancel() {
		dispose();
	}


	/**
	 * Sets the position of dialog to center of specified component and shows it.
	 * @param parent Component in which the dialog will be centered.
	 */
	public void showInCenterOf(Component parent) {
		setLocationRelativeTo(parent);
		setVisible(true);
	}


	/**
	 * Handles the validation exception - shows error message and requests focus for the invalid control.
	 * @param e Validation exception.
	 */
	protected void handleValidationException(ValidationException e) {
		if (e.hasInvalidComponent()) {
			e.getInvalidComponent().requestFocusInWindow();
		}
		JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}
}
