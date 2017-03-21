package main.process.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONObject;

public class JsonGenerator {

	/**
	 * Generate JSON code for a Wikipedia file
	 * @param title
	 * @param cats
	 * @param text
	 * @param links
	 * @param infoBox
	 * @param redirectpage
	 * @return
	 */
	public JSONObject generateWikiJSON(String title, Vector<String> cats,
			String text, Vector<String> links, String infoBox,
			String redirectpage) {
		Map<String, String> map = new HashMap<String, String>();

		if (title != null)
			map.put("title", title);
		if (text != null)
			map.put("text", text);
		if (infoBox != null)
			map.put("infoBox", infoBox);

		JSONObject json = new JSONObject();
		json.accumulateAll(map);

		if (cats != null && !cats.isEmpty()) {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < cats.size(); i++)
				list.add(cats.get(i));

			json.accumulate("categories", list);
		}

		if (links != null && !links.isEmpty()) {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < links.size(); i++)
				list.add(links.get(i));

			json.accumulate("tags", list);
		}
		return json;
	}
}