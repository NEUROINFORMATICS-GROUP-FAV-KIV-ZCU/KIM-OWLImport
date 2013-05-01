package cz.zcu.kiv.eeg.owlimport.project;

import cz.zcu.kiv.eeg.owlimport.model.RuleManager;
import cz.zcu.kiv.eeg.owlimport.model.SourceManager;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleFactory;
import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleParams;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceFactory;
import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceParams;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * ProjectReader reads the sources and its configuration from given XML file and creates list of sources with rules.
 *
 * Uses XMLStream reader for possible embedding of ontology files.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ProjectReader {
	/** Source manager used to create sources. */
	private SourceManager sourceManager;
	/** Rule manager used to create rules. */
	private RuleManager ruleManager;
	/** Input file. */
	private File file;

	/**
	 * Initializes new project reader for the given file. Source and Rule managers are used to fetch factories for
	 * new object creation.
	 * @param srcManager Source manager.
	 * @param rlManager Rule manager.
	 * @param inputFile Input file.
	 */
	public ProjectReader(SourceManager srcManager, RuleManager rlManager, File inputFile) {
		sourceManager = srcManager;
		ruleManager = rlManager;
		file = inputFile;
	}


	/**
	 * Loads the input file and creates list of sources.
	 * @return List of sources.
	 * @throws ProjectReadException when the project cannot be read.
	 */
	public List<AbstractSource> load() throws ProjectReadException {
		List<AbstractSource> sources = new LinkedList<>();
		try {
			InputStream in = openFile();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader reader = factory.createXMLStreamReader(in);

			reader.nextTag();
			reader.require(XMLStreamConstants.START_ELEMENT, null, Elements.SOURCES);
			while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
				sources.add(loadSource(reader));
			}

			reader.require(XMLStreamConstants.END_ELEMENT, null, Elements.SOURCES);

			in.close();
			return sources;
		} catch (IOException e) {
			throw new ProjectReadException("Error while reading project file.", e);
		} catch (XMLStreamException e) {
			throw new ProjectReadException("Error while reading XML data.", e);
		}
	}


	/**
	 * Opens the input file.
	 * @return Input stream.
	 * @throws FileNotFoundException when the input file cannot be found.
	 */
	private InputStream openFile() throws FileNotFoundException {
		FileInputStream fs = new FileInputStream(file);
		BufferedInputStream bfs = new BufferedInputStream(fs);
		return bfs;
	}


	/** Current source title. */
	private String sourceTitle;
	/** Current source base URI. */
	private String sourceBaseUri;
	/** Current source params. */
	private ISourceParams sourceParams;
	/** Current source rules. */
	private List<AbstractRule> sourceRules;

	/**
	 * Loads the source element and creates the new source object.
	 * @param reader XML reader.
	 * @return Created source object.
	 * @throws XMLStreamException when the XML cannot be read or parsed.
	 */
	private AbstractSource loadSource(XMLStreamReader reader) throws XMLStreamException {
		reader.require(XMLStreamConstants.START_ELEMENT, null, Elements.SOURCE);
		String type = reader.getAttributeValue(null, Attributes.SOURCE_TYPE);
		ISourceFactory factory = sourceManager.getFactoryFor(type);
		sourceTitle = sourceBaseUri = null;
		sourceParams = null;
		sourceRules = new LinkedList<>();
		while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
			loadSourceMember(reader, factory);
		}

		AbstractSource source = factory.createSource(sourceTitle, sourceBaseUri, sourceParams);
		for (AbstractRule rule : sourceRules) {
			source.addRule(rule);
		}

		reader.nextTag();
		reader.require(XMLStreamConstants.END_ELEMENT, null, Elements.SOURCE);

		return source;
	}

	/**
	 * Load source child element.
	 * @param reader XML reader.
	 * @param factory Source factory.
	 * @throws XMLStreamException when the XML cannot be read or parsed.
	 */
	private void loadSourceMember(XMLStreamReader reader, ISourceFactory factory) throws XMLStreamException {
		String elName = reader.getLocalName();
		if (elName.equals(Elements.SOURCE_TITLE)) {
			sourceTitle = XmlReaderUtils.loadElementText(reader);
		} else if (elName.equals(Elements.SOURCE_BASE_URI)) {
			sourceBaseUri = XmlReaderUtils.loadElementText(reader);
		} else if (elName.equals(Elements.SOURCE_LOCATION)) {
			sourceParams = factory.loadParams(reader);
		} else if (elName.equals(Elements.RULES)) {
			loadSourceRules(reader);
		} else {
			throw XmlReaderUtils.unexpectedElementException(reader);
		}
	}

	/** Title of the currently processed rule. */
	private String ruleTitle;
	/** Params of the currently processed rule. */
	private IRuleParams ruleParams;

	/**
	 * Loads source rules.
	 * @param reader XML reader.
	 * @throws XMLStreamException when the XML cannot be read or parsed.
	 */
	private void loadSourceRules(XMLStreamReader reader) throws XMLStreamException {
		while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
			reader.require(XMLStreamConstants.START_ELEMENT, null, Elements.RULE);
			String type = reader.getAttributeValue(null, Attributes.RULE_TYPE);
			IRuleFactory factory = ruleManager.getFactoryFor(type);
			ruleTitle = null;
			ruleParams = null;
			while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
				loadRuleMember(reader, factory);
			}

			AbstractRule rule = factory.createRule(ruleTitle);
			rule.setRuleParams(ruleParams);
			sourceRules.add(rule);
		}
	}

	/**
	 * Reads the rule child element.
	 * @param reader XML reader.
	 * @param factory Rule factory.
	 * @throws XMLStreamException when the XML cannot be read or parsed.
	 */
	private void loadRuleMember(XMLStreamReader reader, IRuleFactory factory) throws XMLStreamException {
		String elName = reader.getLocalName();
		if (elName.equals(Elements.RULE_TITLE)) {
			ruleTitle = XmlReaderUtils.loadElementText(reader);
		} else if (elName.equals(Elements.RULE_PARAMS)) {
			ruleParams = factory.loadParams(reader);
		} else {
			throw XmlReaderUtils.unexpectedElementException(reader);
		}
	}


}
