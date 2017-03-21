package main.db.mongoDB;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.Mongo;

/*
 * Connection to MongoDb database
 * This class is a Singleton
 * 
 */
public final class MongoDbConnector {
	private static Mongo mongo = null;
	private static volatile MongoDbConnector instance = null;
	private static DB db = null;

	/**
	 * 
	 * @param dbName
	 * @throws UnknownHostException
	 */
	private MongoDbConnector(String dbName) throws UnknownHostException {
		mongo = new Mongo();
		MongoDbConnector.db = mongo.getDB(dbName);
	}

	/**
	 * 
	 * @param dbName
	 * @param host
	 * @param port
	 * @throws UnknownHostException
	 */
	private MongoDbConnector(String dbName, String host, int port)
			throws UnknownHostException {
		mongo = new Mongo(host, port);
		MongoDbConnector.db = mongo.getDB(dbName);
	}

	/**
	 * 
	 * @param dbName
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @throws UnknownHostException
	 */
	private MongoDbConnector(String dbName, String host, int port,
			String username, char[] password) throws UnknownHostException {
		mongo = new Mongo(host, port);
		MongoDbConnector.db = mongo.getDB(dbName);
		// TODO: Use auth (true = authentified; false = password/username error)
		// boolean auth = mongo.getDB(dbName).authenticate(username, password);
	}

	public final static DB getDB() throws UnknownHostException {
		if (MongoDbConnector.instance == null)
			synchronized (MongoDbConnector.class) {
				if (MongoDbConnector.instance == null) {
					MongoDbConnector.instance = new MongoDbConnector("default");
				}
			}
		return MongoDbConnector.db;
	}

	public final static DB getDB(String dbName) throws UnknownHostException {
		if (MongoDbConnector.instance == null)
			synchronized (MongoDbConnector.class) {
				if (MongoDbConnector.instance == null) {
					MongoDbConnector.instance = new MongoDbConnector(dbName);
				}
			}
		return MongoDbConnector.db;
	}

	public final static DB getDB(String dbName, String host, int port)
			throws UnknownHostException {
		if (MongoDbConnector.instance == null)
			synchronized (MongoDbConnector.class) {
				if (MongoDbConnector.instance == null) {
					MongoDbConnector.instance = new MongoDbConnector(dbName,
							host, port);
				}
			}
		return MongoDbConnector.db;
	}

	public final static DB getDB(String dbName, String host, int port,
			String username, char[] password) throws UnknownHostException {
		if (MongoDbConnector.instance == null)
			synchronized (MongoDbConnector.class) {
				if (MongoDbConnector.instance == null) {
					MongoDbConnector.instance = new MongoDbConnector(dbName,
							host, port, username, password);
				}
			}
		return MongoDbConnector.db;
	}

	public final static void disconnect() {
		mongo.close();
		MongoDbConnector.instance = null;
	}
}
