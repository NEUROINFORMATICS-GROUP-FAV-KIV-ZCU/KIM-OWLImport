package cz.zcu.kiv.eeg.owlimport.project;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.List;

/**
 * ProjectWriter writes the definition of given list of sources and their rules to the XML file.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ProjectWriter {
	/** Output file. */
	private File file;

	/**
	 * Creates project writer for the given output file.
	 * @param outputFile Output XML file.
	 */
	public ProjectWriter(File outputFile) {
		file = outputFile;
	}


	/**
	 * Saves the given list of sources to the output file.
	 * @param sources List of ontology sources.
	 * @throws ProjectWriteException when the project cannot be written.
	 */
	public void save(List<AbstractSource> sources) throws ProjectWriteException {
		try {
			OutputStream out = openFile();

			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = factory.createXMLStreamWriter(out);

			writer.writeStartDocument();
			writeSources(writer, sources);
			writer.writeEndDocument();
			writer.close();
			out.close();
		} catch (IOException e) {
			throw new ProjectWriteException("Error while opening output file.", e);
		} catch (XMLStreamException e) {
			throw new ProjectWriteException("Error while writing project XML file.", e);
		}
	}

	/**
	 * Opens the output file.
	 * @return Output stream.
	 * @throws FileNotFoundException when the file cannot be opened for writing.
	 */
	private OutputStream openFile() throws FileNotFoundException {
		FileOutputStream fs = new FileOutputStream(file);
		BufferedOutputStream bfs = new BufferedOutputStream(fs);
		return bfs;
	}

	/**
	 * Writes the list of sources to the output file.
	 * @param out XML writer.
	 * @param sources List of sources.
	 * @throws XMLStreamException when the file cannot be written.
	 */
	private void writeSources(XMLStreamWriter out, List<AbstractSource> sources) throws XMLStreamException {
		out.writeStartElement(Elements.SOURCES);
		for (AbstractSource source : sources) {
			writeSource(out, source);
		}
		out.writeEndElement();
	}

	/**
	 * Writes out the given source.
	 * @param out XML writer.
	 * @param source Source to be written.
	 * @throws XMLStreamException when the file cannot be written.
	 */
	private void writeSource(XMLStreamWriter out, AbstractSource source) throws XMLStreamException {
		out.writeStartElement(Elements.SOURCE);
		out.writeAttribute(Attributes.SOURCE_TYPE, source.getClass().getName());
		writeSourceTitle(out, source);
		writeBaseUrl(out, source);
		writeLocation(out, source);
		writeRules(out, source);
		out.writeEndElement();
	}

	/**
	 * Writes out the source title.
	 * @param out XML writer.
	 * @param source Source.
	 * @throws XMLStreamException when the file cannot be written.
	 */
	private void writeSourceTitle(XMLStreamWriter out, AbstractSource source) throws XMLStreamException {
		out.writeStartElement(Elements.SOURCE_TITLE);
		out.writeCharacters(source.getTitle());
		out.writeEndElement();
	}

	/**
	 * Writes out the source base URI.
	 * @param out XML writer.
	 * @param source Source.
	 * @throws XMLStreamException when the file cannot be written.
	 */
	private void writeBaseUrl(XMLStreamWriter out, AbstractSource source) throws XMLStreamException {
		out.writeStartElement(Elements.SOURCE_BASE_URI);
		out.writeCharacters(source.getBaseUrl());
		out.writeEndElement();
	}

	/**
	 * Write source location parameters.
	 * @param out XML writer.
	 * @param source Source.
	 * @throws XMLStreamException when the file cannot be written.
	 */
	private void writeLocation(XMLStreamWriter out, AbstractSource source) throws XMLStreamException {
		out.writeStartElement(Elements.SOURCE_LOCATION);
		source.saveLocation(out);
		out.writeEndElement();
	}

	/**
	 * Write out rules for the given source.
	 * @param out XML writer.
	 * @param source Source.
	 * @throws XMLStreamException when the file cannot be written.
	 */
	private void writeRules(XMLStreamWriter out, AbstractSource source) throws XMLStreamException {
		out.writeStartElement(Elements.RULES);
		for (AbstractRule rule : source.getRules()) {
			writeRule(out, rule);
		}
		out.writeEndElement();
	}

	/**
	 * Write out single rule.
	 * @param out XML writer.
	 * @param rule Rule to be written.
	 * @throws XMLStreamException when the file cannot be written.
	 */
	private void writeRule(XMLStreamWriter out, AbstractRule rule) throws XMLStreamException {
		out.writeStartElement(Elements.RULE);
		out.writeAttribute(Attributes.RULE_TYPE, rule.getClass().getName());
		writeRuleTitle(out, rule);
		writeRuleParams(out, rule);
		out.writeEndElement();
	}

	/**
	 * Write out rule title.
	 * @param out XML writer.
	 * @param rule Rule.
	 * @throws XMLStreamException when the file cannot be written.
	 */
	private void writeRuleTitle(XMLStreamWriter out, AbstractRule rule) throws XMLStreamException {
		out.writeStartElement(Elements.RULE_TITLE);
		out.writeCharacters(rule.getTitle());
		out.writeEndElement();
	}

	/**
	 * Write out rule params.
	 * @param out XML writer.
	 * @param rule Rule.
	 * @throws XMLStreamException when the file cannot be written.
	 */
	private void writeRuleParams(XMLStreamWriter out, AbstractRule rule) throws XMLStreamException {
		out.writeStartElement(Elements.RULE_PARAMS);
		rule.saveParams(out);
		out.writeEndElement();
	}

}
