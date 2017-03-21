package main.gui.overview;

import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import main.gui.Window;
import main.jongo.mapping.RuntimeClassCreator;

import org.apache.wink.json4j.JSONException;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jongo.Find;

/**
 * Managing pagination Singleton
 */
public final class Pagination {

	private static volatile Pagination instance = null;
	private int nbDocByPage; // Number of documents displayed on each page
	private long nbDocs; // Total document number

	private Pagination(long nbDocs, int nbDocByPage) {
		super();
		this.nbDocByPage = nbDocByPage;
		this.nbDocs = nbDocs;
	}

	public final static Pagination getInstance(long nbDocs, int nbDocByPage) {
		if (Pagination.instance == null) {
			synchronized (Pagination.class) {
				if (Pagination.instance == null) {
					Pagination.instance = new Pagination(nbDocs, nbDocByPage);
				}
			}
		}
		return Pagination.instance;
	}

	public int getNbDocByPage() {
		return nbDocByPage;
	}

	public void setNbDocByPage(int nbDocByPage) {
		this.nbDocByPage = nbDocByPage;
	}

	public long getNbDocs() {
		return nbDocs;
	}

	public void setNbDocs(long nbDocs) {
		this.nbDocs = nbDocs;
	}

	public void setNbDocs(Find articles) {
		long res = 0;
		Class<?> c = RuntimeClassCreator.createClass();
		Iterable<?> it = articles.as(c);
		Iterator<?> cursor = it.iterator();

		while (cursor.hasNext()) {
			res++;
			cursor.next();
		}

		setNbDocs(res);
	}

	public int getNbPages() {
		if (nbDocByPage != 0)
			return (int) (nbDocs / nbDocByPage) + 1;
		else
			return 0;
	}

	/**
	 * Update number of pages
	 */
	public void updateSpinner() {
		Window.getSpinner().setMaximum(getNbPages());
		Window.getSpinner().setSelection(1);
		Window.getLblSur().setText("sur " + getNbPages());
	}

	/**
	 * Create all colums
	 */
	public void createColumns(List<String> keys) throws JSONException {
		deleteAllColumns();

		Window.getTable().getParent().setRedraw(false);
		for (String key : keys) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(
					Window.getTableViewer(), SWT.NONE);
			TableColumn tblclmnTest = tableViewerColumn.getColumn();
			Window.getTcl_composite().setColumnData(tblclmnTest,
					new ColumnPixelData(150, true, true));
			tblclmnTest.setText(key);
		}
		Window.getTable().getParent().layout();
		Window.getTable().getParent().setRedraw(true);
	}

	/**
	 * Fill all columns (from index 'begin' to index 'end') in the collection
	 * coll
	 * 
	 * @param articles
	 * @param begin
	 * @param end
	 * @throws JSONException
	 * @throws UnknownHostException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void fillColumns(Find articles, int begin, int end)
			throws JSONException, UnknownHostException, SecurityException,
			IllegalArgumentException, IllegalAccessException {

		Class<?> c = RuntimeClassCreator.createClass();
		Iterable<?> it = articles.skip(begin).as(c);
		Iterator<?> cursor = it.iterator();

		for (int i = begin; i < end; i++) {
			if (!cursor.hasNext())
				break;

			Object a = cursor.next();

			// Lines
			for (int j = 0; j < Window.getTable().getColumnCount(); j++) {
				TableItem item;
				if (j == 0)
					item = new TableItem(Window.getTable(), SWT.NONE);
				else
					item = Window.getTable().getItem(i % this.nbDocByPage);

				try {
					Field attribut = a.getClass().getField(
							Window.getTable().getColumn(j).getText());

					Object valeur = attribut.get(a);
					if (valeur != null)
						item.setText(j, valeur.toString());
				} catch (NoSuchFieldException e) {
					item.setText(j, "");
				}
			}
		}
	}

	/**
	 * Delete array
	 */
	public void deleteAllColumns() {
		while (Window.getTable().getItemCount() > 0)
			Window.getTable().getItem(0).dispose();

		while (Window.getTable().getColumnCount() > 0)
			Window.getTable().getColumn(0).dispose();
	}
}
