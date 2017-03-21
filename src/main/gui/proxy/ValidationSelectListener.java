package main.gui.proxy;

import main.analyse.ConfigUpdater;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * Listener of Button "validation" of proxy window.
 */
public class ValidationSelectListener implements SelectionListener {

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		String host = ProxyDialog.getTextHost().getText();
		int port = ProxyDialog.getSpinner().getSelection();
		
		new ConfigUpdater(host, port, !ProxyDialog.getBtnNePasUtiliser()
				.getSelection());

	}
}
