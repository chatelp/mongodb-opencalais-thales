package main.jongo.mapping;

/**
 * Class representing system.index collection
 * This is needed by Jongo for a correct mapping
 */
public class Index {
	private String v;
	private String key;
	private String ns;
	private String name;

	public String getV() {
		return v;
	}

	public String getKey() {
		return key;
	}

	public String getNs() {
		return ns;
	}

	public String getName() {
		return name;
	}

}
