package main.jongo.mapping;

public class Introspection {

	/**
	 * Checks for the existence of an attribute in a class
	 * @param attribute
	 * @return
	 */
	@SuppressWarnings("unused")
	public Boolean containsAttribute(String attribute) {

		Class<?> c = RuntimeClassCreator.createClass();
		for (int i = 0; i < c.getDeclaredFields().length; i++) {
			if (attribute.equalsIgnoreCase(c.getDeclaredFields()[i]
					.getName()))
				;
			return true;
		}

		return false;
	}

}
