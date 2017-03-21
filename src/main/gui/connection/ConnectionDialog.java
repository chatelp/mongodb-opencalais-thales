package main.gui.connection;


import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Button;

public class ConnectionDialog extends Dialog {

	protected Object result;
	protected static Shell shell;
	private static Text txtHost = null;
	private static Text txtUsername = null;
	private static Text txtPassword = null;
	private static Text txtBddName = null;
	private static Spinner spinPort = null;
	
	public static Shell getShell() {
		return shell;
	}

	public static Text getTxtHost() {
		return txtHost;
	}

	public static Text getTxtUsername() {
		return txtUsername;
	}

	public static Text getTxtPassword() {
		return txtPassword;
	}

	public static Text getTxtBddName() {
		return txtBddName;
	}
	
	public static Spinner getSpinPort() {
		return spinPort;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ConnectionDialog(Shell parent, int style) {
		super(parent, style);
		setText("Connexion");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.CLOSE | SWT.TITLE
				| SWT.PRIMARY_MODAL);
		shell.setSize(265, 219);
		shell.setText(getText());

		Label lblHost = new Label(shell, SWT.NONE);
		lblHost.setBounds(10, 18, 49, 13);
		lblHost.setText("Host*");

		Label lblPort = new Label(shell, SWT.NONE);
		lblPort.setBounds(10, 43, 49, 13);
		lblPort.setText("Port*");

		Label lblNomDutilisateur = new Label(shell, SWT.NONE);
		lblNomDutilisateur.setBounds(10, 70, 79, 13);
		lblNomDutilisateur.setText("Nom d'utilisateur");

		Label lblMotDePasse = new Label(shell, SWT.NONE);
		lblMotDePasse.setBounds(10, 95, 64, 13);
		lblMotDePasse.setText("Mot de passe");

		Label lblBaseDeDonnes = new Label(shell, SWT.NONE);
		lblBaseDeDonnes.setBounds(10, 120, 94, 13);
		lblBaseDeDonnes.setText("Base de donn\u00E9es*");

		txtHost = new Text(shell, SWT.BORDER);
		txtHost.setText("localhost");
		txtHost.setBounds(110, 15, 140, 19);

		spinPort = new Spinner(shell, SWT.BORDER);
		spinPort.setMaximum(99999);
		spinPort.setSelection(27017);
		spinPort.setBounds(110, 40, 90, 21);

		txtUsername = new Text(shell, SWT.BORDER);
		txtUsername.setBounds(110, 67, 140, 19);

		txtPassword = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		txtPassword.setBounds(110, 92, 140, 19);

		txtBddName = new Text(shell, SWT.BORDER);
		txtBddName.setBounds(110, 117, 140, 19);

		Button btnConnexion = new Button(shell, SWT.NONE);
		btnConnexion.addSelectionListener(new ConnectBtnListener());
		btnConnexion.setBounds(182, 154, 68, 23);
		btnConnexion.setText("Connexion");
		btnConnexion.setSelection(true);

	}
}
