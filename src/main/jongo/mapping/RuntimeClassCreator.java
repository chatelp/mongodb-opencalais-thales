package main.jongo.mapping;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import main.gui.overview.OverviewState;

public class RuntimeClassCreator {

	/**
	 * Create classes at runtime.
	 * This function create class representing mongoDB collections
	 * Jongo library use these classes to generate queries
	 * @param args
	 * @throws CannotCompileException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static Class<?> createClass() {
		// TODO Auto-generated method stub
		ClassPool pool = ClassPool.getDefault();
		CtClass runtimeClass = pool.makeClass("RuntimeClass"
				+ System.currentTimeMillis() + System.nanoTime());

		try {
			// System.index
			if (OverviewState.getInstance().getCurrentCollection().getName()
					.equalsIgnoreCase("system.indexes")) {
				runtimeClass.addField(CtField.make("public Integer v;",
						runtimeClass));
				runtimeClass.addField(CtField.make("public String ns;",
						runtimeClass));
				runtimeClass.addField(CtField.make("public String name;",
						runtimeClass));
				runtimeClass.addField(CtField.make(
						"public com.mongodb.BasicDBObject key;", runtimeClass));
			}
			// OpenClais entities
			// http://www.opencalais.com/documentation/
			// calais-web-service-api/calais-semantic-metadata-french/entities-index-and-definitions-
			else if (OverviewState.getInstance().getCurrentCollection()
					.getName().equalsIgnoreCase("result.analysis")) {
				String entities[] = new String[] { "City", "Company",
						"Continent", "Country", "Currency", "EmailAddress",
						"Facility", "FaxNumber", "Holiday", "IndustryTerm",
						"MarketIndex", "NaturalFeature", "OperatingSystem",
						"Organization", "Person", "PhoneNumber", "Position",
						"ProgrammingLanguage", "PublishedMedium",
						"ProvinceOrState", "Region", "Thechnology", "URL" };

				for (int i = 0; i < entities.length - 1; i++)
					runtimeClass.addField(CtField.make("public java.util.List "
							+ entities[i] + ";", runtimeClass));

				runtimeClass.addField(CtField.make(
						"public org.bson.types.ObjectId _id;", runtimeClass));
				runtimeClass.addField(CtField.make(
						"public java.util.List TitleOfAnalysedDocuments;",
						runtimeClass));
			} else {
				// Dump results
				runtimeClass.addField(CtField.make(
						"public org.bson.types.ObjectId _id;", runtimeClass));
				runtimeClass.addField(CtField.make("public String text;",
						runtimeClass));
				runtimeClass.addField(CtField.make("public String title;",
						runtimeClass));
				runtimeClass.addField(CtField.make(
						"public java.util.List tags;", runtimeClass));
				runtimeClass.addField(CtField.make("public String infoBox;",
						runtimeClass));
			}

		} catch (CannotCompileException e) {
			e.printStackTrace();
		}

		try {
			return runtimeClass.toClass();
		} catch (CannotCompileException e) {
			e.printStackTrace();
			return null;
		}
	}

}
