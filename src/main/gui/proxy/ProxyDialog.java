package main.gui.proxy;

import main.analyse.ConfigReader;
import main.analyse.ConfigUpdater;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ProxyDialog extends Dialog {

	protected Object result;
	protected Shell shlConfigurationDunProxy;
	private static Button btnNePasUtiliser;
	private static Text textHost;
	private static Spinner spinner;
	private Label lblPort;
	private Label lblProxyHttp;

	public static Button getBtnNePasUtiliser() {
		return btnNePasUtiliser;
	}

	public static Text getTextHost() {
		return textHost;
	}

	public static Spinner getSpinner() {
		return spinner;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ProxyDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlConfigurationDunProxy.open();
		shlConfigurationDunProxy.layout();
		Display display = getParent().getDisplay();
		while (!shlConfigurationDunProxy.isDisposed()) {
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
		shlConfigurationDunProxy = new Shell(getParent(), getStyle());
		shlConfigurationDunProxy.setSize(330, 220);
		shlConfigurationDunProxy.setText("Configuration d'un proxy");

		lblProxyHttp = new Label(shlConfigurationDunProxy, SWT.NONE);
		lblProxyHttp.setEnabled(ConfigReader.getInstance().getUseProxy());
		lblProxyHttp.setBounds(20, 77, 65, 13);
		lblProxyHttp.setText("Proxy HTTP:");

		lblPort = new Label(shlConfigurationDunProxy, SWT.NONE);
		lblPort.setEnabled(ConfigReader.getInstance().getUseProxy());
		lblPort.setBounds(20, 109, 49, 13);
		lblPort.setText("Port:");

		textHost = new Text(shlConfigurationDunProxy, SWT.BORDER);
		textHost.setEnabled(ConfigReader.getInstance().getUseProxy());
		if (ConfigReader.getInstance().getHost() != null)
			textHost.setMessage(ConfigReader.getInstance().getHost());
		textHost.setBounds(101, 74, 200, 19);

		spinner = new Spinner(shlConfigurationDunProxy, SWT.BORDER);
		spinner.setPageIncrement(100);
		spinner.setTextLimit(4);
		spinner.setMaximum(99999);
		spinner.setEnabled(ConfigReader.getInstance().getUseProxy());
		spinner.setSelection(ConfigReader.getInstance().getPort());
		spinner.setBounds(101, 106, 75, 21);

		Button btnValider = new Button(shlConfigurationDunProxy, SWT.NONE);
		btnValider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String host = ProxyDialog.getTextHost().getText();
				int port = ProxyDialog.getSpinner().getSelection();
				
				new ConfigUpdater(host, port, !ProxyDialog.getBtnNePasUtiliser()
						.getSelection());
				shlConfigurationDunProxy.close();
			}
		});
		
		btnValider.setBounds(20, 146, 68, 23);
		btnValider.setText("Valider");

		btnNePasUtiliser = new Button(shlConfigurationDunProxy, SWT.CHECK);
		btnNePasUtiliser.setSelection(!ConfigReader.getInstance().getUseProxy());
		btnNePasUtiliser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableBtns();
			}
		});
		btnNePasUtiliser.setBounds(14, 20, 134, 16);
		btnNePasUtiliser.setText("Ne pas utiliser de proxy");

		Label label = new Label(shlConfigurationDunProxy, SWT.SEPARATOR
				| SWT.HORIZONTAL);
		label.setBounds(14, 57, 300, 2);
	}

	private void enableBtns() {
		textHost.setEnabled(!btnNePasUtiliser.getSelection());
		spinner.setEnabled(!btnNePasUtiliser.getSelection());
		lblPort.setEnabled(!btnNePasUtiliser.getSelection());
		lblProxyHttp.setEnabled(!btnNePasUtiliser.getSelection());
	}
}
