package cz.zcu.kiv.eeg.owlimport.project;

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
import java.util.List;
import java.util.Map;

/**
 * Exporter executes defined rules for the given list of sources and writes the results to specified file.
 *
 * Result is written in Turtle format.
 * @author Jan Smitka <jan@smitka.org>
 */
public class Exporter {
	/** Output file. */
	private File outputFile;
	/** Output stream. */
	private OutputStream outputStream;
	/** Output RDF writer. */
	private RDFWriter rdfWriter;


	/**
	 * Creates a new exporter, result will be written to the specified file.
	 * @param file Output file.
	 */
	public Exporter(File file) {
		outputFile = file;
	}


	/**
	 * Creates a generic export exception.
	 * @param cause Causing exception.
	 * @return Exception.
	 */
	private ExportException exportException(Throwable cause) {
		return new ExportException("An error occurred while exporting one of the sources. The resulting file might be incomplete.", cause);
	}

	/**
	 * Executes all rules in all given sources and writes the results to the file.
	 * @param sources
	 * @throws ExportException
	 */
	public void writeSources(List<AbstractSource> sources) throws ExportException {
		openFile();
		for (AbstractSource source : sources) {
			writeSource(source);
		}
		closeFile();
	}

	/**
	 * Opens the output file and initializes the RDF writer.
	 * @throws ExportException
	 */
	private void openFile() throws ExportException {
		try {
			outputStream = createStream();
			rdfWriter = Rio.createWriter(RDFFormat.TURTLE, outputStream);
			rdfWriter.startRDF();
		} catch (Exception e) {
			throw exportException(e);
		}
	}

	/**
	 * Creates the output stream.
	 * @return Output stream.
	 * @throws IOException when the output file could not be opened for writing.
	 */
	private OutputStream createStream() throws IOException {
		FileOutputStream fileStream = new FileOutputStream(outputFile);
		return new BufferedOutputStream(fileStream);
	}

	/**
	 * Exports the given source and writes the results to the output file.
	 * @param source Source to be exported.
	 * @throws ExportException when the source could not be exported.
	 */
	private void writeSource(AbstractSource source) throws ExportException {
		for (AbstractRule rule : source.getRules()) {
			try {
				writeRule(rule);
			} catch (RuleExportException|RDFHandlerException e) {
				throw exportException(e);
			}
		}
	}

	/**
	 * Executes the given rule and writes the resulting statements.
	 * @param rule
	 * @throws RuleExportException
	 * @throws RDFHandlerException
	 */
	private void writeRule(AbstractRule rule) throws RuleExportException, RDFHandlerException {
		Iteration<Statement, ? extends Exception> statements = rule.getStatements();
		if (statements != null) {
			writeStatements(statements);
		}
	}


	/**
	 * Writes the list of statements to the output file.
	 * @param result Result of rule export - iteration of statements.
	 * @throws RuleExportException when the rule could not be exported to the output file.
	 * @throws RDFHandlerException when the statements could not be written to the output file.
	 */
	private void writeStatements(Iteration<Statement, ? extends Exception> result) throws RuleExportException, RDFHandlerException {
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


	/**
	 * Closes the output file.
	 * @throws ExportException
	 */
	private void closeFile() throws ExportException {
		try {
			rdfWriter.endRDF();
			outputStream.close();
		} catch (Exception e) {
			throw exportException(e);
		}
	}
}
