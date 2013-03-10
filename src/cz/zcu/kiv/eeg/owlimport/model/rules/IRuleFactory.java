package cz.zcu.kiv.eeg.owlimport.model.rules;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public interface IRuleFactory {
	public Class getCreatedClass();

	public AbstractRule createRule(String title);
}
