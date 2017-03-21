package main.gui.analyse;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.widgets.Shell;

import main.analyse.Analyser;
import main.gui.overview.OverviewState;
import main.store.Export;

public class AnalyseLongOperation extends Thread {

	private Shell shlAnalyser;

	public AnalyseLongOperation(Shell shlAnalyser) {
		this.shlAnalyser = shlAnalyser;
	}

	/**
	 * Launch a analyse
	 */
	public void run() {
		Export ex = new Export();
		Analyser a = Analyser.getInstance();

		try {
			ex.exportLastRequest("xml", "misc/files/analyse.xml", OverviewState
					.getInstance().getCurrentCollection());
		} catch (IOException e) {
			e.printStackTrace();
		}
		File f = new File("misc/files/analyse.xml");
		try {
			a.analyse(f);
		} catch (IOException e) {
			e.printStackTrace();
		}

		shlAnalyser.getDisplay().asyncExec(new Runnable() {
			public void run() {
				shlAnalyser.dispose();
			}
		});
	}
}
