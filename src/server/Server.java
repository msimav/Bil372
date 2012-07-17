package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

import beans.Post;
import beans.PrivateMessage;
import beans.Topic;
import beans.User;

import util.Utils;

public class Server {

	private static final int port = 5001; // TODO: define the port number.

	private ServerSocket server;
	private HashMap<Integer, Server.Client> clients;
	private Loger loger;
	private DBHandler dbhandler;
	private int sessionCounter = 1;

	public Server() {
		// Register ShutdownHook
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				shutdown();
			}
		});

		// Initilaze clients map
		clients = new HashMap<Integer, Server.Client>();

		// Start logger
		loger = new Loger();
		try {
			loger.addPrintStream(new PrintStream("Server.log"));
		} catch (FileNotFoundException e) {
			System.err.println("Log file not found!");
			e.printStackTrace();
		}
		loger.log(String.format("[%s][INFO] Server initilazed.", Utils.getDate()));

		// Start DBHandler
		dbhandler = DBHandler.getInstance();
	}

	public void run() {
		Socket socket;

		try {
			server = new ServerSocket(port);
			loger.log(String.format("[%s][INFO] Server is running on %s:%d.", Utils.getDate(), server
					.getInetAddress().getHostAddress(), port));
			while (true) {
				socket = server.accept();
				loger.log(String.format("[%s][INFO] New connection from %s", Utils.getDate(), socket.getInetAddress().getHostAddress()));
				Client client = new Client(socket, sessionCounter);
				clients.put(sessionCounter, client);
				loger.log(String.format("[%s][INFO] Session ID of %s is %d", Utils.getDate(), client.ipaddr(), client.sessionid));
				new Thread(client).start();
				sessionCounter++;
			}
		} catch (IOException e) {
			loger.log(String.format("[%s][ERROR] IOException in Server.run: %s", Utils.getDate(), e.getMessage()));
		}
	}

	private void shutdown() {
		loger.log(String.format("[%s][INFO] Server is shuting down.", Utils.getDate()));

		//DBHandler
		dbhandler.close();
		loger.log(String.format("[%s][INFO] DBHandler is closed.", Utils.getDate()));

		// Clients
		loger.log(String.format("[%s][INFO] Sending shutdown signal to Clients.", Utils.getDate()));
		for (Iterator<Client> iterator = clients.values().iterator(); iterator.hasNext();) {
			Client client = (Client) iterator.next();
			client.send("SHUTDOWN Server is shuting down\n"); // TODO Shutdown message
			client.close();
		}

		// ServerSocket
		try {
			server.close();
			loger.log(String.format("[%s][INFO] ServerSocket is closed successfully.", Utils.getDate()));
		} catch (IOException e) {
			loger.log(String.format("[%s][ERROR] ServerSocket is not closed successfully.", Utils.getDate()));
			loger.log(String.format("[%s][ERROR] IOException in Server.shutdown: %s", Utils.getDate(), e.getMessage()));
		}

		// Log
		loger.log(String.format("[%s][INFO] Shutdown completed.", Utils.getDate()));
		loger.closeAll();
	}

	private void handleInput(String input, int sessionid) {
		// Get requester class
		Client requester;
		requester = clients.get(sessionid);
		// Get cmd name and its params
		String[] cmdparam = input.split(" ", 2);
		String name = "cmd" + cmdparam[0]; // cmdparam[0] is cmd

		Class<? extends Server> s = this.getClass();
		Method method;
		try {
			method = s.getDeclaredMethod(name, Server.Client.class, String.class);
			method.invoke(this, requester, cmdparam[1]);
		} catch (SecurityException e) { // Boring exception handling
			loger.log(String.format("[%s][ERROR] SecurityException in Server.handleInput: %s", Utils.getDate(), e.getMessage()));
			loger.log(String.format("[%s][ERROR] SecurityException Session ID: %d, IP Adres: %s", Utils.getDate(), requester.sessionid, requester.ipaddr()));
		} catch (NoSuchMethodException e) {
			loger.log(String.format("[%s][ERROR] NoSuchMethodException in Server.handleInput: %s", Utils.getDate(), e.getMessage()));
			loger.log(String.format("[%s][ERROR] NoSuchMethodException Session ID: %d, IP Adres: %s", Utils.getDate(), requester.sessionid, requester.ipaddr()));
			requester.send("ERROR no such command exist\n");
		} catch (IllegalArgumentException e) {
			loger.log(String.format("[%s][ERROR] IllegalArgumentException in Server.handleInput: %s", Utils.getDate(), e.getMessage()));
			loger.log(String.format("[%s][ERROR] IllegalArgumentException Session ID: %d, IP Adres: %s", Utils.getDate(), requester.sessionid, requester.ipaddr()));
		} catch (IllegalAccessException e) {
			loger.log(String.format("[%s][ERROR] IllegalAccessException in Server.handleInput: %s", Utils.getDate(), e.getMessage()));
			loger.log(String.format("[%s][ERROR] IllegalAccessException Session ID: %d, IP Adres: %s", Utils.getDate(), requester.sessionid, requester.ipaddr()));
		} catch (InvocationTargetException e) {
			loger.log(String.format("[%s][ERROR] InvocationTargetException in Server.handleInput: %s", Utils.getDate(), e.getMessage()));
			loger.log(String.format("[%s][ERROR] InvocationTargetException Session ID: %d, IP Adres: %s", Utils.getDate(), requester.sessionid, requester.ipaddr()));
			loger.log(String.format("[%s][ERROR] InvocationTargetException cmd: %s, params: %s", Utils.getDate(), cmdparam[0], cmdparam[1]));
		}
	}

	@SuppressWarnings("unused")
	private void cmdLOGIN(Client requester, String input) {
		User login = Utils.fromJSON(input, User.class);
		User response = dbhandler.login(login);
		if(response == null)
			requester.send("LOGIN -1 Login Error\n");
		else
			requester.send(String.format("LOGIN %d %s\n", requester.sessionid, Utils.toJSON(response)));
	}

	@SuppressWarnings("unused")
	private void cmdREGISTER(Client requester, String input) {
		User newuser = Utils.fromJSON(input, User.class);
		User response = dbhandler.register(newuser);
		if(response == null)
			requester.send("REGISTER -1 Register Failed\n");
		else
			requester.send(String.format("REGISTER 0 %s\n", Utils.toJSON(response)));
	}

	@SuppressWarnings("unused")
	private void cmdCREATETOPIC(Client requester, String input) {
		Topic newtopic = Utils.fromJSON(input, Topic.class);
		Topic response = dbhandler.createTopic(newtopic);
		if(response == null)
			requester.send("ERROR Topic creation failed\n"); // TODO review it
		else {
			for (Iterator<Client> iterator = clients.values().iterator(); iterator.hasNext();) {
				Client client = (Client) iterator.next();
				client.send(String.format("NEWTOPIC %s\n", Utils.toJSON(response)));
			}
		}
	}

	@SuppressWarnings("unused")
	private void cmdLSPM(Client requester, String input) {
		User user = Utils.fromJSON(input, User.class);
		PrivateMessage[] response = dbhandler.getPMs(user);
		if(response == null)
			requester.send("LSPM []\n");
		else
			requester.send(String.format("LSPM %s\n", Utils.toJSON(response)));
	}

	@SuppressWarnings("unused")
	private void cmdGETCONVERSATION(Client requester, String input) {
		User[] tuple = Utils.fromJSON(input, User[].class);
		PrivateMessage[] response = dbhandler.getPMdetails(tuple[0], tuple[1]);
		if(response == null)
			requester.send("GETCONVERSATION []\n");
		else
			requester.send(String.format("GETCONVERSATION %s\n", Utils.toJSON(response)));
	}

	@SuppressWarnings("unused")
	private void cmdCREATEPOST(Client requester, String input) {
		Post newpost = Utils.fromJSON(input, Post.class);
		Post response = dbhandler.createPost(newpost);
		if(response == null)
			requester.send("ERROR Error occured while sending topic");
		else {
			for (Iterator<Client> iterator = clients.values().iterator(); iterator.hasNext();) {
				Client client = (Client) iterator.next();
				client.send(String.format("NEWPOST %s\n", Utils.toJSON(response)));
			}
		}
	}

	@SuppressWarnings("unused")
	private void cmdSENDPM(Client requester, String input) {
		PrivateMessage newpm = Utils.fromJSON(input, PrivateMessage.class);
		PrivateMessage response = dbhandler.sendPM(newpm);
		if(response == null)
			requester.send("ERROR Private Message couldn't be sent\n");
		else
			requester.send("SUCCESS Private Message has been sent\n");
	}

	@SuppressWarnings("unused")
	private void cmdLSPOST(Client requester, String input) {
		Topic topic = Utils.fromJSON(input, Topic.class);
		Post[] response = dbhandler.getPost(topic);
		if(response == null) // boyle bir sey mumkun degil
			requester.send("ERROR nasil bir hataya dustun sen reyiz\n");
		else
			requester.send(String.format("LSPOST %s\n", Utils.toJSON(response)));
	}

	@SuppressWarnings("unused")
	private void cmdLSTOPIC(Client requester, String input) {
		User user = Utils.fromJSON(input, User.class);
		Topic[] response = dbhandler.getTopicList(user);
		if(response == null)
			requester.send("ERROR There is no topic created.\n");
		else
			requester.send(String.format("LSTOPIC %s\n", Utils.toJSON(response)));
	}

	@SuppressWarnings("unused")
	private void cmdLSUSER(Client requester, String input) {
		User user = Utils.fromJSON(input, User.class);
		User[] response = dbhandler.userList(user); // TODO userlist degisecek
		if(response == null)
			requester.send("ERROR You are the only user in the whole universe");
		else
			requester.send(String.format("LSUSER %s\n", Utils.toJSON(response)));
	}

	@SuppressWarnings("unused")
	private void cmdLOGOUT(Client requester, String input) {
		clients.remove(requester.hashCode());
		requester.close();
		loger.log(String.format("[%s][INFO] Disconnection from IP Address: %s, Session ID: %d", Utils.getDate(), requester.ipaddr(), requester.sessionid));
	}

	@SuppressWarnings("unused")
	private void cmdRECONNECT(Client requester, String input) {
		int session = Integer.parseInt(input);
		// Close and remove the old client
		clients.get(session).close();
		clients.remove(session);
		// Put new client
		clients.put(session, requester);
		// Remove duplicate
		clients.remove(requester.hashCode());
		// Replace new session id with the old one
		requester.sessionid = session;
		requester.send(String.format("RECONNECT %s\n", session));
	}

	private class Client implements Runnable {
		private BufferedReader reader;
		private BufferedWriter writer;
		private Socket socket;
		private int sessionid;

		public Client(Socket socket, int session) {
			this.socket = socket;
			this.sessionid = session;
			try {
				this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (IOException e) {
				loger.log(String.format("[%s][ERROR] IOException in Client(%d).Client: %s", Utils.getDate(), sessionid, e.getMessage()));
			}
		}

		public void send(String arg) {
			try {
				this.writer.write(arg);
				this.writer.flush();
			} catch (IOException e) {
				loger.log(String.format("[%s][ERROR] IOException in Client(%d).send: %s", Utils.getDate(), sessionid, e.toString()));
			}
		}

		public void close(){
			try {
				reader.close();
				writer.close();
				socket.close();
			} catch (IOException e) {
				loger.log(String.format("[%s][ERROR] IOException in Client(%d).close: %s", Utils.getDate(), sessionid, e.toString()));
			}
		}

		@Override
		protected void finalize() throws Throwable {
			try {
				this.close();
			} finally {
				super.finalize();
			}
		}

		public String ipaddr() {
			return this.socket.getInetAddress().getHostAddress();
		}

		@Override
		public int hashCode() {
			return this.sessionid;
		}

		@Override
		public void run() {
			String input;
			try {
				while((input = reader.readLine()) != null) {
					handleInput(input, this.sessionid);
				}
			} catch (IOException e) {
				// BufferedReader will throw an exception after closed, so just ignore it
				loger.log(String.format("[%s][ERROR] IOException in Client(%d).run: %s", Utils.getDate(), sessionid, e.getMessage()));
			}
		}
	}
}
