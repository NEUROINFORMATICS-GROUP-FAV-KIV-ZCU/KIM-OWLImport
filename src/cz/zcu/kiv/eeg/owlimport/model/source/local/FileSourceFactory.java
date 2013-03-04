package cz.zcu.kiv.eeg.owlimport.model.source.local;

import cz.zcu.kiv.eeg.owlimport.gui.ISourceParamsComponent;
import cz.zcu.kiv.eeg.owlimport.gui.source.FileParamsComponent;
import cz.zcu.kiv.eeg.owlimport.model.source.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.source.ISourceFactory;
import cz.zcu.kiv.eeg.owlimport.model.source.ISourceParams;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class FileSourceFactory implements ISourceFactory {
	private static final String FACTORY_TITLE = "Local File";

	@Override
	public AbstractSource createSource(String title, String baseUrl, ISourceParams parameters) {
		if (!(parameters instanceof FileSourceParams)) {
			throw invalidParametersSetException();
		}

		FileSourceParams fileParams = (FileSourceParams) parameters;
		return new FileSource(title, baseUrl, fileParams.getFile());
	}


	private IllegalArgumentException invalidParametersSetException() {
		return new IllegalArgumentException("Specified parameters set is not an instalce of FileSourceParams.");
	}

	@Override
	public ISourceParamsComponent createGuiComponent() {
		return new FileParamsComponent();
	}

	@Override
	public String toString() {
		return FACTORY_TITLE;
	}
}
