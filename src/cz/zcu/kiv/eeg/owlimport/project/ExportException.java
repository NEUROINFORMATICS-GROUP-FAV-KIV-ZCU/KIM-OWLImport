package cz.zcu.kiv.eeg.owlimport.project;

/**
 * Generic export exception.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportException extends Exception {
	public ExportException() {
	}

	public ExportException(String message) {
		super(message);
	}

	public ExportException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExportException(Throwable cause) {
		super(cause);
	}

	public ExportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
