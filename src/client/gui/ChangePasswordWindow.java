package client.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

import client.Client;

public class ChangePasswordWindow extends JFrame {

	private JPanel contentPane;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JPasswordField passwordField_2;
	public Client client;

	/**
	 * Create the frame.
	 */
	public ChangePasswordWindow(Client client) {
		
		try {
	        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName()
	            );
	    } catch (Exception e) { }
		
		this.client = client;
		
		setResizable(false);
		setTitle("Change Password");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 460, 144);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(4, 2, 0, 0));
		
		JLabel lblOldPassword = new JLabel("Old Password :");
		panel.add(lblOldPassword);
		
		passwordField = new JPasswordField();
		panel.add(passwordField);
		
		JLabel lblNewLabel_1 = new JLabel("New Password:");
		panel.add(lblNewLabel_1);
		
		passwordField_1 = new JPasswordField();
		panel.add(passwordField_1);
		
		JLabel lblNewLabel_2 = new JLabel("New Password( Again ) :");
		panel.add(lblNewLabel_2);
		
		passwordField_2 = new JPasswordField();
		panel.add(passwordField_2);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		panel.add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("Change Password");
		panel.add(btnNewButton);
	}

}
