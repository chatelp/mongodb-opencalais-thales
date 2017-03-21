package main.gui;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import main.db.mongoDB.MongoDbConnector;
import main.gui.analyse.GuiAnalyser;
import main.gui.analyse.GuiAnalyserProgressBar;
import main.gui.exportation.EnumExport;
import main.gui.exportation.ExportDialog;
import main.gui.importation.Filter;
import main.gui.importation.ImportProgressBar;
import main.gui.overview.CollectionSelectListener;
import main.gui.overview.SpinnerListener;
import main.gui.proxy.ProxyDialog;
import main.gui.queries.BtnsQueryCreator;
import main.gui.queries.Field;
import main.gui.queries.GuiQuery;
import main.gui.queries.GuiQueryProgressBar;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ExpandAdapter;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jongo.Find;

import swing2swt.layout.BorderLayout;

public class Window {

	/**
	 * Attributes
	 */
	private static Find articles = null;
	// Menu and left part items
	private static Boolean connected = false;
	private static Button btnConnexion = null;
	private static Button btnAnalyse = null;
	private static Button btnExportRes = null;
	private static Button btnLaunchRequest = null;
	private static MenuItem mntmDconnexion = null;
	private static MenuItem mntmConnexion = null;
	private static MenuItem mntmExport = null;
	private static MenuItem mntmImport = null;
	private static org.eclipse.swt.widgets.List list = null;
	private static Label lblDocument = null;
	// Center items
	private static TabFolder tabFolder = null;
	// Table items
	private static Label lblQuery = null;
	private static TableViewer tableViewer = null;
	private static TableColumnLayout tcl_composite = null;
	private static Table table = null;
	// Pagination items
	private static Label lblSur = null;
	private static Spinner spinner = null;
	// Queries items
	private static ScrolledComposite scrolledComposite_queriesBtn = null;
	private static List<Field> fields = null;
	// Analyse items
	private static Composite compositeAnalyse = null;
	private static ScrolledComposite scrolledComposite_2;

	/**
	 * Getters and setters
	 */
	public static Find getArticles() {
		return articles;
	}

	public static void setArticles(Find articles) {
		Window.articles = articles;
	}

	public static Boolean getConnected() {
		return connected;
	}

	public static org.eclipse.swt.widgets.List getList() {
		return list;
	}

	public static Label getLblDocument() {
		return lblDocument;
	}

	public static Button getBtnLaunchRequest() {
		return btnLaunchRequest;
	}

	public static TableViewer getTableViewer() {
		return tableViewer;
	}

	public static TableColumnLayout getTcl_composite() {
		return tcl_composite;
	}

	public static Table getTable() {
		return table;
	}

	public static Label getLblSur() {
		return lblSur;
	}

	public static Spinner getSpinner() {
		return spinner;
	}

	public static ScrolledComposite getScrolledComposite_queriesBtn() {
		return scrolledComposite_queriesBtn;
	}

	public static void setConnected(Boolean connected) {
		Window.connected = connected;
	}

	public static void setLblDocument(Label label) {
		Window.lblDocument = label;
	}

