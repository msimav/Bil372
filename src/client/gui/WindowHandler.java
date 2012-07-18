package client.gui;

import java.lang.reflect.Method;
import javax.swing.JOptionPane;

import client.Client;
import client.gui.statistics.LoginLogChart;
import client.gui.statistics.PostOfTopicChart;

import beans.*;

public class WindowHandler {
	
	private Client client;
	private LoginWindow loginWindow;
	private MainWindow mainWindow;
	private CreateTopicWindow createTopicWindow;
	private RegisterWindow registerWindow;
	private PrivateMessageWindow pmWindow;
	private MessageWindow messageWindow;
	private OpenConversationWindow openConversationWindow;
	private CreatePostWindow createPostWindow;
	private ChangePasswordWindow changePassword;
	private LoginLogChart logChartWindow;
	private PostOfTopicChart postChartWindow;
	
	public WindowHandler( Client client ) {
		this.client = client;
		openLoginWindow();		
	}
	
	
	/*
	 * Client methods
	 */
	public void showError(String reason) {
		JOptionPane.showMessageDialog(null, reason);
	}
	
	public void showSuccess(String reason) {
		JOptionPane.showMessageDialog(null, reason);
	}
	
	public void loginAccepted () {
		this.closeLoginWindow();
		this.openMainWindow();
	}
	
	public void loginDenied( String reason ) {
		showError(reason);
	}
	
	public void appendTopic( Topic topic) {
		mainWindow.addTopic(topic);
		this.mainWindow.statusLabel.setText("A new topic opened : '" + topic.getTitle() + "'" );
		this.mainWindow.statusLabel.setVisible(true);
	}
	
	public void showTopicList( Topic[] topicList) {
		if( topicList.length != 0 ) {
			mainWindow.clearTopics();
			for ( Topic t : topicList ) {
				mainWindow.addTopic(t);
			}
		}		
		else {
			JOptionPane.showMessageDialog(null, "There is no topic to show...");
		}
	}

	public void newPost(Post newPost) {
		if( mainWindow.currentTopic.getId() == newPost.getTopic().getId() ) {
			mainWindow.addPost(newPost);
			this.mainWindow.statusLabel.setText("There is a new post for the topic : " +  newPost.getTopic().getTitle() );
			this.mainWindow.statusLabel.setVisible(true);
		}
	}
	
	public void showPosts( Post[] postList ) {
		if( postList.length != 0 ) {
			mainWindow.clearPosts();
			mainWindow.currentTopic = postList[0].getTopic();
			for( Post p : postList ) {
				mainWindow.addPost(p);
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "There is no post...");
		}
	}	
	
	public void listUsers( User[] users ) {
		if( users.length != 0 ) {
			openNewConversationWindow();
			closeMessageWindow();
			openConversationWindow.fillUserList(users);		
		}
		else {
			JOptionPane.showMessageDialog(null, "There is no user that you can send message...");
		}
	}
	
	public void showMessages( PrivateMessage[] pmList) {
		if( pmList.length != 0 ) {
			openMessageWindow();
			for(PrivateMessage pm : pmList ) {
				this.messageWindow.addMessageComponent(pm);
			}
		}
		else {
			openMessageWindow();
		}
	}	
	
	public void showConversation( PrivateMessage[] pmList ) {
		if( pmList.length != 0 ) {
			closeMessageWindow();
			openPmWindow();
			pmWindow.from = pmList[0].getFrom().getId() == this.client.getUser().getId() ? pmList[0].getTo() : pmList[0].getFrom();
			pmWindow.fromLabel.setText( pmWindow.from.getName() );
			for(PrivateMessage pm : pmList ) {
				pmWindow.addMessage(pm);
			}
		}
	}
	
	public void registerSuccess(User user) {
		closeRegisterWindow();
		openLoginWindow();
		loginWindow.emailText.setText( user.getEmail() );
	}
	
