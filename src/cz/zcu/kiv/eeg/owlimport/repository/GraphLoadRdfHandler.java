package cz.zcu.kiv.eeg.owlimport.repository;

import org.openrdf.model.Graph;
import org.openrdf.model.Statement;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.RDFHandlerBase;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class GraphLoadRdfHandler extends RDFHandlerBase {
	private Graph g;


	public GraphLoadRdfHandler(Graph graph) {
		g = graph;
	}


	@Override
	public void handleStatement(Statement statement) throws RDFHandlerException {
		g.add(statement);
	}
}
