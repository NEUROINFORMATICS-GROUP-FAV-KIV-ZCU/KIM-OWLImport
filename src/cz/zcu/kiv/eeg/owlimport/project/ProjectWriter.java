package cz.zcu.kiv.eeg.owlimport.project;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.List;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ProjectWriter {


	private File file;

	public ProjectWriter(File outputFile) {
		file = outputFile;
	}


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

	private OutputStream openFile() throws FileNotFoundException {
		FileOutputStream fs = new FileOutputStream(file);
		BufferedOutputStream bfs = new BufferedOutputStream(fs);
		return bfs;
	}

	private void writeSources(XMLStreamWriter out, List<AbstractSource> sources) throws XMLStreamException {
		for (AbstractSource source : sources) {
			writeSource(out, source);
		}
	}

	private void writeSource(XMLStreamWriter out, AbstractSource source) throws XMLStreamException {
		out.writeStartElement(Elements.SOURCE);
		out.writeAttribute(Attributes.SOURCE_TYPE, source.getClass().getName());
		writeSourceTitle(out, source);
		writeBaseUrl(out, source);
		writeLocation(out, source);
		writeRules(out, source);
		out.writeEndElement();
	}

	private void writeSourceTitle(XMLStreamWriter out, AbstractSource source) throws XMLStreamException {
		out.writeStartElement(Elements.SOURCE_TITLE);
		out.writeCharacters(source.getTitle());
		out.writeEndElement();
	}

	private void writeBaseUrl(XMLStreamWriter out, AbstractSource source) throws XMLStreamException {
		out.writeStartElement(Elements.SOURCE_BASE_URI);
		out.writeCharacters(source.getBaseUrl());
		out.writeEndElement();
	}


	private void writeLocation(XMLStreamWriter out, AbstractSource source) throws XMLStreamException {
		out.writeStartElement(Elements.SOURCE_LOCATION);
		source.saveLocation(out);
		out.writeEndElement();
	}


	private void writeRules(XMLStreamWriter out, AbstractSource source) throws XMLStreamException {
		out.writeStartElement(Elements.RULES);
		for (AbstractRule rule : source.getRules()) {
			writeRule(out, rule);
		}
		out.writeEndElement();
	}

	private void writeRule(XMLStreamWriter out, AbstractRule rule) throws XMLStreamException {
		out.writeStartElement(Elements.RULE);
		out.writeAttribute(Attributes.RULE_TYPE, rule.getClass().getName());
		writeRuleTitle(out, rule);
		writeRuleParams(out, rule);
		out.writeEndElement();
	}

	private void writeRuleTitle(XMLStreamWriter out, AbstractRule rule) throws XMLStreamException {
		out.writeStartElement(Elements.RULE_TITLE);
		out.writeCharacters(rule.getTitle());
		out.writeEndElement();
	}

	private void writeRuleParams(XMLStreamWriter out, AbstractRule rule) throws XMLStreamException {
		out.writeStartElement(Elements.RULE_PARAMS);
		rule.saveParams(out);
		out.writeEndElement();
	}

}
