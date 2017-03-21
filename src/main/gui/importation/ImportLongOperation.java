package main.gui.importation;

import java.io.IOException;

import org.eclipse.swt.widgets.Display;

import main.store.Store;

public class ImportLongOperation extends Thread {

	private String path;
	private Display display;

	public ImportLongOperation(Display display, String path) {
		this.path = path;
		this.display = display;
	}

	public void run() {
		Store s = new Store();
		try {
			s.storeWikipediaFile(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		display.asyncExec(new Runnable() {
			public void run() {
				display.dispose();
			}
		});
	}
}
