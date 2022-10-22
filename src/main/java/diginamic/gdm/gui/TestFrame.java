package diginamic.gdm.gui;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.springframework.boot.system.SystemProperties;

import diginamic.gdm.gui.buttons.actions.*;

public class TestFrame extends JFrame{

	/** serialVersionUID */
	private static final long serialVersionUID = 172295025018193286L;
	private static JFrame mainFrame;
	private JPanel contentPane;
	private String windowsTitle = "Gestion des missions";
	private FileSystem baseFile = FileSystems.getDefault();

	private TrayIcon trayIcon = null;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	SystemTray tray;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainFrame = new TestFrame();
					//frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestFrame() {	
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		setContentPane(contentPane);
		this.trayMode();
		this.createLoginForm();
	}
	public void createLoginForm() {
		
		
		JPanel loginForm = new JPanel();
		JTextField login = new JTextField();
		JTextField password = new JTextField();
		ActionListener loginAction = new loginAction(login, password);
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(loginAction);
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new CloseAction());
		loginForm.add(login);
		loginForm.add(password);
		loginForm.add(loginButton);
		loginForm.add(closeButton);
		// Adds Button to content pane of frame
		contentPane.add(loginForm);
		loginForm.setVisible(true);

		//mainFrame.setVisible(true);

	}

	/**
	 * si le système le supporte créer une icône dans le système tray
	 * 
	 */
	private void trayMode() {
		if (!SystemTray.isSupported()) {
			System.err.println("System tray is not supported !!! ");
			return;
		}
		tray = SystemTray.getSystemTray();

		// load an image
		
		System.out.println(System.getProperty("java.class.path"));
		//Path p = Paths.get(System.getProperty("java.class.path").toString());
		
		Path p = this.baseFile.getPath("F:\\dev\\projetFinFormationDIGINAMIC2022\\Gestion_Missions_BE\\src\\main\\resources\\public\\images\\logo.jpeg").normalize();
		p = this.baseFile.getPath("..\\src\\main\\resources\\public\\images\\logo.jpeg").normalize();
			
		System.out.println(p);
		
		System.out.println(p.getFileName());
		System.out.println(p.getParent());
		System.out.println(p.getRoot());
				
//		Image image = getIconImage() ;
		Image image = toolkit.getImage(p.toString());
				
		// create a action listener to listen for default action executed on the tray
		// icon
		ActionListener closingAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// execute default action of the application
				// notification 
				//trayIcon.displayMessage("Fermeture", new StringBuilder(" fermeture du programme demandée.\n ")
						//.append(" le programme vas se fermer").toString(), TrayIcon.MessageType.INFO);
				System.exit(0);
			}
		};

		ActionListener openAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(true);
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
		trayIcon.addActionListener(openAction);
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