	public void registerFailed( String reason ) {
		showError(reason);
	}
	
	
	
	
	/*
	 * Open - Close Methods
	 */
	
	//Ogrenme amacli kullanilmayan method
	public void openWindow(String arg) {
		Class<? extends WindowHandler> s = this.getClass();
		String name ="open" + arg;
		Method m;
		try{
			m = s.getDeclaredMethod(name);
			m.invoke(this);
		}
		catch(Exception e) {
			
		}
	}
	
	public void openLoginLogChart() {
		LoginLogChart.run();
	}
	
	public void openPostChart() {
		PostOfTopicChart.run();
	}
	
	public void openLoginWindow() {
		loginWindow = new LoginWindow(this.client);
		loginWindow.setVisible(true);
	}
	
	public void closeLoginWindow() {
		loginWindow.setVisible(false);
		loginWindow.dispose();
	}
	
	public void openChangePasswordWindow() {
		changePassword = new ChangePasswordWindow(this.client);
		changePassword.setVisible(true);
	}
	
	public void closeChangePasswordWindow() {
		changePassword.setVisible(false);
		changePassword.dispose();
	}
	
	public void openMainWindow() {
		mainWindow = new MainWindow(this.client);
		mainWindow.setVisible(true);
		mainWindow.mnNewMenu.setText(this.client.getUser().getName());
	}
	
	public void closeMainWindow() {
		mainWindow.setVisible(false);
		mainWindow.dispose();
	}
	
	public void openTopicWindow() {
		createTopicWindow = new CreateTopicWindow(this.client);
		createTopicWindow.setVisible(true);
	}
	
	public void closeTopicWindow() {
		createTopicWindow.setVisible(false);
		createTopicWindow.dispose();
	}
	
	public void openCreatePostWindow() {		
		createPostWindow = new CreatePostWindow(this.client);
		createPostWindow.setVisible(true);
	}
	
	public void closeCreatePostWindow() {
		createPostWindow.setVisible(false);
		createPostWindow.dispose();
	}
	
	public void openRegisterWindow() {		
		registerWindow = new RegisterWindow(this.client);
		registerWindow.setVisible(true);
	}
	
	public void closeRegisterWindow() {
		registerWindow.setVisible(false);
		registerWindow.dispose();
	}
	
	public void openMessageWindow() {
		messageWindow = new MessageWindow(this.client);
		messageWindow.setVisible(true);
	}
	
	public void closeMessageWindow() {
		messageWindow.setVisible(false);
		messageWindow.dispose();
	}
	
	public void openPmWindow() {
		pmWindow = new PrivateMessageWindow(this.client);
		pmWindow.setVisible(true);
	}
	
	public void closePmWindow() {
		pmWindow.setVisible(false);
		pmWindow.dispose();
	}
	
	public void openCreateTopicWindow() {		
		createTopicWindow = new CreateTopicWindow(this.client);
		createTopicWindow.setVisible(true);
	}
	
	public void closeCreateTopicWindow() {
		createTopicWindow.setVisible(false);
		createTopicWindow.dispose();
	}
	
	public void openNewConversationWindow() {
		openConversationWindow = new OpenConversationWindow(this.client);
		openConversationWindow.setVisible(true);
	}
	
	public void closeNewConversationWindow() {
		openConversationWindow.setVisible(false);
		openConversationWindow.dispose();
	}
	
	
	/*
	 * Getter-Setter Methods
	 */

	public Client getClient() {
		return client;
	}

	public LoginWindow getLoginWindow() {
		return loginWindow;
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public CreateTopicWindow getCreateTopicWindow() {
		return createTopicWindow;
	}

	public RegisterWindow getRegisterWindow() {
		return registerWindow;
	}

	public PrivateMessageWindow getPmWindow() {
		return pmWindow;
	}

	public MessageWindow getMessageWindow() {
		return messageWindow;
	}
	
	public CreatePostWindow getCreatePostWindow() {
		return createPostWindow;
	}
	
}


