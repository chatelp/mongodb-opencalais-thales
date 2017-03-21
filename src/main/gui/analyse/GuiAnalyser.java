package main.gui.analyse;

import java.awt.Frame;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.analyse.Analyser;
import main.store.Store;
import main.util.Pair;
import net.sf.json.JSONObject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jfree.chart.ChartPanel;

/**
 * Create all visual elements in "Analyse" tab
 */
public class GuiAnalyser {
	private static volatile GuiAnalyser instance = null;
	private Composite composite_1;
	private List<Table> tables;

	private GuiAnalyser() {
		super();
		tables = new ArrayList<Table>();
	}

	public final static GuiAnalyser getInstance() {
		if (GuiAnalyser.instance == null) {
			synchronized (GuiAnalyser.class) {
				if (GuiAnalyser.instance == null) {
					GuiAnalyser.instance = new GuiAnalyser();
				}
			}
		}
		return GuiAnalyser.instance;
	}

	public void createChartElements(Composite parent, FormToolkit formToolkit) {
		if (Analyser.getInstance().getHashMap() != null) {
			deleteAnalyse();
			composite_1 = new Composite(parent, SWT.EMBEDDED);
			GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, false,
					false, 3, 1);
			gd_composite_1.heightHint = 315;
			composite_1.setLayoutData(gd_composite_1);
			formToolkit.adapt(composite_1);
			formToolkit.paintBordersFor(composite_1);

			Frame frame = SWT_AWT.new_Frame(composite_1);
			{
				Panel panel = new Panel();
				frame.add(panel);
				panel.setLayout(new java.awt.BorderLayout(0, 0));
				{
					ChartCreator cc = new ChartCreator();
					ChartPanel chartPanel = cc.createChart();
					panel.add(chartPanel);
				}
			}
		}
	}

	public void createArrayElements(Composite parent, FormToolkit formToolkit) {
		JSONObject json = new JSONObject();

		if (Analyser.getInstance().getHashMap() != null) {
			HashMap<String, List<Pair<String, Double>>> h = Analyser
					.getInstance().getHashMap();
			for (String mapKey : h.keySet()) {
				Table table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
				GridData gd_table = new GridData(SWT.FILL, SWT.TOP, true, true,
						1, 1);
				gd_table.heightHint = 130;
				gd_table.widthHint = 127;

				table.setLayoutData(gd_table);
				formToolkit.adapt(table);
				formToolkit.paintBordersFor(table);
				table.setHeaderVisible(true);
				table.setLinesVisible(true);

				TableColumn tblclmnTest = new TableColumn(table, SWT.NONE);
				tblclmnTest.setWidth(170);
				tblclmnTest.setText(mapKey);

				TableColumn tblclmnTest_1 = new TableColumn(table, SWT.NONE);
				tblclmnTest_1.setWidth(70);
				tblclmnTest_1.setText("Pertinence");

				List<Pair<String, Double>> l = new ArrayList<Pair<String, Double>>();
				for (Pair<String, Double> p : h.get(mapKey)) {
					l.add(p);
				}
				sortList(l);
				for (Pair<String, Double> p : l) {
					TableItem tableItem = new TableItem(table, SWT.NONE);
					tableItem.setText(new String[] { p.getFirst(),
							String.valueOf(p.getSecond()) });
				}

				tables.add(table);

				// To construct JSON, we keep only String and not relevance
				List<String> l2 = new ArrayList<String>();
				for (Pair<String, Double> p : h.get(mapKey)) {
					l2.add(p.getFirst());
				}
				json.accumulate(mapKey, l2);
			}
			json.accumulate("TitleOfAnalysedDocuments", Analyser.getInstance()
					.getTitlesAnalysed());

			new Store().storeResAnalysis(json);
		}
	}

	/**
	 * Algorithm: Selection sort
	 * @param l
	 */
	private void sortList(List<Pair<String, Double>> l) {
		int cpt;
		Double element;
		Pair<String, Double> save = null;

		for (int i = 1; i < l.size(); i++) {
			element = l.get(i).getSecond();
			cpt = i - 1;
			save = l.get(i);
			while (cpt >= 0 && l.get(cpt).getSecond() < element) {
				l.set(cpt + 1, l.get(cpt));
				cpt--;
			}
			l.set(cpt + 1, save);
		}
	}

	public void deleteAnalyse() {
		if (composite_1 != null) {
			composite_1.dispose();
			for (Table t : tables) {
				t.dispose();
			}
		}
	}
}
