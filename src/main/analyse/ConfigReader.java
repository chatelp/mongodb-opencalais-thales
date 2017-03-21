package main.analyse;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Configuration reading an xml document
 * Used for proxy parameters
 *
 */
public class ConfigReader {
	private static volatile ConfigReader instance = null;

	public final static ConfigReader getInstance() {
		ConfigReader.instance = new ConfigReader();
		return ConfigReader.instance;
	}

	/**
	 * Attributes
	 */
	private Document doc = null;
	private Boolean useProxy;
	private String host = null;
	private int port;

	/**
	 * Getters and Setters
	 */
	public Boolean getUseProxy() {
		return useProxy;
	}

	public void setUseProxy(Boolean useProxy) {
		this.useProxy = useProxy;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ConfigReader() {
		try {
			doc = parserXML(new File("misc/config/config.xml"));
			visit(doc, 0);

		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	public void visit(Node node, int level) {
		NodeList nl = node.getChildNodes();

		for (int i = 0, cnt = nl.getLength(); i < cnt; i++) {
			Node n = nl.item(i);

			if (n.getNodeName().contentEquals("proxy")) {
				NodeList proxyNl = n.getChildNodes();
				for (int j = 0, c = proxyNl.getLength(); j < c; j++) {
					Node n2 = proxyNl.item(j);

					if (n2.getNodeName().contentEquals("useProxy")) {
						setUseProxy(Boolean.valueOf(n2.getTextContent()));
					}
					if (n2.getNodeName().contentEquals("host")) {
						setHost(n2.getTextContent());
					}
					if (n2.getNodeName().contentEquals("port")) {
						setPort(Integer.parseInt(n2.getTextContent()));
					}
				}
			}

			visit(nl.item(i), level + 1);
		}
	}

	public Document parserXML(File file) throws SAXException, IOException,
			ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(file);
	}
}
