package diginamic.gdm.gui.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import diginamic.gdm.gui.MainFrame;

public class Tray {

	private static Tray self = null;

	private MainFrame mainWindow;
	private String windowsTitle = "Gestion des missions";
	private FileSystem baseFile = FileSystems.getDefault();

	private TrayIcon trayIcon = null;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private SystemTray tray;

	private Tray(MainFrame main) {
		this.mainWindow = main;
		this.trayMode();
	}

	public static Tray getTray(MainFrame main) {
		if (self != null) {
			return self;
		}
		return new Tray(main);
	}

	private void trayMode() {
		if (!SystemTray.isSupported()) {
			System.err.println("System tray is not supported !!! ");
			return;
		}
		tray = SystemTray.getSystemTray();

		// load an image

		System.out.println(System.getProperty("java.class.path"));
		// Path p = Paths.get(System.getProperty("java.class.path").toString());

		Path p = this.baseFile.getPath(
				"F:\\dev\\projetFinFormationDIGINAMIC2022\\Gestion_Missions_BE\\src\\main\\resources\\public\\images\\logo.jpeg")
				.normalize();
		p = this.baseFile.getPath("..\\src\\main\\resources\\public\\images\\logo.jpeg").normalize();

		System.out.println(p);

		System.out.println(p.getFileName());
		System.out.println(p.getParent());
		System.out.println(p.getRoot());

//		Image image = getIconImage() ;
		Image image = toolkit.getImage(p.toAbsolutePath().toString());

		// create a action listener to listen for default action executed on the tray
		// icon
		ActionListener closingAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// execute default action of the application
				// notification
				// trayIcon.displayMessage("Fermeture", new StringBuilder(" fermeture du
				// programme demandée.\n ")
				// .append(" le programme vas se fermer").toString(),
				// TrayIcon.MessageType.INFO);
				System.exit(0);
			}
		};

		ActionListener openAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainWindow.setVisible(true);
			}
		};

		ActionListener toggleAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainWindow.setVisible(!mainWindow.isVisible());
			}
		};
		// create a popup menu
		PopupMenu popup = new PopupMenu();
		// create menu item for the default action
		MenuItem menuOpen = new MenuItem("Ouvrir le fenêtre");
		menuOpen.addActionListener(openAction);
		MenuItem menuExit = new MenuItem("fermer le programme");
		menuExit.addActionListener(closingAction);

		popup.add(menuOpen);
		popup.add(menuExit);
		/// ... add other items
		// construct a TrayIcon
		trayIcon = new TrayIcon(image, this.windowsTitle, popup);
		// set the TrayIcon properties
		trayIcon.setImageAutoSize(true);
		trayIcon.addActionListener(toggleAction);
		// ...
		// add the tray image
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.err.println(e);
		}
		// ...

	}
}
