package main.gui.connection;

import main.db.mongoDB.MongoDbConnector;
import main.gui.Window;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.MessageBox;

import com.mongodb.DB;

public class ConnectBtnListener implements SelectionListener {

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
		DB db = null;
		String dbName = ConnectionDialog.getTxtBddName().getText();
		String host = ConnectionDialog.getTxtHost().getText();
		String userName = ConnectionDialog.getTxtUsername().getText();
		char[] password = ConnectionDialog.getTxtPassword().getText()
				.toCharArray();
		int port = ConnectionDialog.getSpinPort().getSelection();

		try {
			if (!userName.equals("") && !password.equals(""))
				db = MongoDbConnector.getDB(dbName, host, port, userName, password);
			else if (!dbName.equals("") && !host.equals("") && port != 0)
				db = MongoDbConnector.getDB(dbName, host, port);
			else if (!dbName.equals(""))
				db = MongoDbConnector.getDB(dbName);
			else {
				MessageBox mb = new MessageBox(ConnectionDialog.shell,
						SWT.ICON_ERROR | SWT.RETRY);
				mb.setMessage("Veuillez renseigner tous les champs marqués d'une étoile (*)");
				mb.open();
			}

			if (db != null)
			{
				Window.enableButtons();
				ConnectionDialog.shell.dispose();
			}

		} catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			IStatus status = new Status(IStatus.ERROR, "Plugin ID",
					"Hôte inconnu", e);
			ErrorDialog
					.openError(
							ConnectionDialog.shell,
							"Erreur de connexion",
							"Connexion impossible à la base de données MongoDB",
							status);
		} catch (Exception e2) {
			IStatus status = new Status(IStatus.ERROR, "Plugin ID",
					"Erreur inconnue, avez-vous bien lancé un serveur MongoDB ?", e2);
			ErrorDialog
					.openError(
							ConnectionDialog.shell,
							"Erreur de connexion",
							"Connexion impossible à la base de données MongoDB",
							status);
		}
	}
}
