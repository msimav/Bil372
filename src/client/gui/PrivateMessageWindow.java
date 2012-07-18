package client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.DropMode;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import util.Utils;

import client.Client;

import beans.PrivateMessage;
import beans.User;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class PrivateMessageWindow extends JFrame {

	private JPanel contentPane;
	private JScrollPane scrollPane_1;
	private JPanel centerPanel ;
	public User from;
	private JTextArea message;
	public Client client;
	public JLabel fromLabel;
	/**
	 * Create the frame.
	 */
	public PrivateMessageWindow(Client client) {
		setTitle("Private Message");
		
		try {
	        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName()
	            );
	    } catch (Exception e) { }
		
		this.client = client;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 385, 536);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);
		
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.WHITE);
		contentPane.add(northPanel, BorderLayout.NORTH);
		
		fromLabel = new JLabel("Umut Ozan Y\u0131ld\u0131r\u0131m");
		fromLabel.setFont(new Font("Calibri", Font.BOLD, 17));
		northPanel.add(fromLabel);
		
		scrollPane_1 = new JScrollPane();
		contentPane.add(scrollPane_1 , BorderLayout.CENTER);
		
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		scrollPane_1.setViewportView(centerPanel);
		
		
		JPanel southPanel = new JPanel();
		contentPane.add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		message = new JTextArea();
		message.setFont(new Font("Segoe UI", Font.BOLD, 9));
		message.setRows(4);
		
		JScrollPane scrollPane = new JScrollPane();
		southPanel.add(scrollPane);
		scrollPane.setViewportView(message);
		
		JPanel panel = new JPanel();
		southPanel.add(panel);
		
		JButton btnNewButton = new JButton("Send Message");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				sendMessage();
			}
		});
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnNewButton_1 = new JButton("Back");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				goBack();
			}
		});
		panel.add(btnNewButton_1);
		panel.add(btnNewButton);
	}
	
	public void addMessage( PrivateMessage message ) {
		PrivateMessagePanel newComp = new PrivateMessagePanel();
		newComp.message.setText(message.getMessage());
		newComp.dateLabel.setText(message.getDate());
		newComp.userName.setText(message.getFrom().getName());
		
		Dimension maximumSize = new Dimension();
		maximumSize.width = scrollPane_1.getWidth() - 20;
		
		if( newComp.message.getLineCount() * 20 > 115 ) 
			maximumSize.height = newComp.message.getLineCount() * 20 + 20 ;
		else
			maximumSize.height = 115;
		
		
		newComp.setMinimumSize(maximumSize);
		newComp.setPreferredSize(maximumSize);

		centerPanel.add(newComp);
		centerPanel.revalidate();
	}
	
	public void clearMessages() {
		centerPanel.removeAll();
		centerPanel.revalidate();
	}
	
	public void sendMessage() {
		if( message.getText() != null ) {
			PrivateMessage pm = new PrivateMessage(-1 , from , this.client.getUser() ,Utils.getDate() , message.getText());
			this.client.sendPrivateMessage(pm);			
		}
		else {
			JOptionPane.showMessageDialog(contentPane, "Please enter a message...");
		}
	}
	
	public void goBack() {
		this.client.listPrivateMessages();
		this.client.getWindowHandler().closePmWindow();
	}

}
