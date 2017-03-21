package main.gui.exportation;

import java.io.IOException;

import main.gui.overview.OverviewState;
import main.store.Export;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ExportDialog extends Dialog {

	protected Object result;
	protected Shell shlExportation;
	private Text textPath;
	private Text textName;
	private EnumExport exportType;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 * @param type
	 * 
	 */
	public ExportDialog(Shell parent, int style, EnumExport exportType) {
		super(parent, style);
		this.exportType = exportType;
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlExportation.open();
		shlExportation.layout();
		Display display = getParent().getDisplay();
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
		shlExportation.setSize(400, 210);
		shlExportation.setText("Exportation");

		Label lblChoisissezLeFormat = new Label(shlExportation, SWT.NONE);
		lblChoisissezLeFormat.setBounds(10, 10, 384, 29);
		if (exportType == EnumExport.ANALYSE)
			lblChoisissezLeFormat.setText("Exportation de l'analyse...");
		else
			lblChoisissezLeFormat
					.setText("Choisissez le format dans lequel vous d\u00E9sirez exporter"
							+ "les donn\u00E9es puis cliquez\r\nsur \"Exporter\".");

		final Combo combo;
		if (exportType != EnumExport.ANALYSE) {
			Label lblFormat = new Label(shlExportation, SWT.NONE);
			lblFormat.setBounds(10, 56, 49, 13);
			lblFormat.setText("Format:");

			combo = new Combo(shlExportation, SWT.READ_ONLY);
			combo.setBounds(84, 53, 87, 21);
			combo.add("Xml", 0);
			combo.add("Json", 1);
			combo.setText(combo.getItem(0));
		}
		else
			combo = null;

		Label lblDestination = new Label(shlExportation, SWT.NONE);
		lblDestination.setBounds(10, 86, 68, 13);
		lblDestination.setText("Destination:");

		textPath = new Text(shlExportation, SWT.BORDER);
		textPath.setEditable(false);
		textPath.setBounds(84, 80, 176, 19);

		Button btnBrowse = new Button(shlExportation, SWT.NONE);
		btnBrowse.setBounds(267, 78, 68, 23);
		btnBrowse.setText("Parcourir");
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(shlExportation,
						SWT.NULL);
				String path = dialog.open();
				if (path != null) {
					textPath.setText(path);
				}
			}
		});

		Button btnExporter = new Button(shlExportation, SWT.NONE);
		btnExporter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Export ex = new Export();
				String path = textPath.getText() + "\\" + textName.getText();
				try {
					if (exportType == EnumExport.ALL)
						ex.exportAll(combo.getText(), path, OverviewState
								.getInstance().getCurrentCollection());
					else if (exportType == EnumExport.LAST)
						ex.exportLastRequest(combo.getText(), path,
								OverviewState.getInstance()
										.getCurrentCollection());
					else
						ex.exportAnalyse(path);

					shlExportation.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		btnExporter.setBounds(10, 140, 68, 23);
		btnExporter.setText("Exporter");

		Button btnAnnuler = new Button(shlExportation, SWT.NONE);
		btnAnnuler.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlExportation.close();
			}
		});
		btnAnnuler.setBounds(84, 140, 68, 23);
		btnAnnuler.setText("Annuler");

		textName = new Text(shlExportation, SWT.BORDER);
		textName.setBounds(84, 105, 176, 19);

		Label lblNomDuDump = new Label(shlExportation, SWT.NONE);
		lblNomDuDump.setBounds(10, 111, 49, 13);
		lblNomDuDump.setText("Nom:");

	}
}
