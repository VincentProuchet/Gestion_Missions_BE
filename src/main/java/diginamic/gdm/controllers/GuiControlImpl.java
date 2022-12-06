package diginamic.gdm.controllers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import diginamic.gdm.gui.MainFrame;
import diginamic.gdm.gui.tray.Tray;
import diginamic.gdm.services.CollaboratorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * controller GUI
 * fait pour que le programme ait un client local sur le serveur
 * @author Vincent
 *
 */
@Controller
@AllArgsConstructor
public class GuiControlImpl implements GuiControl {
	@Autowired
	private CollaboratorService userService;

	/** fenêtre principale de l'application  */
	private static MainFrame mainWindow;
	/**
	 * partie de l'application mise dans la zone de notification
	 */
	private static Tray apptray;

	/**
	 *
	 */

	@PostConstruct
	public void initService() {
		try {
			mainWindow = new MainFrame(this);
			mainWindow.setVisible(true);
			
		} catch (Exception e) {
			System.err.println("system can't support GUI");
		}
		apptray = Tray.getTray(mainWindow);

	}

	@Override
	public void closeProgram() {
		System.exit(0);

	}

	@Override
	public void logOutUser() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loginUser(String userName, String password) {
		// TODO Auto-generated method stub

	}

}
