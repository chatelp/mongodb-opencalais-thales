package main.gui.importation;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ImportProgressBar extends Dialog {

	protected Object result;
	protected Shell shlExportation;
	private ImportLongOperation thread;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ImportProgressBar(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open(String path) {
		createContents();
		shlExportation.open();
		shlExportation.layout();
		Display display = shlExportation.getDisplay();

		thread = new ImportLongOperation(display, path);
		thread.start();

		while (!shlExportation.isDisposed()) {
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
		shlExportation = new Shell(getParent(), getStyle());
		shlExportation.setSize(355, 135);
		shlExportation.setText("Importation");

		ProgressBar progressBar = new ProgressBar(shlExportation,
				SWT.INDETERMINATE);
		progressBar.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_BLUE));
		progressBar.setMaximum(50);
		progressBar.setBounds(7, 38, 335, 28);

		final Button btnArrter = new Button(shlExportation, SWT.NONE);
		btnArrter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				thread.interrupt();
				shlExportation.dispose();
			}
		});
		btnArrter.setBounds(274, 72, 68, 23);
		btnArrter.setText("Arr\u00EAter");

		Label lblExportationEnCours = new Label(shlExportation, SWT.NONE);
		lblExportationEnCours.setBounds(10, 7, 441, 13);
		lblExportationEnCours
				.setText("Cette op\u00E9ration peut prendre plusieurs minutes. Merci de patienter.");

	}

}
