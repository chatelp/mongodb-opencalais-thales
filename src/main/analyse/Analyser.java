package main.analyse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.gui.overview.OverviewState;
import main.util.Pair;
import mx.bigdata.jcalais.CalaisClient;
import mx.bigdata.jcalais.CalaisObject;
import mx.bigdata.jcalais.CalaisResponse;
import mx.bigdata.jcalais.rest.CalaisRestClient;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

/**
 * Singleton Analyser
 * 
 */
public class Analyser {
	private static volatile Analyser instance = null;
	private HashMap<String, List<Pair<String, Double>>> hashMap = null;
	private List<CalaisResponse> results = null;

	/**
	 * Getters and setters
	 * 
	 */
	public List<CalaisResponse> getResults() {
		return results;
	}

	public HashMap<String, List<Pair<String, Double>>> getHashMap() {
		return hashMap;
	}

	private Analyser() {
		super();
		results = new ArrayList<CalaisResponse>();
	}

	public final static Analyser getInstance() {
		if (Analyser.instance == null) {
			synchronized (Analyser.class) {
				if (Analyser.instance == null) {
					Analyser.instance = new Analyser();
				}
			}
		}
		return Analyser.instance;
	}

	/**
	 * Send a file f to OpenCalais server and download a reply stored in a
	 * hashmap h. Is f is too large (>100Ko) then f is split and all files are
	 * sent one by one to OpenCalais server.
	 * 
	 * @param f
	 * @throws IOException
	 */
	public void analyse(File f) throws IOException {
		ConfigReader cr = new ConfigReader();
		System.setProperty("http.proxyHost", cr.getHost());
		System.setProperty("http.proxyPort", String.valueOf(cr.getPort()));

		CalaisClient client = new CalaisRestClient("4btnnw9bw24b3xhqrd4gzz72");

		deleteFiles("misc/files/split/");
		splitFile(f, "misc/files/split/");

		// Parcourir tous les fichiers du répertoire
		File dir = new File("misc/files/split");
		File[] files = dir.listFiles();

		//
		// "Key1" -> [ <"Name1", relevance> ; <"Name2", relevance> ; ... ]
		// "Key2" -> [ <"Name1", relevance> ; <"Name2", relevance> ; ... ]
		// "Key3" -> [ <"Name1", relevance> ; <"Name2", relevance> ; ... ]
		//
		HashMap<String, List<Pair<String, Double>>> h = new HashMap<String, List<Pair<String, Double>>>();

		for (int i = 0; i < files.length; i++) {
			InputStream is = new FileInputStream(files[i]);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			CalaisResponse response = client.analyze(br);
			results.add(response);

			for (CalaisObject entity : response.getEntities()) {
				Double relevance = 0.0;
				String type = "";
				String name = "";

				if (entity.getField("_type") != null)
					type = entity.getField("_type");
				if (entity.getField("name") != null)
					name = entity.getField("name");
				if ((entity.getField("relevance") != null)
						&& (Double.valueOf(entity.getField("relevance")) != null))
					relevance = Double.valueOf(entity.getField("relevance"));

				Pair<String, Double> pair = new Pair<String, Double>(name,
						relevance);
				stockPair(pair, type, h);
			}
		}
		hashMap = h;
	}

	/**
	 * Retrieves documents related to the analysis then seeks the title
	 * documents.
	 * 
	 * @return List of title documents that are analysed
	 */
	public List<String> getTitlesAnalysed() {
		// Lecture
		String line;
		List<String> l = new ArrayList<String>();

		InputStream is;
		try {
			is = new FileInputStream("misc/config/req_res.txt");

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			BasicDBObject query = new BasicDBObject();
			DBCursor cursor;

			while ((line = br.readLine()) != null) {
				query.put("_id", new ObjectId(line));
				cursor = OverviewState.getInstance().getCurrentCollection()
						.find(query);

				l.add(cursor.next().get("title").toString());
			}

			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}

	private void stockPair(Pair<String, Double> p, String key,
			HashMap<String, List<Pair<String, Double>>> map) {
		List<Pair<String, Double>> list = map.get(key);
		if (list == null) {
			list = new ArrayList<Pair<String, Double>>();
			map.put(key, list);
		}

		// Avoid duplication
		boolean b = false;
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).getFirst().equals(p.getFirst()))
				b = true;

		if (!b)
			list.add(p);

	}

	private void splitFile(File f, String path) throws IOException {
		FileInputStream fis = new FileInputStream(f);
		int size = 99 * 1000;
		byte buffer[] = new byte[size];

		int count = 0;
		while (true) {
			int i = fis.read(buffer, 0, size);
			if (i == -1)
				break;

			String filename = f.getName() + count;
			FileOutputStream fos = new FileOutputStream(path + filename);
			fos.write(buffer, 0, i);
			fos.flush();
			fos.close();

			++count;
		}
		fis.close();
	}

	private void deleteFiles(String path) {
		File dir = new File(path);
		File[] files = dir.listFiles();

		for (int i = 0; i < files.length; i++) {
			// A File object to represent the filename
			String fileName = files[i].getName();
			File f = files[i];

			// Make sure the file or directory exists and isn't write protected
			if (!f.exists())
				throw new IllegalArgumentException(
						"Delete: no such file or directory: " + fileName);

			if (!f.canWrite())
				throw new IllegalArgumentException("Delete: write protected: "
						+ fileName);

			f.delete();
		}
	}
}
