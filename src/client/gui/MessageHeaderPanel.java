package client.gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.MatteBorder;

import beans.User;

import client.Client;

public class MessageHeaderPanel extends JPanel {

	JLabel dateLabel;
	JLabel lastMessageLbl;
	JLabel userName;
	public Client client;
	public User from;
	
	/**
	 * Create the panel.
	 */
	
	public void onClick() {
		client.showConversation(from);
	}
	
	public MessageHeaderPanel(Client client) {
		setBackground(Color.WHITE);
		
		try {
	        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName()
	            );
	    } catch (Exception e) { }
		
		this.client = client;
		
		setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				onClick();
			}
		});
		setLayout(new BorderLayout(0, 5));
		
		JPanel westPanel = new JPanel();
		westPanel.setBackground(Color.WHITE);
		add(westPanel, BorderLayout.WEST);
		
		JLabel iconLabel = new JLabel("");
		iconLabel.setIcon(new ImageIcon("C:\\Users\\Umut\\Desktop\\darth_vader_icon_64x64_by_geo_almighty-d33pmvd.png"));
		westPanel.add(iconLabel);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel.add(panel_1, BorderLayout.NORTH);
		
		userName = new JLabel("Ozan");
		panel_1.add(userName);
		userName.setFont(new Font("Segoe UI", Font.BOLD, 13));
		userName.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel.add(panel_2, BorderLayout.CENTER);
		
		lastMessageLbl = new JLabel("Burda gelen son mesaj");
		panel_2.add(lastMessageLbl);
		lastMessageLbl.setHorizontalAlignment(SwingConstants.CENTER);
		lastMessageLbl.setFont(new Font("Segoe UI", Font.ITALIC, 10));
		
		dateLabel = new JLabel("Date");
		dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
		dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(dateLabel, BorderLayout.SOUTH);

	}

}
