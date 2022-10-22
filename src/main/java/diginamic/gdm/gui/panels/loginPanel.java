package diginamic.gdm.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class loginPanel extends JPanel {
	/** serialVersionUID */
	private static final long serialVersionUID = 6881765115453335497L;
	
	private JTextField textFieldUsername;
	private JTextField textFieldCredential;
	
	/**
	 * Create the panel.
	 */
	public loginPanel() {
		
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("username :" + textFieldUsername.getText());
				System.out.println("motdepasse :" + textFieldCredential.getText());
			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnLogin.setBounds(320, 258, 103, 41);
		add(btnLogin);
		
		JLabel lblUsername = new JLabel("username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblUsername.setForeground(new Color(255, 255, 255));
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setLabelFor(textFieldUsername);
		lblUsername.setBounds(24, 77, 143, 32);
		add(lblUsername);
		
		JLabel lblPassword = new JLabel("password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPassword.setForeground(new Color(255, 255, 255));
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setLabelFor(textFieldCredential);
		lblPassword.setBounds(24, 144, 143, 34);
		add(lblPassword);
		setBackground(new Color(0, 0, 0));
		setForeground(new Color(0, 0, 0));
		setBounds(10, 11, 451, 335);
		setLayout(null);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setToolTipText("nom d'utilisateur");
		textFieldUsername.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldUsername.setBounds(177, 77, 246, 35);
		add(textFieldUsername);
		textFieldUsername.setColumns(30);
		
		textFieldCredential = new JTextField();
		textFieldCredential.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldCredential.setBounds(177, 144, 246, 35);
		add(textFieldCredential);
		textFieldCredential.setToolTipText("mot de passe");
		textFieldCredential.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldCredential.setColumns(30);
	}
}
