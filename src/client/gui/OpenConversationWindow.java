package client.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import util.Utils;

import beans.PrivateMessage;
import beans.User;

import client.Client;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OpenConversationWindow extends JFrame {

	private JPanel contentPane;
	public Client client;
	public JComboBox userListComboBox;
	private User[] userList;
	private JTextArea textArea;
	

	/**
	 * Create the frame.
	 */
	public OpenConversationWindow(Client client) {
		try {
	        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName()
	            );
	    } catch (Exception e) { }
		
		this.client = client;
		
		setTitle("Send Message");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 421, 422);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(10, 30));
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		scrollPane.setViewportView(textArea);
		
		JPanel northPanel = new JPanel();
		contentPane.add(northPanel, BorderLayout.NORTH);
		northPanel.setLayout(new GridLayout(1, 0, 0, 10));
		
		JLabel lblNewLabel = new JLabel("Select Receiver User :");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		northPanel.add(lblNewLabel);
		
		userListComboBox = new JComboBox();
		northPanel.add(userListComboBox);
		
		JPanel southPanel = new JPanel();
		contentPane.add(southPanel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Back");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				goBack();
			}
		});
		southPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Send");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				sendMessage();
			}
		});
		southPanel.add(btnNewButton_1);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.EAST);
	}
	
	public void fillUserList(User[] userList) {
		this.userList = userList;
		for (int i = 0; i < userList.length; i++) {
			userListComboBox.insertItemAt(userList[i].getName(), i);
		}
	}
	
	//TODO Test et
	public void sendMessage() {
		int selectedIndex = userListComboBox.getSelectedIndex();
		
		if( selectedIndex != -1  && textArea.getText() != null ) {
			PrivateMessage newMessage = new PrivateMessage(-1 , userList[ selectedIndex ] , this.client.getUser() , Utils.getDate() , textArea.getText());
			this.client.sendPrivateMessage(newMessage);
		}
		else {
			JOptionPane.showMessageDialog(contentPane, "Please fill out hte required fields...");
		}
		
	}
	
	public void goBack() {
		this.client.listPrivateMessages();
		this.client.getWindowHandler().closeNewConversationWindow();
	}

}
