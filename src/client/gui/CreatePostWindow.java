package client.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import beans.Post;
import beans.Topic;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import util.Utils;

import client.Client;

import java.awt.GridLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreatePostWindow extends JFrame {

	public Topic topic;
	public Post reply;
	public Client client;
	private JPanel contentPane;
	private JTextArea textArea;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
					);
		} catch (Exception e) { }

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreatePostWindow frame = new CreatePostWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CreatePostWindow() {

		setTitle("Create New Post");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 547, 369);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(20, 10));
		setContentPane(contentPane);

		JPanel northPanel = new JPanel();
		northPanel.setForeground(Color.PINK);
		contentPane.add(northPanel, BorderLayout.NORTH);

		JLabel topicNameLabel = new JLabel("Topic Basl\u0131g\u0131");
		topicNameLabel.setForeground(Color.PINK);
		northPanel.add(topicNameLabel);

		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		GridBagLayout gbl_centerPanel = new GridBagLayout();
		gbl_centerPanel.columnWidths = new int[]{461, 0};
		gbl_centerPanel.rowHeights = new int[] {10, 0, 0};
		gbl_centerPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_centerPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		centerPanel.setLayout(gbl_centerPanel);

		JLabel lblNewLabel = new JLabel("New Post :");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		centerPanel.add(lblNewLabel, gbc_lblNewLabel);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		centerPanel.add(scrollPane, gbc_scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		JPanel southPanel = new JPanel();
		contentPane.add(southPanel, BorderLayout.SOUTH);

		JButton backButton = new JButton("Back");
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				goBack();
			}
		});
		southPanel.add(backButton);

		JButton createButton = new JButton("Create");
		createButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				createPost();
			}
		});
		southPanel.add(createButton);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.EAST);
	}

	public void createPost() {
		if( this.textArea.getText() != null ) {
			Post post = new Post( -1 , this.client.getUser() , this.topic , Utils.getDate() , textArea.getText() , this.reply );
			this.client.createPost(post);
		}
		else {
			JOptionPane.showMessageDialog(null, "Please write your post.");
		}
	}
	
	public void goBack() {
		this.client.getWindowHandler().closeCreatePostWindow();
	}

}