	public static void setFields(List<Field> fields) {
		Window.fields = fields;
	}

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());
	private Text text;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Window window = new Window();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1024, 700);
		shell.setText("Big Data");

		createMenu();
		createLeft();
		createCenter();
	}

	/**
	 * Create menu.
	 */
	protected void createMenu() {
		shell.setLayout(new BorderLayout(0, 0));
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmConnexionSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmConnexionSubmenu.setText("Connexion");

		Menu menu_1 = new Menu(mntmConnexionSubmenu);
		mntmConnexionSubmenu.setMenu(menu_1);

		mntmConnexion = new MenuItem(menu_1, SWT.NONE);
		mntmConnexion.addSelectionListener(new ConnectionBtnListener(shell));
		mntmConnexion.setText("Connexion \u00E0 MongoDB");

		mntmDconnexion = new MenuItem(menu_1, SWT.NONE);
		mntmDconnexion.addSelectionListener(new ConnectionBtnListener(shell));
		mntmDconnexion.setEnabled(false);
		mntmDconnexion.setText("D\u00E9connexion");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		mntmExit.setText("Quitter");

		MenuItem mntmFileSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmFileSubmenu.setText("Fichiers");

		Menu menu_2 = new Menu(mntmFileSubmenu);
		mntmFileSubmenu.setMenu(menu_2);

		mntmImport = new MenuItem(menu_2, SWT.CASCADE);
		mntmImport.setText("Importer");
		mntmImport.setEnabled(false);

		Menu menu_4 = new Menu(mntmImport);
		mntmImport.setMenu(menu_4);

		MenuItem mntmImportDumpWikipedia = new MenuItem(menu_4, SWT.NONE);
		mntmImportDumpWikipedia.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shell);
				fd.setText("Ouvrir un dump Wikipedia");
				String[] filterExt = { "*.xml", "*.bz2", "*.zip", "*.rar" };
				fd.setFilterExtensions(filterExt);
				String path = fd.open();

				new Filter(shell, SWT.SIMPLE).open();

				ImportProgressBar ipb = new ImportProgressBar(shell, SWT.NONE);
				ipb.open(path);
			}
		});
		mntmImportDumpWikipedia.setText("Dump Wikipedia");

		mntmExport = new MenuItem(menu_2, SWT.CASCADE);
		mntmExport.setEnabled(false);
		mntmExport.setText("Exporter");

		Menu menu_5 = new Menu(mntmExport);
		mntmExport.setMenu(menu_5);

		MenuItem mntmExportAllDb = new MenuItem(menu_5, SWT.NONE);
		mntmExportAllDb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ExportDialog ed = new ExportDialog(shell, SWT.SIMPLE,
						EnumExport.ALL);
				ed.open();
			}
		});
		mntmExportAllDb.setText("Toute la collection");

		MenuItem mntmExportRsultsLastQuery = new MenuItem(menu_5, SWT.NONE);
		mntmExportRsultsLastQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ExportDialog ed = new ExportDialog(shell, SWT.SIMPLE,
						EnumExport.LAST);
				ed.open();
			}
		});
		mntmExportRsultsLastQuery
				.setText("R\u00E9sultats de la derni\u00E8re requ\u00EAte");

		MenuItem mntmOptionSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmOptionSubmenu.setText("Options");

		Menu menu_3 = new Menu(mntmOptionSubmenu);
		mntmOptionSubmenu.setMenu(menu_3);

		MenuItem mntmConfigureProxy = new MenuItem(menu_3, SWT.NONE);
		mntmConfigureProxy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProxyDialog pd = new ProxyDialog(shell, SWT.SIMPLE);
				pd.open();
			}
		});
		mntmConfigureProxy.setText("Configurer un proxy");

	}

	protected void createLeft() {
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(BorderLayout.WEST);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(2, false));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		btnConnexion = new Button(composite, SWT.NONE);
		btnConnexion.addSelectionListener(new ConnectionBtnListener(shell));
		GridData gd_btnConnexion = new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 2, 1);
		gd_btnConnexion.widthHint = 120;
		gd_btnConnexion.heightHint = 53;
		btnConnexion.setLayoutData(gd_btnConnexion);
		formToolkit.adapt(btnConnexion, true, true);
		btnConnexion.setText("Connexion");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL
				| SWT.CENTER);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 2,
				1);
		gd_label.widthHint = 197;
		label.setLayoutData(gd_label);
		formToolkit.adapt(label, true, true);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Label lblCollections = new Label(composite, SWT.NONE);
		formToolkit.adapt(lblCollections, true, true);
		lblCollections.setText("Collections:");
		new Label(composite, SWT.NONE);

		list = new org.eclipse.swt.widgets.List(composite, SWT.BORDER
				| SWT.V_SCROLL);
		list.addSelectionListener(new CollectionSelectListener());

		list.setToolTipText("");
		GridData gd_list = new GridData(SWT.FILL, SWT.CENTER, false, false, 2,
				1);
		gd_list.widthHint = 153;
		list.setLayoutData(gd_list);
		formToolkit.adapt(list, true, true);

		lblDocument = new Label(composite, SWT.NONE);
		lblDocument.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 2, 1));
		formToolkit.adapt(lblDocument, true, true);
		lblDocument.setText("0 document");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				2, 1));
		formToolkit.adapt(label_1, true, true);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		btnAnalyse = new Button(composite, SWT.NONE);
		btnAnalyse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GuiAnalyserProgressBar gapb = new GuiAnalyserProgressBar(shell,
						SWT.NONE);
				gapb.open();

				GuiAnalyser ga = GuiAnalyser.getInstance();
				ga.createChartElements(compositeAnalyse, formToolkit);
				ga.createArrayElements(compositeAnalyse, formToolkit);
				scrolledComposite_2.setContent(compositeAnalyse);
				scrolledComposite_2.setMinSize(compositeAnalyse.computeSize(
						SWT.DEFAULT, SWT.DEFAULT));

				tabFolder.setSelection(2);
				// Update list
				updateList();

			}
		});
		btnAnalyse.setEnabled(false);
		GridData gd_btnAnalyse = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 2, 1);
		gd_btnAnalyse.heightHint = 48;
		btnAnalyse.setLayoutData(gd_btnAnalyse);
		formToolkit.adapt(btnAnalyse, true, true);
		btnAnalyse.setText("Analyser");
	}

	/**
	 * Create center.
	 */
	protected void createCenter() {
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(BorderLayout.CENTER);
		formToolkit.adapt(tabFolder);
		formToolkit.paintBordersFor(tabFolder);

		createTabOverview(tabFolder);
		createTabRequests(tabFolder);
		createTabAnalyse(tabFolder);
	}

	protected void createTabOverview(TabFolder tabFolder) {
		TabItem tbtmOverviewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmOverviewItem.setText("Aper\u00E7u");

		final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmOverviewItem.setControl(composite_1);
		formToolkit.paintBordersFor(composite_1);
		composite_1.setLayout(new GridLayout(1, true));

		ExpandBar expandBar = new ExpandBar(composite_1, SWT.NONE);
		expandBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				3, 1));
		formToolkit.adapt(expandBar);
		formToolkit.paintBordersFor(expandBar);

		/*
		 * Problem: with a GridLayout, the "expandable area" is always shown, no
		 * matter if you collapse it or not (sick !) Solution: ask the composite
		 * containing the GridLayout to do its layout again any time that
		 * theExpandBar expands or collapses
		 */
		expandBar.addExpandListener(new ExpandAdapter() {

			@Override
			public void itemCollapsed(ExpandEvent e) {
				Display.getCurrent().asyncExec(new Runnable() {
					public void run() {
						composite_1.layout();
					}
				});
			}

			@Override
			public void itemExpanded(ExpandEvent e) {
				Display.getCurrent().asyncExec(new Runnable() {
					public void run() {
						composite_1.layout();
					}
				});
			}
		});

		ExpandItem xpndtmNewExpanditem = new ExpandItem(expandBar, SWT.NONE);
		xpndtmNewExpanditem.setExpanded(true);
		xpndtmNewExpanditem.setText("Requ\u00EAte");

		ScrolledComposite scrolledComposite = new ScrolledComposite(expandBar,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		xpndtmNewExpanditem.setControl(scrolledComposite);
		formToolkit.adapt(scrolledComposite);
		formToolkit.paintBordersFor(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		xpndtmNewExpanditem.setHeight(40);

		lblQuery = new Label(scrolledComposite, SWT.NONE);
		formToolkit.adapt(lblQuery, true, true);
		lblQuery.setText("Aucune requ\u00EAte ne filtre actuellement les documents de la base de donn\u00E9es.");
		scrolledComposite.setContent(lblQuery);
		scrolledComposite.setMinSize(lblQuery.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		Composite composite = new Composite(composite_1, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, false, false,
				3, 1);
		gd_composite.heightHint = 497;
		composite.setLayoutData(gd_composite);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		tcl_composite = new TableColumnLayout();
		composite.setLayout(tcl_composite);

		tableViewer = new TableViewer(composite, SWT.BORDER
				| SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(false);
		formToolkit.paintBordersFor(table);

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite_2.widthHint = 127;
		gd_composite_2.heightHint = 21;
		composite_2.setLayoutData(gd_composite_2);
		formToolkit.adapt(composite_2);
		formToolkit.paintBordersFor(composite_2);
		FillLayout fl_composite_2 = new FillLayout(SWT.HORIZONTAL);
		fl_composite_2.spacing = 5;
		composite_2.setLayout(fl_composite_2);

		spinner = new Spinner(composite_2, SWT.BORDER | SWT.READ_ONLY);
		spinner.setTextLimit(999999);
		spinner.setMaximum(1);
		spinner.setMinimum(1);
		formToolkit.adapt(spinner);
		formToolkit.paintBordersFor(spinner);
		spinner.addModifyListener(new SpinnerListener());

		lblSur = new Label(composite_2, SWT.SHADOW_NONE | SWT.CENTER);
		lblSur.setAlignment(SWT.LEFT);
		formToolkit.adapt(lblSur, true, true);
		lblSur.setText("sur 1");
	}

	private void createTabRequests(final TabFolder tabFolder) {
		TabItem tbtmRequte = new TabItem(tabFolder, SWT.NONE);
		tbtmRequte.setText("Requ\u00EAte");

		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmRequte.setControl(composite_2);
		formToolkit.paintBordersFor(composite_2);
		composite_2.setLayout(new GridLayout(2, false));

		Label lblCrateurDeRequtes = new Label(composite_2, SWT.NONE);
		GridData gd_lblCrateurDeRequtes = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_lblCrateurDeRequtes.widthHint = 115;
		lblCrateurDeRequtes.setLayoutData(gd_lblCrateurDeRequtes);
		formToolkit.adapt(lblCrateurDeRequtes, true, true);
		lblCrateurDeRequtes.setText("Cr\u00E9ateur de requ\u00EAtes:");
		new Label(composite_2, SWT.NONE);

		scrolledComposite_queriesBtn = new ScrolledComposite(composite_2,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_scrolledComposite_1 = new GridData(SWT.FILL, SWT.FILL,
				true, true, 2, 1);
		gd_scrolledComposite_1.heightHint = 365;
		scrolledComposite_queriesBtn.setLayoutData(gd_scrolledComposite_1);
		formToolkit.adapt(scrolledComposite_queriesBtn);
		formToolkit.paintBordersFor(scrolledComposite_queriesBtn);
		scrolledComposite_queriesBtn.setExpandHorizontal(true);
		scrolledComposite_queriesBtn.setExpandVertical(true);

		Button btnGnrerLaRequte = new Button(composite_2, SWT.NONE);
		GridData gd_btnGnrerLaRequte = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_btnGnrerLaRequte.widthHint = 161;
		btnGnrerLaRequte.setLayoutData(gd_btnGnrerLaRequte);
		formToolkit.adapt(btnGnrerLaRequte, true, true);
		btnGnrerLaRequte.setText("G\u00E9n\u00E9rer la requ\u00EAte");
		new Label(composite_2, SWT.NONE);

		Label label = new Label(composite_2, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2,
				1));
		formToolkit.adapt(label, true, true);

		text = new Text(composite_2, SWT.BORDER | SWT.V_SCROLL);
		btnGnrerLaRequte.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO
				GuiQuery q = GuiQuery.getInstance();
				text.setText(q.createQuery(fields));
			}
		});
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_text.heightHint = 60;
		text.setLayoutData(gd_text);
		formToolkit.adapt(text, true, true);

		Composite composite = new Composite(composite_2, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 2, 1);
		gd_composite.heightHint = 67;
		composite.setLayoutData(gd_composite);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);

		btnLaunchRequest = new Button(composite, SWT.NONE);
		btnLaunchRequest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				GuiQuery q = GuiQuery.getInstance();
				q.setRequest(text.getText());

				GuiQueryProgressBar gqpb = new GuiQueryProgressBar(shell,
						SWT.NONE);
				gqpb.open();

				try {
					GuiQuery.getInstance().saveQueryResult(articles);
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				lblQuery.setText(GuiQuery.getInstance().getRequest());

				tabFolder.setSelection(0);
			}
		});

		btnLaunchRequest.setEnabled(false);
		formToolkit.adapt(btnLaunchRequest, true, true);
		btnLaunchRequest.setText("Lancer la requ\u00EAte");

		Button btnNewButton_4 = new Button(composite, SWT.NONE);
		btnNewButton_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
				BtnsQueryCreator.getInstance().deleteBtnQuery();
				BtnsQueryCreator.getInstance().createBtns();
			}
		});
		formToolkit.adapt(btnNewButton_4, true, true);
		btnNewButton_4
				.setText("R\u00E9initialiser les champs de la requ\u00EAte");

	}

	private void createTabAnalyse(TabFolder tabFolder) {
		TabItem tbtmAnalyse = new TabItem(tabFolder, SWT.NONE);
		tbtmAnalyse.setText("Analyse");

		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmAnalyse.setControl(composite_3);
		formToolkit.paintBordersFor(composite_3);
		composite_3.setLayout(new GridLayout(1, true));

		Label lblRsultatsDeLanalyse = new Label(composite_3, SWT.NONE);
		lblRsultatsDeLanalyse.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		formToolkit.adapt(lblRsultatsDeLanalyse, true, true);
		lblRsultatsDeLanalyse.setText("R\u00E9sultats de l'analyse:");

		scrolledComposite_2 = new ScrolledComposite(composite_3, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_scrolledComposite_2 = new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1);
		gd_scrolledComposite_2.heightHint = 531;
		scrolledComposite_2.setLayoutData(gd_scrolledComposite_2);
		formToolkit.adapt(scrolledComposite_2);
		formToolkit.paintBordersFor(scrolledComposite_2);
		scrolledComposite_2.setExpandHorizontal(true);
		scrolledComposite_2.setExpandVertical(true);

		compositeAnalyse = new Composite(scrolledComposite_2, SWT.NONE);
		formToolkit.adapt(compositeAnalyse);
		formToolkit.paintBordersFor(compositeAnalyse);
		compositeAnalyse.setLayout(new GridLayout(3, false));

		new Label(compositeAnalyse, SWT.NONE);
		new Label(compositeAnalyse, SWT.NONE);
		new Label(compositeAnalyse, SWT.NONE);

		btnExportRes = new Button(composite_3, SWT.NONE);
		btnExportRes.setEnabled(false);
		GridData gd_btnExportRes = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1);
		gd_btnExportRes.heightHint = 35;
		btnExportRes.setLayoutData(gd_btnExportRes);
		formToolkit.adapt(btnExportRes, true, true);
		btnExportRes.setText("Exporter les r\u00E9sultats");
		btnExportRes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ExportDialog ed = new ExportDialog(shell, SWT.NORMAL,
						EnumExport.ANALYSE);
				ed.open();
			}
		});
	}

	/**
	 * Display or disable all clickable buttons
	 */
	public static void enableButtons() {
		btnAnalyse.setEnabled(!btnAnalyse.getEnabled());
		btnExportRes.setEnabled(!btnExportRes.getEnabled());
		mntmDconnexion.setEnabled(!mntmDconnexion.getEnabled());
		mntmConnexion.setEnabled(!mntmConnexion.getEnabled());
		mntmImport.setEnabled(!mntmImport.getEnabled());
		mntmExport.setEnabled(!mntmExport.getEnabled());

		if (btnLaunchRequest.getEnabled())
			btnLaunchRequest.setEnabled(false);

		if (!connected)
			btnConnexion.setText("Déconnexion");
		else
			btnConnexion.setText("Connexion");

		connected = !connected;
	}

	public static Label getLblQuery() {
		return lblQuery;
	}

	/**
	 * Update the list of collection
	 */
	private void updateList() {
		try {
			list.removeAll();
			Set<String> collections = MongoDbConnector.getDB()
					.getCollectionNames();
			Iterator<String> i = collections.iterator();
			while (i.hasNext())
				list.add(i.next().toString());
			list.pack();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
