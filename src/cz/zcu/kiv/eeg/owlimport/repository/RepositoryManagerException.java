package cz.zcu.kiv.eeg.owlimport.repository;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class RepositoryManagerException extends Exception {
	public RepositoryManagerException() {
	}

	public RepositoryManagerException(String message) {
		super(message);
	}

	public RepositoryManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepositoryManagerException(Throwable cause) {
		super(cause);
	}

	public RepositoryManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
