package main.gui.overview;

import java.net.UnknownHostException;

import main.db.mongoDB.MongoDbConnector;
import main.gui.Window;
import main.gui.queries.BtnsQueryCreator;
import main.gui.queries.GuiQuery;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.DB;
import com.mongodb.DBCollection;

/*
 * Read and display documents related to the selected collection
 */
public class CollectionSelectListener implements SelectionListener {

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
		DBCollection dbc;
		String collection = "";
		String[] selection = Window.getList().getSelection();
		
		for (int i = 0; i < selection.length; i++) {
			collection += selection[i];
		}

		Window.getBtnLaunchRequest().setEnabled(true);
		DB db;
		try {
			db = MongoDbConnector.getDB();
			dbc = db.getCollection(collection);
			OverviewState.getInstance().setCurrentCollection(dbc);
			Pagination p = Pagination.getInstance(db.getCollection(collection)
					.count(), 100);
			p.setNbDocs(dbc.count());

			GuiQuery.getInstance().resetRequest();

			Jongo jongo = new Jongo(MongoDbConnector.getDB());
			MongoCollection coll = jongo.getCollection(OverviewState
					.getInstance().getCurrentCollection().getName());
			Window.setArticles(coll.find(GuiQuery.getInstance().getRequest()));

			p.updateSpinner();

			Window.getLblDocument().setText(p.getNbDocs() + " documents");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BtnsQueryCreator bqc = BtnsQueryCreator.getInstance();
		bqc.createBtns();
	}
}
