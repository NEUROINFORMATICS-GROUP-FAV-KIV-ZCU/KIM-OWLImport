package cz.zcu.kiv.eeg.owlimport.model.sources;

/**
 * Source import exception.
 * @author Jan Smitka <jan@smitka.org>
 */
public class SourceImportException extends Exception {
	public SourceImportException() {
	}

	public SourceImportException(String message) {
		super(message);
	}

	public SourceImportException(String message, Throwable cause) {
		super(message, cause);
	}

	public SourceImportException(Throwable cause) {
		super(cause);
	}

	public SourceImportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
