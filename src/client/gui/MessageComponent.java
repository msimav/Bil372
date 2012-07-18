package client.gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Component;
import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import client.Client;

import beans.Post;

import java.awt.GridLayout;
import java.awt.SystemColor;

public class MessageComponent extends JPanel {

	/**
	 * Create the panel.
	 */
	JTextArea txtrReplytext;
	JTextArea txtrMessagetext;
	JLabel dateLabel;
	JButton btnNewButton;
	JLabel userName;
	public JLabel iconLabel;
	public Post post;
	public Post reply;
	public Client client;
	JPanel panel;
	JPanel centerPanel;
	JPanel leftPanel;
	JPanel panel_3 ;
	JPanel southPanel;
	JPanel panel_1;
	JPanel panel_2;
	
	public MessageComponent(Client client) {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				enter();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				exit();
			}
		});
		
		try {
	        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName()
	            );
	    } catch (Exception e) { }
		
		this.client = client;

		setBackground(Color.WHITE);
		setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.LIGHT_GRAY));
		
		setLayout(new BorderLayout(5, 10));
		
		leftPanel = new JPanel();
		leftPanel.setBackground(Color.WHITE);
		add(leftPanel, BorderLayout.WEST);
		
		iconLabel = new JLabel("");
		iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
		iconLabel.setIcon(new ImageIcon("C:\\Users\\Umut\\Desktop\\darth_vader_icon_64x64_by_geo_almighty-d33pmvd.png"));
		leftPanel.add(iconLabel);
		
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		centerPanel.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 1, 0, 10));
		
		userName = new JLabel("User Name");
		userName.setHorizontalAlignment(SwingConstants.LEFT);
		userName.setForeground(new Color(0, 0, 0));
		userName.setBackground(Color.WHITE);
		userName.setFont(new Font("Segoe UI", Font.BOLD, 13));
		panel_3.add(userName);
		
		
		
		txtrMessagetext = new JTextArea();		
		txtrMessagetext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				enter();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				exit();
			}
		});
		txtrMessagetext.setForeground(Color.DARK_GRAY);
		txtrMessagetext.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtrMessagetext.setEditable(false);
		txtrMessagetext.setText("messageText");
		txtrMessagetext.setBorder(null);
		centerPanel.add(txtrMessagetext);
		
		
		txtrReplytext = new JTextArea();
		txtrReplytext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				enter();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				exit();
			}
		});
		txtrReplytext.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtrReplytext.setText("replyText");
		txtrReplytext.setEditable(false);
		txtrReplytext.setForeground(new Color(128, 0, 0));
		txtrReplytext.setVisible(false);
		txtrReplytext.setBorder(null);
		centerPanel.add(txtrReplytext);
		
		southPanel = new JPanel();
		southPanel.setBackground(Color.WHITE);
		add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		southPanel.add(panel);
		
		panel_1 = new JPanel();
		panel_1.setBorder(null);
		panel_1.setBackground(Color.WHITE);
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setHgap(20);
		flowLayout.setAlignment(FlowLayout.RIGHT);
		southPanel.add(panel_1);
		
		JLabel lblNewLabel = new JLabel(" ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
		panel_1.add(lblNewLabel);
		
		dateLabel = new JLabel("10.10.2010");
		panel_1.add(dateLabel);
		
		
		btnNewButton = new JButton(" Reply");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				replyPost();
			}
		});
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\Umut\\Desktop\\reply_icon.gif"));
		panel_1.add(btnNewButton);
		
		panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		add(panel_2, BorderLayout.EAST);

	}
	
	public void replyPost() {
		this.client.getWindowHandler().openCreatePostWindow();
		this.client.getWindowHandler().getCreatePostWindow().reply = this.post;
		this.client.getWindowHandler().getCreatePostWindow().topic = this.client.getWindowHandler().getMainWindow().currentTopic;
		this.client.getWindowHandler().getCreatePostWindow().topicNameLabel.setText(this.client.getWindowHandler().getMainWindow().currentTopic.getTitle());
		this.client.getWindowHandler().getCreatePostWindow().setTitle( "In reply to : " + this.post.getUser().getName());
	}
	
	public void enter() {
		this.setBackground(new Color(255, 255, 204));
		this.txtrMessagetext.setBackground(new Color(255, 255, 204));
		this.txtrReplytext.setBackground(new Color(255, 255, 204));
		this.leftPanel.setBackground(new Color(255, 255, 204));
		this.centerPanel.setBackground(new Color(255, 255, 204));
		this.panel.setBackground(new Color(255, 255, 204));
		this.panel_1.setBackground(new Color(255, 255, 204));
		this.panel_2.setBackground(new Color(255, 255, 204));
		this.panel_3.setBackground(new Color(255, 255, 204));
	}
	
	public void exit() {
		this.setBackground(Color.WHITE);
		this.txtrMessagetext.setBackground(Color.WHITE);
		this.txtrReplytext.setBackground(Color.WHITE);
		this.leftPanel.setBackground(Color.WHITE);
		this.centerPanel.setBackground(Color.WHITE);
		this.panel.setBackground(Color.WHITE);
		this.panel_1.setBackground(Color.WHITE);
		this.panel_2.setBackground(Color.WHITE);
		this.panel_3.setBackground(Color.WHITE);
	}

}
