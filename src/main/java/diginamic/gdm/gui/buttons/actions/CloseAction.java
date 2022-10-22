package diginamic.gdm.gui.buttons.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import diginamic.gdm.gui.TestFrame;

public class CloseAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("system exit");
		System.exit(0);
	}

}
