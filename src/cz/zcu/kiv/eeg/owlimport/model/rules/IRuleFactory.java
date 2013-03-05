package cz.zcu.kiv.eeg.owlimport.model.rules;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public interface IRuleFactory {
	public AbstractRule createRule(String source, String title);
}
