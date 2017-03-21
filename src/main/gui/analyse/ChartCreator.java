package main.gui.analyse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JDialog;

import main.analyse.Analyser;
import main.util.Pair;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class ChartCreator {

	/*
	 * Create a chart Pair: Name of the key and number of values
	 */
	public ChartPanel createChart() {
		JDialog ratioHommeFemmeJdialog = new JDialog();
		ratioHommeFemmeJdialog.setTitle("Résultats");

		final DefaultPieDataset pieDataset = new DefaultPieDataset();

		List<Pair<String, Integer>> l = findNbValues(Analyser.getInstance()
				.getHashMap());
		for (Pair<String, Integer> v : l) {
			pieDataset.setValue(v.getFirst(), v.getSecond());
		}

		final JFreeChart pieChart = ChartFactory.createPieChart("Résultats",
				pieDataset, true, false, false);
		ChartPanel chartPanel = new ChartPanel(pieChart);
		return chartPanel;
	}

	private List<Pair<String, Integer>> findNbValues(
			HashMap<String, List<Pair<String, Double>>> h) {
		List<Pair<String, Integer>> l = new ArrayList<Pair<String, Integer>>();
		for (String mapKey : h.keySet()) {
			int i = 0;
			for (@SuppressWarnings("unused")
			Pair<String, Double> p : h.get(mapKey)) {
				i++;
			}
			l.add(new Pair<String, Integer>(mapKey, i));
		}

		return l;

	}
}
