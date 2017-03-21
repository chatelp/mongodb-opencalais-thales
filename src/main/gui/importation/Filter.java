package main.gui.importation;

import java.util.ArrayList;
import java.util.List;

import main.ontology.OwlParser;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * Filter of an ontology owl
 */
public class Filter extends Dialog {

	protected Object result;
	protected Shell shlFiltrageDuDump;
	protected String ontoPath;
	protected List<Button> btnCheck;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public Filter(Shell parent, int style) {
		super(parent, style);
		btnCheck = new ArrayList<Button>();
		setText("SWT Dialog");
		ontoPath = "misc/files/ontology/onto.owl";
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlFiltrageDuDump.open();
		shlFiltrageDuDump.layout();
		Display display = getParent().getDisplay();
		while (!shlFiltrageDuDump.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog (Selection of an owl file etc...).
	 */
	private void createContents() {
		shlFiltrageDuDump = new Shell(getParent(), getStyle());
		shlFiltrageDuDump.setSize(360, 320);
		shlFiltrageDuDump.setText("Filtrage du dump");
		shlFiltrageDuDump.setLayout(new FillLayout(SWT.HORIZONTAL));

		ScrolledComposite scrolledComposite = new ScrolledComposite(
				shlFiltrageDuDump, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		Label lblChoisissezLesCls = new Label(composite, SWT.NONE);
		lblChoisissezLesCls
				.setText("Choisissez les cl\u00E9s utilis\u00E9es pour filtrer le document:");
		new Label(composite, SWT.NONE);

		final Label lblontologie = new Label(composite, SWT.NONE);
		GridData gd_lblontologie = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblontologie.widthHint = 343;
		lblontologie.setLayoutData(gd_lblontologie);
		lblontologie.setText("(ontologie: " + ontoPath + ")");

		Button btnAutreOntologie = new Button(composite, SWT.NONE);
		btnAutreOntologie.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO
				FileDialog fd = new FileDialog(shlFiltrageDuDump);
				fd.setText("Choisir une ontologie owl");
				String[] filterExt = { "*.owl"};
				fd.setFilterExtensions(filterExt);
				String path = fd.open();
				
				if (path != null && !path.contentEquals(""))
					ontoPath = path;
				
				lblontologie.setText("(ontologie: " + ontoPath + ")");
			}
		});
		btnAutreOntologie.setText("Choisir une autre ontologie");
		new Label(composite, SWT.NONE);

		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2,
				1));

		List<String> strs = OwlParser.getInstance().findLabelKeysFromDocument(
				"WikiDumpDocument", "en");

		for (String s : strs) {
			Button btnCheckButton = new Button(composite, SWT.CHECK);
			btnCheckButton.setText(s);
			btnCheckButton.setSelection(true);
			btnCheck.add(btnCheckButton);
		}

		new Label(composite, SWT.NONE);

		Button btnValider = new Button(composite, SWT.NONE);
		btnValider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<String> selectedKeys = new ArrayList<String>();
				for (Button b : btnCheck) {
					if (b.getSelection())
						selectedKeys.add(b.getText());
				}
				
				OwlParser.getInstance().setChosenKeys(selectedKeys);
				shlFiltrageDuDump.close();
			}
		});
		btnValider.setText("Valider");
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

	}
}
