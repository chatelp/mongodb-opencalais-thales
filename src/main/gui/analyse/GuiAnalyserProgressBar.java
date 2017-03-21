package main.gui.analyse;

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

public class GuiAnalyserProgressBar extends Dialog {

	protected Object result;
	protected Shell shlAnalyser;
	private AnalyseLongOperation thread;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public GuiAnalyserProgressBar(Shell parent, int style) {
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
		shlAnalyser.open();
		shlAnalyser.layout();
		Display display = getParent().getDisplay();

		thread = new AnalyseLongOperation(shlAnalyser);
		thread.start();

		while (!shlAnalyser.isDisposed()) {
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
		shlAnalyser = new Shell(getParent(), getStyle());
		shlAnalyser.setSize(355, 135);
		shlAnalyser.setText("Requête");

		ProgressBar progressBar = new ProgressBar(shlAnalyser,
				SWT.INDETERMINATE);
		progressBar.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_BLUE));
		progressBar.setMaximum(50);
		progressBar.setBounds(7, 38, 335, 28);

		final Button btnArrter = new Button(shlAnalyser, SWT.NONE);
		btnArrter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlAnalyser.dispose();
			}
		});
		btnArrter.setBounds(274, 72, 68, 23);
		btnArrter.setText("Arr\u00EAter");

		Label lblExportationEnCours = new Label(shlAnalyser, SWT.NONE);
		lblExportationEnCours.setBounds(10, 7, 441, 13);
		lblExportationEnCours
				.setText("Upload des fichiers à analyser sur OpenCalais.org.\n" +
						 "Cette op\u00E9ration peut prendre plusieurs minutes. Merci de patienter.");
	}

}
