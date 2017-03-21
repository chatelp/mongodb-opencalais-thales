package main.analyse;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Configuration writing an xml document
 * Used for proxy parameters
 *
 */
public class ConfigUpdater {
	private Document doc = null;

	public ConfigUpdater(String host, int port, boolean useProxy) {
		try {
			doc = parserXML(new File("misc/config/config.xml"));
			updateProxy(host, port, useProxy, doc, 0);
			transform("misc/config/config.xml");

		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	public void updateProxy(String host, int port, Boolean useProxy, Node node, int level) {
		NodeList nl = node.getChildNodes();

		for (int i = 0, cnt = nl.getLength(); i < cnt; i++) {
			Node n = nl.item(i);

			if (n.getNodeName().contentEquals("proxy")) {
				NodeList proxyNl = n.getChildNodes();
				for (int j = 0, c = proxyNl.getLength(); j < c; j++) {
					Node n2 = proxyNl.item(j);

					if (n2.getNodeName().contentEquals("useProxy")) {
						n2.setTextContent(useProxy.toString());
					}
					if (n2.getNodeName().contentEquals("host")) {
						n2.setTextContent(host);
					}
					if (n2.getNodeName().contentEquals("port")) {
						n2.setTextContent(Integer.toString(port));
					}
				}
			}

			updateProxy(host, port, useProxy, nl.item(i), level + 1);
		}
	}

	public void transform(String filepath) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);
	}

	public Document parserXML(File file) throws SAXException, IOException,
			ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(file);
	}
}
