package main.ontology;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

public class OwlParser {

	private static volatile OwlParser instance = null;
	private List<String> chosenKeys; // Keys selected by user for the filtering
										// of the dump

	public List<String> getChosenKeys() {
		return chosenKeys;
	}

	public void setChosenKeys(List<String> chosenKeys) {
		this.chosenKeys = chosenKeys;
	}

	private OwlParser() {
		super();
	}

	public final static OwlParser getInstance() {
		if (OwlParser.instance == null) {
			synchronized (OwlParser.class) {
				if (OwlParser.instance == null) {
					OwlParser.instance = new OwlParser();
				}
			}
		}
		return OwlParser.instance;
	}

	/**
	 * Constants
	 */

	public static String BASE = "http://www.owl-ontologies.com/Onto.owl";
	public static String NS = BASE + "#";

	/**
	 * External signature methods
	 */

	public List<String> findKeysFromDocument(String documentType, OntClass doc) {
		List<String> keys = new ArrayList<String>();

		for (Iterator<OntClass> supers = doc.listSuperClasses(); supers
				.hasNext();) {
			keys.add(findRestriction(supers.next()));
		}

		return keys;
	}

	public List<String> findLabelKeysFromDocument(String documentType,
			String lang) {
		OntModel m = getFilterOntology();
		OntClass doc = m.getOntClass(NS + documentType);

		List<String> keys = findKeysFromDocument(documentType, doc);
		List<String> labelKeys = new ArrayList<String>();
		for (String key : keys) {
			if (key != "") {
				OntClass oc = m.getOntClass(NS + key);
				labelKeys.add(oc.getLabel(lang));
			}
		}

		return labelKeys;
	}

	public String findTypeFromKey(String key) {
		OntModel m = getFilterOntology();
		OntClass doc = m.getOntClass(NS + key);
		String type = null;

		if (doc != null)
			for (Iterator<OntClass> supers = doc.listSuperClasses(); supers
					.hasNext();) {
				type = findRestriction(supers.next());
			}

		return type;
	}

	public String findClassFromLabelKey(String labelKey, String lang) {
		OntModel m = getFilterOntology();
		OntClass doc = m.getOntClass(NS + "Key");
		String classKey = null;

		for (Iterator<OntClass> supers = doc.listSubClasses(); supers.hasNext();) {
			OntClass oc = supers.next();
			if (oc.getLabel(lang).equalsIgnoreCase(labelKey))
				return oc.getLocalName();
		}

		return classKey;
	}

	/**
	 * Internal implementation methods
	 * 
	 * @throws FileNotFoundException
	 */

	protected OntModel getFilterOntology() {
		OntModel ontologyModel = ModelFactory.createOntologyModel(
				OntModelSpec.OWL_MEM, null);
		InputStream is;
		try {
			is = new FileInputStream("misc/files/ontology/onto.owl");
			ontologyModel.read(is, "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ontologyModel;
	}

	protected String findRestriction(OntClass sup) {
		if (sup.isRestriction()) {
			String out = String.format("%s", renderConstraint(sup
					.asRestriction().asSomeValuesFromRestriction()
					.getSomeValuesFrom()));
			if (renderURI(sup.asRestriction().getOnProperty()).toString()
					.equalsIgnoreCase(":hasKey"))
				return out.split(":")[1];
			if (renderURI(sup.asRestriction().getOnProperty()).toString()
					.equalsIgnoreCase(":hasType"))
				return out.split(":")[1];
		}

		return "";
	}

	protected Object renderConstraint(Resource constraint) {
		if (constraint.canAs(UnionClass.class)) {
			UnionClass uc = constraint.as(UnionClass.class);
			String r = "union{ ";
			for (Iterator<? extends OntClass> i = uc.listOperands(); i
					.hasNext();) {
				r = r + " " + renderURI(i.next());
			}
			return r + "}";
		} else {
			return renderURI(constraint);
		}
	}

	protected Object renderURI(Resource onP) {
		String qName = onP.getModel().qnameFor(onP.getURI());
		return qName == null ? onP.getLocalName() : qName;
	}

}
