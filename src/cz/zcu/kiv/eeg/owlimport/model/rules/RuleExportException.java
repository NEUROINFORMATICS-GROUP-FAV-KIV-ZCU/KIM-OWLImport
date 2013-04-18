package cz.zcu.kiv.eeg.owlimport.model.rules;

/**
 * Rule exporting exception.
 * @author Jan Smitka <jan@smitka.org>
 */
public class RuleExportException extends Exception {
	public RuleExportException() {
	}

	public RuleExportException(String message) {
		super(message);
	}

	public RuleExportException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuleExportException(Throwable cause) {
		super(cause);
	}
}
