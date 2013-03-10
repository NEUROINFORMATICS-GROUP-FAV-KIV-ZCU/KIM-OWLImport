package cz.zcu.kiv.eeg.owlimport.model.rules;

import cz.zcu.kiv.eeg.owlimport.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.gui.IRuleParamsComponent;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import org.openrdf.query.GraphQueryResult;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public abstract class AbstractRule {
	private AbstractSource source;

	private final String title;

	private IRuleParamsComponent guiComponent;


	public AbstractRule(String ruleTitle, IRuleParams params) {
		title = ruleTitle;

		setRuleParams(params);
	}

	public abstract void setRuleParams(IRuleParams params);

	public abstract IRuleParams getRuleParams();

	public abstract IRuleParamsComponent createGuiComponent();

	public abstract GraphQueryResult getStatements() throws RuleExportException;

	public final void setSource(AbstractSource src) {
		source = src;
	}

	public final AbstractSource getSource() throws RuleExportException {
		return source;
	}


	public final String getTitle() {
		return title;
	}


	public final IRuleParamsComponent getGuiComponent() {
		if (guiComponent == null) {
			guiComponent = createGuiComponent();
		}
		return guiComponent;
	}


	public final void saveParams(XMLStreamWriter writer) throws XMLStreamException {
		getRuleParams().saveXml(writer);
	}


	protected final RepositoryWrapper getRepository() {
		return source.getRepository();
	}

	@Override
	public final String toString() {
		return getTitle();
	}
}
