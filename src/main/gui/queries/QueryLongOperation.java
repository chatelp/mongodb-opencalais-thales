package main.gui.queries;

import java.net.UnknownHostException;

import main.db.mongoDB.MongoDbConnector;
import main.gui.Window;
import main.gui.overview.OverviewState;
import main.gui.overview.Pagination;

import org.eclipse.swt.widgets.Display;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

public class QueryLongOperation extends Thread {

	private Display display;

	public QueryLongOperation(Display display) {
		this.display = display;
	}

	public void run() {

		final Pagination p = Pagination.getInstance(OverviewState.getInstance()
				.getCurrentCollection().count(), 100);

		Jongo jongo;
		try {
			jongo = new Jongo(MongoDbConnector.getDB());
			MongoCollection coll = jongo.getCollection(OverviewState
					.getInstance().getCurrentCollection().getName());

			Window.setArticles(coll.find(GuiQuery.getInstance().getRequest()));

		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		p.setNbDocs(Window.getArticles());

		display.asyncExec(new Runnable() {
			public void run() {
				p.updateSpinner();
				display.getActiveShell().dispose();
			}
		});
	}
}
