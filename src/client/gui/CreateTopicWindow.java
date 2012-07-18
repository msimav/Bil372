package client.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;

import beans.Post;
import beans.Tag;
import beans.Topic;

import client.Client;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import util.Utils;
import java.awt.Font;

public class CreateTopicWindow extends JFrame {

	private JPanel contentPane;
	private JTextField headerText;
	private JTextField tagText;
	private JTextArea postText;
	public Client client;
	
	public CreateTopicWindow(Client client) {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()
	            );
	    } catch (Exception e) { }
		
		this.client = client;
		
		setResizable(false);
		setTitle("Add Topic");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 456, 384);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{440, 0};
		gbl_contentPane.rowHeights = new int[]{40, 40, 40, 40, 40, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNewLabel = new JLabel("Topic Header:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
		headerText = new JTextField();
		GridBagConstraints gbc_headerText = new GridBagConstraints();
		gbc_headerText.fill = GridBagConstraints.BOTH;
		gbc_headerText.insets = new Insets(0, 0, 5, 0);
		gbc_headerText.gridx = 0;
		gbc_headerText.gridy = 1;
		contentPane.add(headerText, gbc_headerText);
		headerText.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Topic Tags :");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		tagText = new JTextField();
		GridBagConstraints gbc_tagText = new GridBagConstraints();
		gbc_tagText.fill = GridBagConstraints.BOTH;
		gbc_tagText.insets = new Insets(0, 0, 5, 0);
		gbc_tagText.gridx = 0;
		gbc_tagText.gridy = 3;
		contentPane.add(tagText, gbc_tagText);
		tagText.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.weighty = 30.0;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 5;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		postText = new JTextArea();
		postText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		scrollPane.setViewportView(postText);
		
		JPanel southPanel = new JPanel();
		GridBagConstraints gbc_southPanel = new GridBagConstraints();
		gbc_southPanel.insets = new Insets(0, 0, 5, 0);
		gbc_southPanel.fill = GridBagConstraints.BOTH;
		gbc_southPanel.gridx = 0;
		gbc_southPanel.gridy = 6;
		contentPane.add(southPanel, gbc_southPanel);
		
		JButton btnNewButton_1 = new JButton("Add Topic");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				createTopic();
			}
		});
		southPanel.add(btnNewButton_1);
		
		JLabel lblNewLabel_2 = new JLabel("First Post :");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 4;
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);
	}
	
	public void createTopic() {
		if( tagText.getText() != null && headerText.getText() != null && postText.getText() != null ) {
			String[] tags = tagText.getText().split(",");
			if( tags != null && tags.length != 0 ) {
				Tag[] tagList = new Tag[ tags.length ];
				for(int i = 0 ; i < tags.length ; i++ ) {
					tagList[i] = new Tag(tags[i]);
				}
				Topic newTopic = new Topic( -1 , this.client.getUser() , Utils.getDate() , headerText.getText() ,tagList , new Post(-1 , this.client.getUser() , null , Utils.getDate() , postText.getText() , null) );
				this.client.createTopic(newTopic);
			}
			else {
				JOptionPane.showMessageDialog(null, "Please enter your tags by separating with comma(,)...");
			}
			
		}
		else {
			JOptionPane.showMessageDialog(null, "Please fill out the fields correctly...");
		}
	}
}