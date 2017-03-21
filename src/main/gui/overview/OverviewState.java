package main.gui.overview;

import java.util.List;

import com.mongodb.DBCollection;

/**
 *	Stores information query and collection
 */
public class OverviewState {
	private static volatile OverviewState instance = null;

	private DBCollection currentCollection;
	private String request;
	private Boolean requestEnabled; // Activated or disabled request ?
	private List<String> id; // Request results

	private OverviewState() {

	}

	public DBCollection getCurrentCollection() {
		return currentCollection;
	}

	public void setCurrentCollection(DBCollection currentCollection) {
		this.currentCollection = currentCollection;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public Boolean getRequestEnabled() {
		return requestEnabled;
	}

	public void setRequestEnabled(Boolean requestEnabled) {
		this.requestEnabled = requestEnabled;
	}

	public List<String> getId() {
		return id;
	}

	public void setId(List<String> id) {
		this.id = id;
	}

	public final static OverviewState getInstance() {
		if (OverviewState.instance == null) {
			synchronized (OverviewState.class) {
				if (OverviewState.instance == null) {
					OverviewState.instance = new OverviewState();
				}
			}
		}
		return OverviewState.instance;
	}
}
