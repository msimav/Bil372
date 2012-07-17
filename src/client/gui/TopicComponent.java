package client.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
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
	/**
	 * Create the panel.
	 */
	public TopicComponent() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {				
				listPosts();
			}
		});
		setBackground(Color.WHITE);
		setBorder(new MatteBorder(0, 0, 5, 0, (Color) new Color(72, 61, 139)));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(10, 0));
		
		userName = new JLabel("User :UserName");
		userName.setFont(new Font("Segoe UI", Font.BOLD, 11));
		userName.setForeground(new Color(70, 130, 180));
		userName.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(userName, BorderLayout.EAST);
		
		date = new JLabel("Create Date : 10.12.2012");
		date.setForeground(new Color(65, 105, 225));
		date.setFont(new Font("Segoe UI", Font.BOLD, 11));
		date.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(date, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		add(panel_1, BorderLayout.WEST);
		
		topicName = new JLabel("Cok \u00F6nemli topic ");
		topicName.setHorizontalAlignment(SwingConstants.CENTER);
		topicName.setForeground(new Color(219, 112, 147));
		topicName.setFont(new Font("Segoe UI", Font.BOLD, 13));
		panel_1.add(topicName);

	}
	
	public void listPosts() {
		this.client.listPosts(topic);
		this.client.getWindowHandler().getMainWindow().currentTopic = topic;
	}

}
