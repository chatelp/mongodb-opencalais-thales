package main.gui;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;

import main.db.mongoDB.MongoDbConnector;
import main.gui.connection.ConnectionDialog;
import main.gui.overview.Pagination;
import main.gui.queries.BtnsQueryCreator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;

/**
 * Button "Connexion" listener
 *
 */
public class ConnectionBtnListener implements SelectionListener {

	Shell shell = null;

	public ConnectionBtnListener(Shell shell) {
		this.shell = shell;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		if (!Window.getConnected()) {
			ConnectionDialog window = new ConnectionDialog(shell,
					SWT.APPLICATION_MODAL);
			window.open();

			if (Window.getConnected())
				try {
					Set<String> collections = MongoDbConnector.getDB()
							.getCollectionNames();
					Iterator<String> i = collections.iterator();
					while (i.hasNext())
						Window.getList().add(i.next().toString());

				} catch (UnknownHostException e1) {
					IStatus status = new Status(IStatus.ERROR, "Plugin ID",
							"Hôte inconnu", e1);
					ErrorDialog
							.openError(
									ConnectionDialog.getShell(),
									"Erreur de connexion",
									"Connexion impossible à la base de données MongoDB",
									status);
				}
		} else {
			Window.getList().removeAll();
			Window.getLblDocument().setText("0 document");
			Pagination.getInstance(0, 0).deleteAllColumns();
			Pagination.getInstance(0, 0).setNbDocs(0);
			Pagination.getInstance(0, 0).updateSpinner();
			if (BtnsQueryCreator.getComposite_btnQueries() != null)
				BtnsQueryCreator.getComposite_btnQueries().dispose();
			MongoDbConnector.disconnect();
			Window.enableButtons();
		}

	}

}
