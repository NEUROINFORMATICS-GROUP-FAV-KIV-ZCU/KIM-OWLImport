package cz.zcu.kiv.eeg.owlimport.gui;

import javax.swing.*;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ValidationException extends Exception {
	private JComponent invalidComponent;

	public ValidationException() {
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(String message, JComponent component) {
		super(message);
		invalidComponent = component;
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public boolean hasInvalidComponent() {
		return (invalidComponent != null);
	}

	public JComponent getInvalidComponent() {
		return invalidComponent;
	}
}
