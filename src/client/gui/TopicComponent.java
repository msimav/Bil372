package client.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.MatteBorder;

import client.Client;

import beans.Topic;

public class TopicComponent extends JPanel {
	
	public Client client;
	public Topic topic;
	JLabel userName;
	JLabel date ;
	JLabel topicName;
	JPanel panel;
	JPanel panel_1;
	/**
	 * Create the panel.
	 */
	public TopicComponent(Client client) {
		
		try {
	        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName()
	            );
	    } catch (Exception e) { }
		
		this.client = client;
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {				
				listPosts();
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				enter();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				exit();
			}
		});
		setBackground(Color.WHITE);
		setBorder(new MatteBorder(0, 0, 3, 0, (Color) Color.LIGHT_GRAY));
		setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(10, 0));
		
		userName = new JLabel("User : UserName");
		userName.setFont(new Font("Segoe UI", Font.BOLD, 11));
		userName.setForeground(new Color(153, 0, 0));
		userName.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(userName, BorderLayout.EAST);
		
		date = new JLabel("Create Date : 10.12.2012");
		date.setForeground(new Color(0, 0, 0));
		date.setFont(new Font("Segoe UI", Font.BOLD, 11));
		date.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(date, BorderLayout.CENTER);
		
		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		add(panel_1, BorderLayout.WEST);
		
		topicName = new JLabel("Cok \u00F6nemli topic ");
		topicName.setHorizontalAlignment(SwingConstants.CENTER);
		topicName.setForeground(Color.BLACK);
		topicName.setFont(new Font("Segoe UI", Font.ITALIC, 13));
		panel_1.add(topicName);

	}
	
	public void listPosts() {
		this.client.getWindowHandler().getMainWindow().currentTopic = topic;
		this.client.listPosts(topic);
	}
	
	public void enter() {
		this.setBackground(Color.GRAY);
		this.panel.setBackground(Color.GRAY);
		this.panel_1.setBackground(Color.GRAY);
	}
	
	public void exit() {
		this.setBackground(Color.WHITE);
		this.panel.setBackground(Color.WHITE);
		this.panel_1.setBackground(Color.WHITE);
	}

}
