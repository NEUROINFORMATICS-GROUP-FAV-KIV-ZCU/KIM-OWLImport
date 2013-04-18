package cz.zcu.kiv.eeg.owlimport.gui;

import javax.swing.*;

/**
 * Validation exception, that can optionally contain reference to the component for which the validation failed.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ValidationException extends Exception {
	private JComponent invalidComponent;

	public ValidationException() {
	}

	public ValidationException(String message) {
		super(message);
	}

	/**
	 * Creates validation exception for specified component.
	 * @param message Error message.
	 * @param component Component with invalid data.
	 */
	public ValidationException(String message, JComponent component) {
		super(message);
		invalidComponent = component;
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Checks if the exception contains information about the component with invalid data.
	 * @return {@code true} if the exception contains reference to invalid component.
	 */
	public boolean hasInvalidComponent() {
		return (invalidComponent != null);
	}

	/**
	 * Gets the reference to invalid component.
	 * @return Component reference.
	 */
	public JComponent getInvalidComponent() {
		return invalidComponent;
	}
}
