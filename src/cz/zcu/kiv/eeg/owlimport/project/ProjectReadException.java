package cz.zcu.kiv.eeg.owlimport.project;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ProjectReadException extends Exception {
	public ProjectReadException() {
	}

	public ProjectReadException(String message) {
		super(message);
	}

	public ProjectReadException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectReadException(Throwable cause) {
		super(cause);
	}

	public ProjectReadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
