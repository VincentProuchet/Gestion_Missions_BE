package diginamic.gdm.gui.buttons.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import lombok.Getter;

@Getter

public class loginAction implements ActionListener {

	private JTextField login = null;
	private JTextField password = null;

	@Override
	public void actionPerformed(ActionEvent e) {
		String text = new StringBuilder("login action\n")
				.append("login :")
				.append(login.getText())
				.append("\n")

				.append("Password :")
				.append(password.getText())
				.append("\n")

				.toString();

		System.out.println(text);
		this.login.setText("");
		this.password.setText("");
	}

	public loginAction(JTextField login, JTextField password) {
		this.login = login;
		this.password = password;
	}



}
