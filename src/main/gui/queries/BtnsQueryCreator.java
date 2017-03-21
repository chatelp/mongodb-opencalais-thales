package main.gui.queries;

import java.util.ArrayList;
import java.util.List;

import main.db.mongoDB.MongoUtil;
import main.gui.Window;
import main.gui.overview.OverviewState;
import main.ontology.OwlParser;

import org.apache.wink.json4j.JSONException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Create buttons useful for creating query
 */
public class BtnsQueryCreator {

	private static volatile BtnsQueryCreator instance = null;
	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());
	private static Composite composite_btnQueries;
	private ScrolledComposite composite_1;
	private Text text_1;
	private CCombo combo_1;

	private BtnsQueryCreator() {
		super();
	}

	public final static BtnsQueryCreator getInstance() {
		if (BtnsQueryCreator.instance == null) {
			synchronized (GuiQuery.class) {
				if (BtnsQueryCreator.instance == null) {
					BtnsQueryCreator.instance = new BtnsQueryCreator();
				}
			}
		}
		return BtnsQueryCreator.instance;
	}

	public CCombo getCombo_1() {
		return combo_1;
	}

	public static Composite getComposite_btnQueries() {
		return composite_btnQueries;
	}

	public void createBtns() {
		List<Field> fields = new ArrayList<Field>();
		OverviewState state = OverviewState.getInstance();
		MongoUtil util = new MongoUtil();
		List<String> keys = new ArrayList<String>();
		composite_1 = Window.getScrolledComposite_queriesBtn();

		composite_btnQueries = new Composite(composite_1, SWT.NONE);
		formToolkit.adapt(composite_btnQueries);
		formToolkit.paintBordersFor(composite_btnQueries);
		composite_btnQueries.setLayout(new GridLayout(4, false));

		composite_1.setContent(composite_btnQueries);
		composite_1.setMinSize(composite_btnQueries.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		try {
			keys = util.getKeys(state.getCurrentCollection(), 0, 100);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String key : keys) {
			final CCombo combo_1 = new CCombo(composite_btnQueries, SWT.BORDER);
			GridData gd_combo_1 = new GridData(SWT.LEFT, SWT.CENTER, false,
					false, 1, 1);
			gd_combo_1.widthHint = 50;
			combo_1.setLayoutData(gd_combo_1);

			combo_1.add("ET");
			combo_1.add("OU");
			combo_1.setEditable(false);
			combo_1.select(0);
			// If it is the first element
			if (!key.contentEquals(keys.get(0))) {
				combo_1.setVisible(false);
			}
			formToolkit.adapt(combo_1);
			formToolkit.paintBordersFor(combo_1);

			Label lblNewLabel_1 = new Label(composite_btnQueries, SWT.NONE);
			GridData gd_lblNewLabel_1 = new GridData(SWT.FILL, SWT.CENTER,
					false, false, 1, 1);
			gd_lblNewLabel_1.widthHint = 100;
			lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
			formToolkit.adapt(lblNewLabel_1, true, true);
			lblNewLabel_1.setText(key);

			CCombo combo = new CCombo(composite_btnQueries, SWT.BORDER);
			GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, false,
					false, 1, 1);
			gd_combo.widthHint = 90;
			combo.setLayoutData(gd_combo);
			combo.add("");
			combo.add("=");

			String className = OwlParser.getInstance().findClassFromLabelKey(
					key, "en");
			String type = ""
					+ OwlParser.getInstance().findTypeFromKey(className);

			if (type.equalsIgnoreCase("integer")) {
				combo.add("<");
				combo.add(">");
			} else
				combo.add("contient");

			combo.setEditable(false);
			formToolkit.adapt(combo);
			formToolkit.paintBordersFor(combo);

			text_1 = new Text(composite_btnQueries, SWT.BORDER);
			GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1);
			gd_text_1.widthHint = 125;
			text_1.setLayoutData(gd_text_1);
			formToolkit.adapt(text_1, true, true);

			final Field field = new Field(key, combo, text_1, combo_1);

			fields.add(field);
		}
		Window.setFields(fields);
		composite_btnQueries.pack();
	}

	public void deleteBtnQuery() {
		if (composite_btnQueries != null)
			composite_btnQueries.dispose();

	}
}
