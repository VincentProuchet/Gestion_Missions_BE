package diginamic.gdm.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import diginamic.gdm.gui.panels.loginPanel;

public class MainFrame extends JFrame {
	/** serialVersionUID */
	private static final long serialVersionUID = 172295025018193286L;

	
	
	private JPanel contentPane;
	

	

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setForeground(new Color(0, 0, 0));
		setTitle("Gestion des missions");
		
		setBounds(100, 100, 486, 551);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton CloseBtn = new JButton("Close program");
		menuBar.add(CloseBtn);
		CloseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		CloseBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane = new JPanel();
		contentPane.setForeground(new Color(255, 255, 255));
		contentPane.setBackground(SystemColor.desktop);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel login = new loginPanel();
		contentPane.add(login);
	}
	


}
