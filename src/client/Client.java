package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;

import client.gui.WindowHandler;

import util.Utils;
import beans.*;

public class Client {
	// TODO tum exceptionlari duzgun bir bicimde handle et
	// gui bunun icin showError methodu saglasin
	public static final String server = "";
	public static final int port = 5001;

	private BufferedReader reader;
	private BufferedWriter writer;
	private Socket socket;
	private int sessionid;
	private User user;
	private WindowHandler gui;


	public Client() {
		// Initilaze GUI
		gui = new WindowHandler(this);

		try {
			// Connnet to server
			socket = new Socket(server, port);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			// Server'dan gelen baglantilari dinleyen thread
			Thread listener = new Thread(new Runnable() {
				public void run() {
					String input;

					try {
						while ((input = reader.readLine()) != null) {
							handleInput(input);
						}
					} catch (EOFException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			listener.start();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleInput(String input) {
		// Get cmd name and its params
		String[] cmdparam = input.split(" ", 2);
		String name = "cmd" + cmdparam[0]; // cmdparam[0] is cmd

		Class<? extends Client> s = this.getClass();
		Method method;
		try {
			method = s.getDeclaredMethod(name, String.class);
			method.invoke(this, cmdparam[1]);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public WindowHandler getWindowHandler() {
		return gui;
	}

	public User getUser() {
		return user;
	}

	private void send(String arg) {
		try {
			writer.write(arg);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public void close() {
		try {
			if (reader != null)
				reader.close();
			if(writer != null)
				writer.close();
			if(socket != null)
				socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try{
			close();
		} finally {
			super.finalize();
		}
	}

	/***************
	 * GUI Methods *
	 ***************/

	public void login(User login) {
		user = login;
		send(String.format("LOGIN %s\n", Utils.toJSON(user)));
	}

	public void register(User newuser) {
		send(String.format("REGISTER %s\n", Utils.toJSON(newuser)));
	}

	public void createTopic(Topic newtopic) {
		send(String.format("CREATETOPIC %s\n", Utils.toJSON(newtopic)));
	}

	public void listPrivateMessages() {
		send(String.format("LSPM %s\n", Utils.toJSON(this.user)));
	}

	public void showConversation(User friend) {
		// (me, friend) tuple seklinde gonderilecek request
		User[] tuple = new User[2];
		tuple[0] = this.user;
		tuple[1] = friend;
		send(String.format("GETCONVERSATION %s\n", Utils.toJSON(tuple)));
	}

	public void createPost(Post newpost) {
		send(String.format("CREATEPOST %s\n", Utils.toJSON(newpost)));
	}

	public void sendPrivateMessage(PrivateMessage newpm) {
		send(String.format("SENDPM %s\n", Utils.toJSON(newpm)));
	}

	public void listPosts(Topic topic) {
		send(String.format("LSPOST %s\n", Utils.toJSON(topic)));
	}

	public void listTopics() {
		// User'in gorebildigi topicleri listelemek icin user parametresi gonder
		send(String.format("LSTOPIC %s\n", Utils.toJSON(this.user)));
	}

	public void listUsers() {
		// Kendi disindaki userlarin listesini doner
		send(String.format("LSUSER %s\n", Utils.toJSON(this.user)));
	}

	public void disconnect() {
		send(String.format("LOGOUT %d\n", sessionid));
	}

	public void reconnect() {
		send(String.format("RECONNECT %d\n", sessionid));
	}

	/********************
	 * Protocol Methods *
	 ********************/

	@SuppressWarnings("unused")
	private void cmdLOGIN(String input) {
		String[] sesionAndUser = input.split(" ", 2);
		int session = Integer.parseInt(sesionAndUser[0]);
		if(session == -1)
			gui.loginDenied(sesionAndUser[1]);
		else {
			this.user = Utils.fromJSON(sesionAndUser[1], User.class);
			this.sessionid = session;
			gui.loginAccepted();
		}
	}

	@SuppressWarnings("unused")
	private void cmdREGISTER(String input) {
		String[] response = input.split(" ", 2);
		int code = Integer.parseInt(response[0]);
		if(code == -1)
			gui.registerFailed(response[1]);
		else {
			gui.registerSuccess(Utils.fromJSON(response[1], User.class));
		}
	}

	@SuppressWarnings("unused")
	private void cmdNEWTOPIC(String input) {
		Topic newtopic = Utils.fromJSON(input, Topic.class);
		gui.appendTopic(newtopic);
	}

	@SuppressWarnings("unused")
	private void cmdLSPM(String input) {
		PrivateMessage[] pmlist = Utils.fromJSON(input, PrivateMessage[].class);
		gui.showMessages(pmlist);
	}

	@SuppressWarnings("unused")
	private void cmdGETCONVERSATION(String input) {
		PrivateMessage[] conv = Utils.fromJSON(input, PrivateMessage[].class);
		gui.showConversation(conv);
	}

	@SuppressWarnings("unused")
	private void cmdNEWPOST(String input) {
		Post newpost = Utils.fromJSON(input, Post.class);
		gui.newPost(newpost);
	}

	@SuppressWarnings("unused")
	private void cmdLSPOST(String input) {
		Post[] postList = Utils.fromJSON(input, Post[].class);
		gui.showPosts(postList);
	}

	@SuppressWarnings("unused")
	private void cmdLSTOPIC(String input) {
		Topic[] topicList = Utils.fromJSON(input, Topic[].class);
		gui.showTopicList(topicList);
	}

	@SuppressWarnings("unused")
	private void cmdLSUSER(String input) {
		User[] userList = Utils.fromJSON(input, User[].class);
		gui.listUsers(userList);
	}

	@SuppressWarnings("unused")
	private void cmdERROR(String input) {
		gui.showError(input);
	}

	@SuppressWarnings("unused")
	private void cmdSUCCCESS(String input) {
		gui.showSuccess(input);
	}

	@SuppressWarnings("unused")
	private void cmdRECONNECT(String input) {
		// TODO dustugumu anlamam lazım ki reconnect gondereyim
	}

	@SuppressWarnings("unused")
	private void cmdSHUTDOWN(String input) {
		gui.showError(input);
		this.close();
		System.exit(0);
	}
}
