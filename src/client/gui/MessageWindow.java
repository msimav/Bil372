package client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

import client.Client;

import beans.PrivateMessage;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class MessageWindow extends JFrame {

	public Client client;
	private JPanel contentPane;
	private JPanel panel_1;
	private JPanel panel_2;
	JPanel panel;
	JScrollPane scrollPane;
	

	/**
	 * Create the frame.
	 */
	public MessageWindow(Client client) {
		
		try {
	        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName()
	            );
	    } catch (Exception e) { }
		
		this.client = client;
		
		setTitle("Messages");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 281, 500);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 20));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Messages");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel, BorderLayout.NORTH);
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane , BorderLayout.CENTER); 
		
		panel = new JPanel();
		panel.setBorder(null);
		panel.setBackground(Color.WHITE);
		scrollPane.setViewportView(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JButton btnNewMessage = new JButton("New Message");
		btnNewMessage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				newMessage();
			}
		});
		contentPane.add(btnNewMessage, BorderLayout.SOUTH);
		
		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		contentPane.add(panel_1, BorderLayout.WEST);
		
		panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		contentPane.add(panel_2, BorderLayout.EAST);
	}
	
	public void addMessageComponent( PrivateMessage message ) {
		MessageHeaderPanel newPanel = new MessageHeaderPanel(this.client);
		newPanel.userName.setText(message.getFrom().getName());
		newPanel.lastMessageLbl.setText(message.getMessage());
		newPanel.dateLabel.setText(message.getDate());
		newPanel.from = message.getFrom();
		
		Dimension size = new Dimension();
		size.height = 70;
		size.width = scrollPane.getWidth() - 20;
		newPanel.setMaximumSize(size);
		newPanel.setMinimumSize(size);
		newPanel.setPreferredSize(size);
		
		panel.add(newPanel);
		panel.revalidate();
		
	}
	
	public void addMessage() {		
		this.client.getWindowHandler().openPmWindow();
		this.client.getWindowHandler().closeMessageWindow();
	}
	
	public void newMessage() {
		this.client.listUsers();
	}

}
