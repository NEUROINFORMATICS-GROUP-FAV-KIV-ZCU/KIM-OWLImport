package cz.zcu.kiv.eeg.owlimport.project;

/**
 * Project write exception.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ProjectWriteException extends Exception {
	public ProjectWriteException() {
	}

	public ProjectWriteException(String message) {
		super(message);
	}

	public ProjectWriteException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectWriteException(Throwable cause) {
		super(cause);
	}

	public ProjectWriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
