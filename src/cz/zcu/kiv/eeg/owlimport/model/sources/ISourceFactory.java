package cz.zcu.kiv.eeg.owlimport.model.sources;

import cz.zcu.kiv.eeg.owlimport.gui.ISourceParamsComponent;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public interface ISourceFactory {
	public AbstractSource createSource(String title, String baseUrl, ISourceParams parameters);

	public ISourceParamsComponent createGuiComponent();
}
