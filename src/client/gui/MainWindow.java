package client.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.FlowLayout;

import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

import client.Client;

import beans.Post;
import beans.Topic;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import util.Utils;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField searchText;
	private JPanel leftCenter;
	private JPanel rightCenter;
	private JScrollPane scrollPane;
	public JMenu mnNewMenu;
	private ArrayList<Topic> topicList;
	public Topic currentTopic;
	public Client client;
	public JLabel statusLabel;

	public static void main(String[] args) {
		new MainWindow(new Client());
	}
	/**
	 * Create the frame.
	 */
	public MainWindow(Client client) {
		
		try {
	        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName()
	            );
	    } catch (Exception e) { }
		
		this.client = client;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				close();
			}
		});
		setResizable(false);
		setTitle("Dalga");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 999, 635);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(10, 0));
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(1, 2, 20, 0));
		
		scrollPane = new JScrollPane();
		centerPanel.add(scrollPane);
		
		
		leftCenter = new JPanel();
		leftCenter.setBackground(Color.WHITE);
		scrollPane.setViewportView(leftCenter);
		leftCenter.setLayout(new BoxLayout(leftCenter, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		centerPanel.add(scrollPane_1);
		
		
		rightCenter = new JPanel();
		rightCenter.setBackground(Color.WHITE);
		scrollPane_1.setViewportView(rightCenter);
		rightCenter.setLayout(new BoxLayout(rightCenter, BoxLayout.Y_AXIS));
		
		JPanel northPanel = new JPanel();
		contentPane.add(northPanel, BorderLayout.NORTH);
		GridBagLayout gbl_northPanel = new GridBagLayout();
		gbl_northPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_northPanel.rowHeights = new int[]{0, 0, 0};
		gbl_northPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_northPanel.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		northPanel.setLayout(gbl_northPanel);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.weighty = 1.0;
		gbc_panel_1.weightx = 0.2;
		gbc_panel_1.anchor = GridBagConstraints.WEST;
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		northPanel.add(panel_1, gbc_panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));
		
		JButton btnNewButton = new JButton("Home");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				home();								
			}
		});
		panel_1.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Messages");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				showMessageList();				
			}
		});
		panel_1.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Create Topic");
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				createTopic();
			}
		});
		panel_1.add(btnNewButton_2);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.ipady = 5;
		gbc_panel_2.ipadx = 5;
		gbc_panel_2.weighty = 1.1;
		gbc_panel_2.weightx = 0.5;
		gbc_panel_2.insets = new Insets(10, 0, 10, 5);
		gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 0;
		northPanel.add(panel_2, gbc_panel_2);
		panel_2.setLayout(new BorderLayout(15, 0));
		
		searchText = new JTextField();
		searchText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_ENTER) {
					search();
				}
			}
		});
		panel_2.add(searchText, BorderLayout.CENTER);
		searchText.setColumns(10);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setHgap(10);
		panel_2.add(panel, BorderLayout.EAST);
		
		JButton btnNewButton_3 = new JButton("Search");
		panel.add(btnNewButton_3);
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				search();
			}
		});
		
		JMenuBar menuBar_1 = new JMenuBar();
		panel.add(menuBar_1);
		
		JMenu mnNewMenu_1 = new JMenu("Statistics");
		menuBar_1.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Post Statistics");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				postChart();
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_3);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Login Log Statistics");
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loginChart();
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_4);
		
		
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
		flowLayout_1.setHgap(20);
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.anchor = GridBagConstraints.EAST;
		gbc_panel_3.weighty = 1.0;
		gbc_panel_3.weightx = 0.3;
		gbc_panel_3.gridx = 2;
		gbc_panel_3.gridy = 0;
		northPanel.add(panel_3, gbc_panel_3);
		
		JButton btnPost = new JButton("New Post");
		btnPost.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				createNewPost();
			}
			
		});
		panel_3.add(btnPost);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		panel_3.add(menuBar);
		
		mnNewMenu = new JMenu("UserName");
		mnNewMenu.setBackground(Color.LIGHT_GRAY);
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Change Password");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changePassword();
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Add/Change Profile Picture");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseImage();
			}
		});
		mntmNewMenuItem_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				chooseImage();
			}
		});
		mntmNewMenuItem_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				chooseImage();
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Logout");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logout();
			}
		});
		mnNewMenu.add(mntmNewMenuItem_2);
		
		statusLabel = new JLabel("New label");
		statusLabel.setVisible(false);
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statusLabel.setForeground(new Color(255, 0, 0));
		statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
		contentPane.add(statusLabel, BorderLayout.SOUTH);
		
		this.topicList = new ArrayList<Topic>();
		this.client.listTopics();
	}

	//Postlar�n listelendi�i componenta post ekler
	public void addPost(Post post) {
		
		MessageComponent newComp = new MessageComponent(this.client);
		newComp.txtrMessagetext.setText( post.getPost() );
		newComp.userName.setText( post.getUser().getName() );
		newComp.dateLabel.setText( post.getDate() );
		newComp.post = post;
		newComp.reply = post.getReply();
		newComp.iconLabel.setIcon(Utils.byteTOImage( post.getUser().getAvatar()));
		
		if( post.getReply() == null ) {
			newComp.txtrReplytext.setVisible(false);
			Dimension d = new Dimension();
			d.width = 455;
			d.height = 100 + newComp.txtrMessagetext.getLineCount() * 20 ;
			newComp.setMinimumSize(d);
			newComp.setMaximumSize(d);
			newComp.setPreferredSize(d);
			rightCenter.add( newComp );
		}
		else {
			newComp.txtrReplytext.setVisible( true );
			newComp.txtrReplytext.setText( String.format("In reply to %s:\n%s", post.getReply().getUser().getName() ,post.getReply().getPost()) );
			
			Dimension d = new Dimension();
			d.width = 455;
			d.height = 100 + newComp.txtrMessagetext.getLineCount() * 20 + newComp.txtrReplytext.getLineCount() * 20;
			newComp.setMinimumSize(d);
			newComp.setMaximumSize(d);
			newComp.setPreferredSize(d);
			rightCenter.add( newComp );
			
		}	
		
		rightCenter.revalidate();
	}
	
	public void addTopic( Topic topic ) {
		topicList.add(topic);
		
		TopicComponent newComp = new TopicComponent(this.client);
		newComp.topicName.setText( topic.getTitle() );
		newComp.userName.setText( topic.getUser().getName() );
		newComp.date.setText( topic.getDate() );
		newComp.topic = topic;
		
		Dimension maximumSize = new Dimension();
		maximumSize.width = scrollPane.getWidth() - 20;
		maximumSize.height = 70;
		
		newComp.setSize(maximumSize);
		newComp.setMinimumSize(maximumSize);
		newComp.setMaximumSize(maximumSize);
		newComp.setPreferredSize(maximumSize);
		
		leftCenter.add(newComp);		
		leftCenter.revalidate();
	}
	
	public void createNewPost() {
		this.client.getWindowHandler().openCreatePostWindow();
		this.client.getWindowHandler().getCreatePostWindow().topic = this.currentTopic;
		this.client.getWindowHandler().getCreatePostWindow().topicNameLabel.setText(this.currentTopic.getTitle());
	}
	
	public void clearTopics() {
		leftCenter.removeAll();
		this.repaint();
		leftCenter.revalidate();
	}
	
	public void clearPosts() {
		rightCenter.removeAll();
		this.repaint();
		rightCenter.revalidate();
	}
	
	public void createTopic() {
		this.client.getWindowHandler().openCreateTopicWindow();
	}
	
	public void home() {
		this.client.listTopics();
	}
	
	public void showMessageList() {
		this.client.listPrivateMessages();
	}
	
	public void close() {
		this.client.disconnect();
	}
	
	public void chooseImage() {
		FileChooser.createAndShowGUI(client);
	}
	
	public void changePassword() {
		this.client.getWindowHandler().openChangePasswordWindow();
	}
	
	public void search() {
		this.client.search( this.searchText.getText());
		this.revalidate();
	}
	
	public void logout() {
		this.client.disconnect();
	}
	
	public void postChart() {
		this.client.getWindowHandler().openPostChart();
	}
	
	public void loginChart() {
		this.client.getWindowHandler().openLoginLogChart();
	}
}
