package main.gui.overview;

import java.net.UnknownHostException;
import java.util.List;

import main.db.mongoDB.MongoUtil;
import main.gui.Window;

import org.apache.wink.json4j.JSONException;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import com.mongodb.DBCollection;

/**
 * Update spinner value
 */
public class SpinnerListener implements ModifyListener {
	@Override
	public void modifyText(ModifyEvent arg0) {

		DBCollection dbc = OverviewState.getInstance().getCurrentCollection();
		Pagination p = null;

		if (dbc != null) {
			p = Pagination.getInstance(dbc.count(), 100);
			MongoUtil util = new MongoUtil();

			int min = Window.getSpinner().getSelection() * p.getNbDocByPage()
					- p.getNbDocByPage();
			int max = Window.getSpinner().getSelection() * p.getNbDocByPage();

			try {
				List<String> keys = util.getKeys(dbc, min, max);

				if (p.getNbDocs() > 0) {
					p.createColumns(keys);
					p.fillColumns(Window.getArticles(), min, max);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

	}
}
