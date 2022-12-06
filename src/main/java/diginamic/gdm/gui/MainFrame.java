package diginamic.gdm.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import diginamic.gdm.controllers.GuiControl;
import diginamic.gdm.gui.panels.loginPanel;


/**
 * application's mainframe
 * please avoid editing class to remove text in code
 * its supposed to b controlled by an IDE plugin
 * and removing them would break this  
 * but you can do it on another branch
 * were application GUI design is considered complete  
 *  
 * @author Vincent
 *
 */
public class MainFrame extends JFrame {
	/** serialVersionUID */
	private static final long serialVersionUID = 172295025018193286L;

	private GuiControl control;
	private JPanel contentPane;

	public MainFrame(GuiControl gui) {
		this.control = gui;
		this.createFrame();
	}
	/**
	 * Create the frame.
	 */
	private void createFrame() {
		setForeground(new Color(0, 0, 0));
		setTitle("Gestion des missions");

		setBounds(100, 100, 486, 551);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JButton CloseBtn = new JButton("Close program");
		menuBar.add(CloseBtn);
		CloseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				control.closeProgram();
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
	/**
	 * SETTER
	 * for the action control
	 * it MUST be a GinControlInterface Implementation
	 * @param control
	 */
	public void setAction(GuiControl control) {
		this.control = control;
	}


}
