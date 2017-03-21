package main.gui.queries;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.gui.Window;
import main.jongo.mapping.RuntimeClassCreator;

import org.jongo.Find;

public class GuiQuery {

	private static volatile GuiQuery instance = null;
	private String request = "";

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public void resetRequest() {
		this.request = "{}";
		Window.getLblQuery()
				.setText(
						"Aucune requ\u00EAte ne filtre actuellement les documents de la base de donn\u00E9es.");
	}

	private GuiQuery() {
		super();
		this.request = "{}";
	}

	public final static GuiQuery getInstance() {
		if (GuiQuery.instance == null) {
			synchronized (GuiQuery.class) {
				if (GuiQuery.instance == null) {
					GuiQuery.instance = new GuiQuery();
				}
			}
		}
		return GuiQuery.instance;
	}

	/**
	 * MongoDB query creation.
	 * fields is the list of selected fields by user
	 * @param fields
	 * @return the query
	 */
	public String createQuery(List<Field> fields) {
		boolean or = getOr(fields);
		fields = trim(fields);

		String res = "{";
		if (or) {
			res += "$or: [";
		}

		for (int i = 0; i < fields.size(); i++) {

			Field f = fields.get(i);

			if (i != fields.size() && i != 0) {
				res += ", ";
			}

			if (or)
				res += "{";

			if (f.getOperator().contentEquals("contient"))
				res += f.getKey() + ": {$regex : '" + f.getFilter()
						+ "', $options: 'i'}";
			if (f.getOperator().contentEquals("="))
				res += f.getKey() + ":'" + f.getFilter() + "'";

			if (or)
				res += "}";

		}

		if (or)
			res += "]";

		res += "}";

		return res;
	}

	private Boolean getOr(List<Field> fields) {
		return fields.get(0).getAndOr().contentEquals("OU");
	}


	/**
	 * Delete empty fields
	 * @param f
	 * @return
	 */
	private List<Field> trim(List<Field> f) {
		List<Field> res = new ArrayList<Field>();

		for (Field field : f) {
			if (!(field.getFilter().contentEquals("") || field.getOperator()
					.contentEquals(""))) {
				res.add(field);
			}
		}

		return res;
	}

	/**
	 * Save all articles ids in a document located at misc/config/req_res.txt
	 * @param articles
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void saveQueryResult(Find articles) throws IllegalArgumentException,
			IllegalAccessException {

		List<String> l = new ArrayList<String>();
		Class<?> c = RuntimeClassCreator.createClass();
		Iterable<?> it = articles.as(c);

		for (Object a : it) {

			try {
				java.lang.reflect.Field attribut = a.getClass().getField("_id");
				Object valeur = attribut.get(a);
				if (valeur != null)
					l.add(attribut.get(a).toString());
			} catch (NoSuchFieldException e) {
			}
		}

		try {
			saveRequestRes(l);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void saveRequestRes(List<String> l) throws IOException {
		FileWriter fw = new FileWriter("misc/config/req_res.txt");
		BufferedWriter output = new BufferedWriter(fw);

		for (String s : l) {
			output.write(s);
			output.newLine();
			output.flush();
		}

		output.close();
	}
}
