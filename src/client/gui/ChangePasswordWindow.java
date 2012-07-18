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

import util.Utils;

import beans.User;

import client.Client;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
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
		passwordField_2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_ENTER) {
					changePassword();
				}
			}
		});
		panel.add(passwordField_2);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				close();
			}
		});
		panel.add(cancelButton);
		
		JButton changePassword = new JButton("Change Password");
		changePassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				changePassword();
			}
		});
		panel.add(changePassword);
	}
	
	public void close() {
		this.client.getWindowHandler().closeChangePasswordWindow();
	}
	
	public void changePassword() {
		if( passwordField.getPassword() != null && passwordField_1.getPassword() != null && passwordField_2.getPassword() != null ) {
			if( this.client.getUser().getPasswd().equals(Utils.md5hash( new String(passwordField.getPassword()))) && new String(passwordField_1.getPassword()).equals(new String(passwordField_2.getPassword()))  ) {
				User updatedUser = new User(this.client.getUser().getId(), this.client.getUser().getName(), this.client.getUser().getEmail(), Utils.md5hash(new String(passwordField_1.getPassword())), this.client.getUser().getAvatar());
				this.client.updatePasswd(updatedUser);
			}
		}
		
	}

}
