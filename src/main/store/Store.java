package main.store;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

import main.db.mongoDB.MongoDbConnector;
import main.gui.MessageBoxErrorIcon;
import main.ontology.OwlParser;
import main.process.convert.JsonGenerator;
import net.sf.json.JSONObject;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public class Store {

	/**
	 * Store a wikipedia file in MongoDB.
	 * @param args
	 * @throws IOException
	 */
	public void storeWikipediaFile(String path) throws IOException {
		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(path);

		DB db = MongoDbConnector.getDB();
		final DBCollection collection = db.getCollection("articles");

		try {
			wxsp.setPageCallback(new PageCallbackHandler() {
				public void process(WikiPage page) {
					JSONObject json;
					String title = null;
					String infoBox = null;
					String text = null;
					String redirectPage = null;

					Vector<String> cats = null;
					Vector<String> links = new Vector<String>();

					List<String> s = OwlParser.getInstance().getChosenKeys();

					if (s.contains("title"))
						title = page.getTitle().toString();
					if (s.contains("cats"))
						cats = page.getCategories();
					if (s.contains("tags"))
						links = page.getLinks();
					if (s.contains("infoBox") && page.getInfoBox() != null)
						infoBox = page.getInfoBox().dumpRaw();
					if (s.contains("text"))
						text = page.getWikiText();
					if (s.contains("redirectpage"))
						redirectPage = page.getRedirectPage();

					json = new JsonGenerator().generateWikiJSON(title, cats,
							text, links, infoBox, redirectPage);
					storeJSON(json.toString(), collection);
				}
			});

			wxsp.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Store results of analysis in MongoDB
	 * @param json
	 */
	public void storeResAnalysis(JSONObject json) {
		try {
			DB db = MongoDbConnector.getDB();
			DBCollection dbc = db.getCollection("result.analysis");
			storeJSON(json.toString(), dbc);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void storeJSON(String json, DBCollection collection) {
		DBObject dbObject = (DBObject) JSON.parse(json);
		try {
			collection.insert(dbObject);
		} catch (com.mongodb.MongoInterruptedException e) {
			MessageBoxErrorIcon
					.error("Une exception a été lancée.\nVeuillez redémarrer l'application.");
		}
	}
}
