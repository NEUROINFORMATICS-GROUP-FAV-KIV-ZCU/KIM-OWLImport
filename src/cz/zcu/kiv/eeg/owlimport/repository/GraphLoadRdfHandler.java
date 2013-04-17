package cz.zcu.kiv.eeg.owlimport.repository;

import org.openrdf.model.Graph;
import org.openrdf.model.Statement;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.RDFHandlerBase;

/**
 * RDF graph builder. Handles RDF parser events and creates a graph from loaded statements.
 * @author Jan Smitka <jan@smitka.org>
 */
public class GraphLoadRdfHandler extends RDFHandlerBase {
	/** Graph to be built. */
	private Graph g;


	/**
	 * Creates a new graph builder for the given graph.
	 * @param graph Empty graph.
	 */
	public GraphLoadRdfHandler(Graph graph) {
		g = graph;
	}


	/**
	 * Adds the given RDF statement to the graph.
	 */
	@Override
	public void handleStatement(Statement statement) throws RDFHandlerException {
		g.add(statement);
	}
}
