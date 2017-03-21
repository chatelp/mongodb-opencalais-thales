package main.gui.queries;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class GuiQueryProgressBar extends Dialog {

	protected Object result;
	protected Shell shlQuery;
	private QueryLongOperation thread;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public GuiQueryProgressBar(Shell parent, int style) {
		super(parent, style);
		setText("Requête");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlQuery.open();
		shlQuery.layout();
		Display display = shlQuery.getDisplay();

		thread = new QueryLongOperation(display);
		thread.start();

		while (!shlQuery.isDisposed() && !thread.isInterrupted()) {
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
		shlQuery = new Shell(getParent(), getStyle());
		shlQuery.setSize(355, 135);
		shlQuery.setText("Requête");

		ProgressBar progressBar = new ProgressBar(shlQuery, SWT.INDETERMINATE);
		progressBar.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_BLUE));
		progressBar.setMaximum(50);
		progressBar.setBounds(7, 38, 335, 28);

		final Button btnArrter = new Button(shlQuery, SWT.NONE);
		btnArrter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlQuery.dispose();
			}
		});
		btnArrter.setBounds(274, 72, 68, 23);
		btnArrter.setText("Arr\u00EAter");

		Label lblExportationEnCours = new Label(shlQuery, SWT.NONE);
		lblExportationEnCours.setBounds(10, 7, 441, 13);
		lblExportationEnCours.setText("Requête en cours de traitement...");
	}

}
