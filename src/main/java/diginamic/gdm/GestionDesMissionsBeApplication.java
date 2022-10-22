package diginamic.gdm;

import java.awt.AWTException;
import java.awt.EventQueue;
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import diginamic.gdm.gui.MainFrame;
import diginamic.gdm.gui.TestFrame;
import diginamic.gdm.gui.tray.Tray;


@SpringBootApplication
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@EnableScheduling
public class GestionDesMissionsBeApplication {
	
	private static MainFrame mainWindow;
	private static Tray apptray; 
	
	public static void main(String[] args) {
		
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						mainWindow = new MainFrame();						
						mainWindow.setVisible(true);						
						apptray = Tray.getTray(mainWindow);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			SpringApplication.run(GestionDesMissionsBeApplication.class, args);
		}
	

		
	}
	
	

	


