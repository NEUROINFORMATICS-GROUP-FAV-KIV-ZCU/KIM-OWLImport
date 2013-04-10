package cz.zcu.kiv.eeg.owlimport.model;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.RuleExportException;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import info.aduna.iteration.CloseableIteration;
import info.aduna.iteration.Iteration;
import org.openrdf.model.Statement;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import java.io.*;
import java.util.Map;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class Exporter {
	private File outputFile;

	private OutputStream outputStream;

	private RDFWriter rdfWriter;


	public Exporter(File file) {
		outputFile = file;
	}


	private ExportException exportException(Throwable cause) {
		return new ExportException("An error occurred while exporting one of the sources. The resulting file might be incomplete.", cause);
	}


	public void openFile() throws ExportException {
		try {
			outputStream = createStream();
			rdfWriter = Rio.createWriter(RDFFormat.N3, outputStream);
			rdfWriter.startRDF();
		} catch (Exception e) {
			throw exportException(e);
		}
	}

	public OutputStream createStream() throws IOException {
		FileOutputStream fileStream = new FileOutputStream(outputFile);
		return new BufferedOutputStream(fileStream);
	}


	public void writeSource(AbstractSource source) throws ExportException {
		for (AbstractRule rule : source.getRules()) {
			try {
				writeRule(rule);
			} catch (RuleExportException|RDFHandlerException e) {
				throw exportException(e);
			}
		}
	}

	public void writeRule(AbstractRule rule) throws RuleExportException, RDFHandlerException {
		writeStatements(rule.getStatements());
	}


	public void writeStatements(Iteration<Statement, ? extends Exception> result) throws RuleExportException, RDFHandlerException {
		try {
			if (result instanceof GraphQueryResult) {
				for (Map.Entry<String, String> nsEntry : ((GraphQueryResult)result).getNamespaces().entrySet()) {
					rdfWriter.handleNamespace(nsEntry.getKey(), nsEntry.getValue());
				}
			}

			while (result.hasNext()) {
				rdfWriter.handleStatement(result.next());
			}

			try {
				if (result instanceof CloseableIteration) {
					((CloseableIteration)result).close();
				}
			} catch (Exception e) {
				// result could not be closed, considered non-fatal
			}
		} catch (Exception e) {
			throw new RuleExportException("Error while exporting rule.", e);
		}
	}



	public void closeFile() throws ExportException {
		try {
			rdfWriter.endRDF();
			outputStream.close();
		} catch (Exception e) {
			throw exportException(e);
		}
	}
}
