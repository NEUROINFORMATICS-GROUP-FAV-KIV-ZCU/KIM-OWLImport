package cz.zcu.kiv.eeg.owlimport;

import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import java.io.OutputStream;
import java.util.Map;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ResultWriter {
	public void exportStatements(GraphQueryResult statements, OutputStream stream) throws QueryEvaluationException, RDFHandlerException {
		RDFWriter writer = Rio.createWriter(RDFFormat.N3, stream);

		writer.startRDF();

		for (Map.Entry<String, String> nsEntry : statements.getNamespaces().entrySet()) {
			writer.handleNamespace(nsEntry.getKey(), nsEntry.getValue());
		}

		while (statements.hasNext()) {
			writer.handleStatement(statements.next());
		}
		statements.close();

		writer.endRDF();

	}
}
