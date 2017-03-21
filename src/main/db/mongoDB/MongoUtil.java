package main.db.mongoDB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class MongoUtil {

	/**
	 * 
	 * @param coll
	 * @param begin
	 * @param end
	 * @return List of all keys (from index 'begin' to index 'end') in the
	 *         collection coll between
	 * @throws JSONException
	 */
	public List<String> getKeys(DBCollection coll, int begin, int end)
			throws JSONException {
		ArrayList<String> keys = new ArrayList<String>();
		DBCursor cursor = coll.find();

		for (int i = begin; i < end; i++) {
			if (cursor.hasNext()) {
				String json = cursor.next().toString();
				JSONObject object = new JSONObject(json);
				@SuppressWarnings("unchecked")
				Iterator<String> currKeys = object.keys();

				while (currKeys.hasNext()) {
					String key = currKeys.next();
					if (!keys.contains(key))
						keys.add(key);
				}
			}
		}
		cursor.close();
		return keys;
	}

	/**
	 * 
	 * @param coll
	 * @param begin
	 * @param end
	 * @return List of all documents (from index 'begin' to index 'end') in the
	 *         collection coll between
	 * @throws JSONException
	 */
	public List<JSONObject> getDocs(DBCollection coll, int begin, int end)
			throws JSONException {
		ArrayList<JSONObject> docs = new ArrayList<JSONObject>();
		DBCursor cursor = coll.find();

		cursor.skip(begin);
		for (int i = begin; i < end; i++) {
			if (!cursor.hasNext())
				break;

			String json = cursor.next().toString();
			JSONObject doc = new JSONObject(json);
			docs.add(doc);

		}
		cursor.close();

		return docs;
	}

}
