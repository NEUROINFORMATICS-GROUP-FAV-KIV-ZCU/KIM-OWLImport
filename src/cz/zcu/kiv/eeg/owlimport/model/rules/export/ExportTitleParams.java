package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleParams;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportTitleParams implements IRuleParams {
	private String labelProp;


	public String getLabelProp() {
		return labelProp;
	}

	public void setLabelProp(String labelProp) {
		this.labelProp = labelProp;
	}
}
