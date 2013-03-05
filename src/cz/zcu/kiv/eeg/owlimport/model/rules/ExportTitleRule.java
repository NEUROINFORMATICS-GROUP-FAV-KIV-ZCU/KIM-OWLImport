package cz.zcu.kiv.eeg.owlimport.model.rules;

import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportTitleRule extends AbstractRule {
	public ExportTitleRule(String ruleTitle) {
		super(ruleTitle);
	}

	@Override
	public GraphQueryResult getStatements() {
		try {
			return getRepository().exportInstances(getSource().getBaseUrl(), "http://kiv.zcu.cz/eegbase#title");
		} catch (MalformedQueryException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (RepositoryException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (QueryEvaluationException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return null;
	}
}
