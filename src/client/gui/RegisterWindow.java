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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RegisterWindow extends JFrame {

	private JPanel contentPane;
	private JTextField nameText;
	private JTextField emailText;
	private JPasswordField passwordField;
	public Client client;
	
	
	/**
	 * Create the frame.
	 */
	public RegisterWindow(Client client) {
		
		try {
	        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName()
	            );
	    } catch (Exception e) { }
		
		this.client = client;
		setTitle("Register Window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 559, 186);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel westPanel = new JPanel();
		westPanel.setBackground(Color.WHITE);
		centerPanel.add(westPanel, BorderLayout.WEST);
		
		JLabel lblNewLabel = new JLabel("                 ");
		westPanel.add(lblNewLabel);
		
		JPanel eastPanel = new JPanel();
		eastPanel.setBackground(Color.WHITE);
		centerPanel.add(eastPanel, BorderLayout.EAST);
		
		JLabel lblNewLabel_1 = new JLabel("                                                   ");
		eastPanel.add(lblNewLabel_1);
		
		JPanel center2 = new JPanel();
		center2.setBackground(Color.WHITE);
		centerPanel.add(center2, BorderLayout.CENTER);
		center2.setLayout(new GridLayout(3, 2, 5, 3));
		
		JLabel lblNewLabel_3 = new JLabel("Name Surname :");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);
		center2.add(lblNewLabel_3);
		
		nameText = new JTextField();
		nameText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_ENTER) {
					register();
				}
			}
		});
		center2.add(nameText);
		nameText.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Email :");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.RIGHT);
		center2.add(lblNewLabel_4);
		
		emailText = new JTextField();
		emailText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_ENTER) {
					register();
				}
			}
		});
		center2.add(emailText);
		emailText.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("Password :");
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.RIGHT);
		center2.add(lblNewLabel_5);
		
		passwordField = new JPasswordField();
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_ENTER) {
					register();
				}
			}
		});
		center2.add(passwordField);
		
		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.WHITE);
		contentPane.add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnNewButton = new JButton("Back");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				goBack();
			}
		});
		southPanel.add(btnNewButton);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				register();
			}
		});
		southPanel.add(btnRegister);
		
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.WHITE);
		contentPane.add(northPanel, BorderLayout.NORTH);
		northPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNewLabel_2 = new JLabel("User Register");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		northPanel.add(lblNewLabel_2);
	}
	
	public void register() {
		if( this.nameText.getText() != null  && this.emailText.getText() != null && passwordField.getPassword() != null ) {
			User newUser = new User(-1 , this.nameText.getText() , this.emailText.getText() ,Utils.md5hash( new String(passwordField.getPassword()) ) ,null );
			this.client.register(newUser);
		}
		else {
			JOptionPane.showMessageDialog(null, "Please fill out the fields correctly...");
		}
		
	}
	
	public void goBack() {
		this.client.getWindowHandler().openLoginWindow();
		this.client.getWindowHandler().closeRegisterWindow();		
	}
}
