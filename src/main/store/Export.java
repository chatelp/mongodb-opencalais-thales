package main.store;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import main.analyse.Analyser;
import main.process.convert.Converter;
import mx.bigdata.jcalais.CalaisResponse;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class Export {

	/**
	 * Export all collection coll in a file format xml type or json type.
	 * 
	 * @param type
	 * @param path
	 * @param coll
	 * @throws IOException
	 */
	public void exportAll(String type, String path, DBCollection coll)
			throws IOException {
		DBCursor cursor = coll.find();
		FileWriter fw;
		BufferedWriter output;
		String txt = "";

		fw = new FileWriter(path, false);
		output = new BufferedWriter(fw);

		while (cursor.hasNext()) {

			if (type.equalsIgnoreCase("xml")) {
				Converter c = new Converter();
				JSON json = JSONSerializer.toJSON(cursor.next());
				txt = c.JsonToXml(json);
			} else
				txt = cursor.next().toString();

			try {
				output.write(txt);
				output.flush();
				if (!cursor.hasNext())
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		cursor.close();
	}

	/**
	 * Export only the result of the last request
	 * File format: xml 'type' or json 'type'
	 * @param type
	 * @param path
	 * @param coll
	 * @throws IOException
	 */
	public void exportLastRequest(String type, String path, DBCollection coll)
			throws IOException {
		// Lecture
		String line;
		String txt;

		InputStream is = new FileInputStream("misc/config/req_res.txt");
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		FileWriter fw = new FileWriter(path, false);
		BufferedWriter output = new BufferedWriter(fw);

		BasicDBObject query = new BasicDBObject();
		DBCursor cursor;

		while ((line = br.readLine()) != null) {
			query.put("_id", new ObjectId(line));
			cursor = coll.find(query);

			if (type.equalsIgnoreCase("xml")) {
				Converter c = new Converter();
				JSON json = JSONSerializer.toJSON(cursor.next());
				txt = c.JsonToXml(json);
			} else
				txt = cursor.next().toString();

			try {
				output.write(txt);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		br.close();
		output.close();
	}

	/**
	 * Export the result of the last analysis
	 * @param path
	 */
	public void exportAnalyse(String path) {
		FileWriter fw;
		BufferedWriter output;

		try {
			fw = new FileWriter(path, false);
			output = new BufferedWriter(fw);

			for (CalaisResponse cr : Analyser.getInstance().getResults()) {
				output.write(cr.getPayload());
				output.flush();
			}

			output.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
