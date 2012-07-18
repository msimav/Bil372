package client.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.UIManager;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JPasswordField;

import util.Utils;

import beans.User;

import client.Client;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginWindow extends JFrame {

	private JPanel contentPane;
	public JTextField emailText;
	private JPasswordField passwordField;
	public Client client;
	

	/**
	 * Create the frame.
	 */
	public LoginWindow(Client client) {
		
		try {
	        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName()
	            );
	    } catch (Exception e) { }
		
		this.client = client;
		setTitle("Login Window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 561, 174);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.controlHighlight);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel westPanel = new JPanel();
		centerPanel.add(westPanel, BorderLayout.WEST);
		
		JLabel lblNewLabel = new JLabel("                 ");
		westPanel.add(lblNewLabel);
		
		JPanel eastPanel = new JPanel();
		centerPanel.add(eastPanel, BorderLayout.EAST);
		
		JLabel lblNewLabel_1 = new JLabel("                                 ");
		eastPanel.add(lblNewLabel_1);
		
		JPanel center2 = new JPanel();
		centerPanel.add(center2, BorderLayout.CENTER);
		center2.setLayout(new GridLayout(2, 2, 5, 3));
		
		JLabel lblNewLabel_4 = new JLabel("Email :");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.RIGHT);
		center2.add(lblNewLabel_4);
		
		emailText = new JTextField();
		center2.add(emailText);
		emailText.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("Password :");
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.RIGHT);
		center2.add(lblNewLabel_5);
		
		passwordField = new JPasswordField();
		center2.add(passwordField);
		
		JPanel southPanel = new JPanel();
		contentPane.add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnNewButton = new JButton("Register");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				register();
			}
		});
		southPanel.add(btnNewButton);
		
		JButton btnRegister = new JButton("Login");
		btnRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				sendUser();								
			}
		});
		southPanel.add(btnRegister);
		
		JPanel northPanel = new JPanel();
		contentPane.add(northPanel, BorderLayout.NORTH);
		northPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNewLabel_2 = new JLabel("User Login");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		northPanel.add(lblNewLabel_2);
		setMaxSize();
	}
	
	public void sendUser() {
		
		if( emailText.getText() != null && passwordField.getPassword() != null )	{
			User user = new User(-1 , null , emailText.getText() , Utils.md5hash(new String(passwordField.getPassword())) , null  );		
			this.client.login(user);
		}
		else {
			JOptionPane.showMessageDialog(null, "Please enter your email and password correctly...");
		}
			
	}
	
	public void register() {
		this.client.getWindowHandler().openRegisterWindow();
		this.client.getWindowHandler().closeLoginWindow();		
	}
	
	public void setMaxSize() {
		Dimension maximumSize = new Dimension();
		maximumSize.height = 200;
		maximumSize.width = 600;
		this.setMaximumSize(maximumSize);
	}
}
