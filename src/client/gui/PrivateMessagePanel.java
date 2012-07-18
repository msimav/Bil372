package client.gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.UIManager;

import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.GridLayout;

public class PrivateMessagePanel extends JPanel {

	JLabel dateLabel;
	JTextArea message;
	public JLabel userName;
	/**
	 * Create the panel.
	 */
	public PrivateMessagePanel() {
		
		try {
	        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName()
	            );
	    } catch (Exception e) { }
		
		
		
		setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(0, 0, 0)));
		setLayout(new BorderLayout(5, 5));
		
		JPanel westPanel = new JPanel();
		add(westPanel, BorderLayout.WEST);
		
		JLabel avatarLabel = new JLabel("");
		avatarLabel.setIcon(new ImageIcon("C:\\Users\\Umut\\Desktop\\darth_vader_icon_64x64_by_geo_almighty-d33pmvd.png"));
		westPanel.add(avatarLabel);
		
		JPanel northPanel = new JPanel();
		add(northPanel, BorderLayout.NORTH);
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
		
		userName = new JLabel("Umut Ozan Y\u0131ld\u0131r\u0131m");
		userName.setFont(new Font("Segoe UI", Font.BOLD, 12));
		northPanel.add(userName);
		
		JPanel centerPanel = new JPanel();
		add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
		
		message = new JTextArea();
		message.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		message.setEditable(false);
		message.setOpaque(false);
		centerPanel.add(message);
		
		JPanel southPanel = new JPanel();
		add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		dateLabel = new JLabel("Date");
		dateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		southPanel.add(dateLabel);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.EAST);

	}

}
