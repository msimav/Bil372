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

import javax.jws.soap.SOAPBinding.Use;

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
		loger.log(String.format("[%s][INFO] Server initilazed.", Utils.getDateTime()));

		// Start DBHandler
		dbhandler = DBHandler.getInstance();
	}

	public void run() {
		Socket socket;

		try {
			server = new ServerSocket(port);
			loger.log(String.format("[%s][INFO] Server is running on %s:%d.", Utils.getDateTime(), server
					.getInetAddress().getHostAddress(), port));
			while (true) {
				socket = server.accept();
				loger.log(String.format("[%s][INFO] New connection from %s", Utils.getDateTime(), socket.getInetAddress().getHostAddress()));
				Client client = new Client(socket, sessionCounter);
				clients.put(sessionCounter, client);
				loger.log(String.format("[%s][INFO] Session ID of %s is %d", Utils.getDateTime(), client.ipaddr(), client.sessionid));
				new Thread(client).start();
				sessionCounter++;
			}
		} catch (IOException e) {
			loger.log(String.format("[%s][ERROR] IOException in Server.run: %s", Utils.getDateTime(), e.getMessage()));
		}
	}

	private void shutdown() {
		loger.log(String.format("[%s][INFO] Server is shuting down.", Utils.getDateTime()));

		//DBHandler
		dbhandler.close();
		loger.log(String.format("[%s][INFO] DBHandler is closed.", Utils.getDateTime()));

		// Clients
		loger.log(String.format("[%s][INFO] Sending shutdown signal to Clients.", Utils.getDateTime()));
		for (Iterator<Client> iterator = clients.values().iterator(); iterator.hasNext();) {
			Client client = (Client) iterator.next();
			client.send("SHUTDOWN Server is shuting down\n"); // TODO Shutdown message
			client.close();
		}

		// ServerSocket
		try {
			server.close();
			loger.log(String.format("[%s][INFO] ServerSocket is closed successfully.", Utils.getDateTime()));
		} catch (IOException e) {
			loger.log(String.format("[%s][ERROR] ServerSocket is not closed successfully.", Utils.getDateTime()));
			loger.log(String.format("[%s][ERROR] IOException in Server.shutdown: %s", Utils.getDateTime(), e.getMessage()));
		}

		// Log
		loger.log(String.format("[%s][INFO] Shutdown completed.", Utils.getDateTime()));
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
			loger.log(String.format("[%s][ERROR] SecurityException in Server.handleInput: %s", Utils.getDateTime(), e.getMessage()));
			loger.log(String.format("[%s][ERROR] SecurityException Session ID: %d, IP Adres: %s", Utils.getDateTime(), requester.sessionid, requester.ipaddr()));
		} catch (NoSuchMethodException e) {
			loger.log(String.format("[%s][ERROR] NoSuchMethodException in Server.handleInput: %s", Utils.getDateTime(), e.getMessage()));
			loger.log(String.format("[%s][ERROR] NoSuchMethodException Session ID: %d, IP Adres: %s", Utils.getDateTime(), requester.sessionid, requester.ipaddr()));
			requester.send("ERROR no such command exist\n");
		} catch (IllegalArgumentException e) {
			loger.log(String.format("[%s][ERROR] IllegalArgumentException in Server.handleInput: %s", Utils.getDateTime(), e.getMessage()));
			loger.log(String.format("[%s][ERROR] IllegalArgumentException Session ID: %d, IP Adres: %s", Utils.getDateTime(), requester.sessionid, requester.ipaddr()));
		} catch (IllegalAccessException e) {
			loger.log(String.format("[%s][ERROR] IllegalAccessException in Server.handleInput: %s", Utils.getDateTime(), e.getMessage()));
			loger.log(String.format("[%s][ERROR] IllegalAccessException Session ID: %d, IP Adres: %s", Utils.getDateTime(), requester.sessionid, requester.ipaddr()));
		} catch (InvocationTargetException e) {
			loger.log(String.format("[%s][ERROR] InvocationTargetException in Server.handleInput: %s", Utils.getDateTime(), e.getMessage()));
			loger.log(String.format("[%s][ERROR] InvocationTargetException Session ID: %d, IP Adres: %s", Utils.getDateTime(), requester.sessionid, requester.ipaddr()));
			loger.log(String.format("[%s][ERROR] InvocationTargetException cmd: %s, params: %s", Utils.getDateTime(), cmdparam[0], cmdparam[1]));
		}
	}

	@SuppressWarnings("unused")
	private void cmdLOGIN(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][LOGIN] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
		User login = Utils.fromJSON(input, User.class);
		User response = dbhandler.login(login);
		String proResponse;
		if(response == null)
			proResponse = "LOGIN -1 Login Error\n";
		else {
			proResponse = String.format("LOGIN %d %s\n", requester.sessionid, Utils.toJSON(response));
			dbhandler.loginLog(response, requester.ipaddr());
		}
		loger.log(String.format("[%s][PROTOCOL][LOGIN] Requester: %s(%d), Response: %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, proResponse));
		requester.send(proResponse);
	}

	@SuppressWarnings("unused")
	private void cmdREGISTER(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][REGISTER] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
		User newuser = Utils.fromJSON(input, User.class);
		User response = dbhandler.register(newuser);
		String proResponse;
		if(response == null)
			proResponse ="REGISTER -1 Register Failed\n";
		else
			proResponse = String.format("REGISTER 0 %s\n", Utils.toJSON(response));
		loger.log(String.format("[%s][PROTOCOL][REGISTER] Requester: %s(%d), Response: %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, proResponse));
		requester.send(proResponse);
	}

	@SuppressWarnings("unused")
	private void cmdCREATETOPIC(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][CREATETOPIC] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
		Topic newtopic = Utils.fromJSON(input, Topic.class);
		Topic response = dbhandler.createTopic(newtopic);
		if(response == null) {
			loger.log(String.format("[%s][PROTOCOL][CREATETOPIC] Requester: %s(%d), Response: ERROR\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid));
			requester.send("ERROR Topic creation failed\n");
		}
		else {
			String proBroadcast = String.format("NEWTOPIC %s\n", Utils.toJSON(response));
			loger.log(String.format("[%s][PROTOCOL][CREATETOPIC] Requester: %s(%d), Broadcast: %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, proBroadcast));
			for (Iterator<Client> iterator = clients.values().iterator(); iterator.hasNext();) {
				Client client = (Client) iterator.next();
				client.send(proBroadcast);
			}
		}
	}

	@SuppressWarnings("unused")
	private void cmdLSPM(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][LSPM] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
		User user = Utils.fromJSON(input, User.class);
		PrivateMessage[] response = dbhandler.getPMs(user);
		String proResponse;
		if(response == null)
			proResponse = "LSPM []\n";
		else
			proResponse = String.format("LSPM %s\n", Utils.toJSON(response));
		loger.log(String.format("[%s][PROTOCOL][LSPM] Requester: %s(%d), Response: %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, proResponse));
		requester.send(proResponse);
	}

	@SuppressWarnings("unused")
	private void cmdGETCONVERSATION(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][GETCONVERSATION] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
		User[] tuple = Utils.fromJSON(input, User[].class);
		PrivateMessage[] response = dbhandler.getPMdetails(tuple[0], tuple[1]);
		String proResponse;
		if(response == null)
			proResponse = "GETCONVERSATION []\n";
		else
			proResponse = String.format("GETCONVERSATION %s\n", Utils.toJSON(response));
		loger.log(String.format("[%s][PROTOCOL][GETCONVERSATION] Requester: %s(%d), Response: %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, proResponse));
		requester.send(proResponse);
	}

	@SuppressWarnings("unused")
	private void cmdCREATEPOST(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][CREATEPOST] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
		Post newpost = Utils.fromJSON(input, Post.class);
		Post response = dbhandler.createPost(newpost);
		if(response == null) {
			loger.log(String.format("[%s][PROTOCOL][CREATEPOST] Requester: %s(%d), Response: ERROR\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid));
			requester.send("ERROR Error occured while sending topic");
		}
		else {
			String proBroadcast = String.format("NEWPOST %s\n", Utils.toJSON(response));
			loger.log(String.format("[%s][PROTOCOL][CREATEPOST] Requester: %s(%d), Broadcast: %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, proBroadcast));
			for (Iterator<Client> iterator = clients.values().iterator(); iterator.hasNext();) {
				Client client = (Client) iterator.next();
				client.send(proBroadcast);
			}
		}
	}

	@SuppressWarnings("unused")
	private void cmdSENDPM(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][SENDPM] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
		PrivateMessage newpm = Utils.fromJSON(input, PrivateMessage.class);
		PrivateMessage response = dbhandler.sendPM(newpm);
		String proResponse;
		if(response == null)
			proResponse = "ERROR Private Message couldn't be sent\n";
		else
			proResponse = "SUCCESS Private Message has been sent\n";
		loger.log(String.format("[%s][PROTOCOL][SENDPM] Requester: %s(%d), Response: %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, proResponse));
		requester.send(proResponse);
	}

	@SuppressWarnings("unused")
	private void cmdLSPOST(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][LSPOST] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
		Topic topic = Utils.fromJSON(input, Topic.class);
		Post[] response = dbhandler.getPost(topic);
		String proResponse;
		if(response == null) // boyle bir sey mumkun degil
			proResponse = "ERROR nasil bir hataya dustun sen reyiz\n";
		else
			proResponse = String.format("LSPOST %s\n", Utils.toJSON(response));
		loger.log(String.format("[%s][PROTOCOL][LSPOST] Requester: %s(%d), Response: %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, proResponse));
		requester.send(proResponse);
	}

	@SuppressWarnings("unused")
	private void cmdLSTOPIC(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][LSTOPIC] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
		User user = Utils.fromJSON(input, User.class);
		Topic[] response = dbhandler.getTopicList(user);
		String proResponse;
		if(response == null)
			proResponse = "ERROR There is no topic created.\n";
		else
			proResponse = String.format("LSTOPIC %s\n", Utils.toJSON(response));
		loger.log(String.format("[%s][PROTOCOL][LSTOPIC] Requester: %s(%d), Response: %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, proResponse));
		requester.send(proResponse);
	}

	@SuppressWarnings("unused")
	private void cmdLSUSER(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][LSUSER] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
		User user = Utils.fromJSON(input, User.class);
		User[] response = dbhandler.userList(user);
		String proResponse;
		if(response == null)
			proResponse = "ERROR You are the only user in the whole universe";
		else
			proResponse = String.format("LSUSER %s\n", Utils.toJSON(response));
		loger.log(String.format("[%s][PROTOCOL][LSUSER] Requester: %s(%d), Response: %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, proResponse));
		requester.send(proResponse);
	}

	@SuppressWarnings("unused")
	private void cmdUPDATEPASSWD(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][UPDATEPASSWD] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
		User[] tuple = Utils.fromJSON(input, User[].class);
		PrivateMessage[] response = dbhandler.getPMdetails(tuple[0], tuple[1]);
		String proResponse;
		if(response == null)
			proResponse = "GETCONVERSATION []\n";
		else
			proResponse = String.format("UPDATEUSER %s\n", Utils.toJSON(response));
		loger.log(String.format("[%s][PROTOCOL][UPDATEPASSWD] Requester: %s(%d), Response: %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, proResponse));
		requester.send(proResponse);
	}

	@SuppressWarnings("unused")
	private void cmdLOGOUT(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][LOGOUT] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
		clients.remove(requester.hashCode());
		requester.close();
		loger.log(String.format("[%s][INFO] Disconnection from IP Address: %s, Session ID: %d", Utils.getDateTime(), requester.ipaddr(), requester.sessionid));
	}

	@SuppressWarnings("unused")
	private void cmdRECONNECT(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][RECONNECT] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
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
		requester.send(String.format("RECONNECT %s\n", session)); // TODO RECONNECT protocol needs more review
	}

	@SuppressWarnings("unused")
	private void cmdUPDATEUSER(Client requester, String input) {
		loger.log(String.format("[%s][PROTOCOL][UPDATEUSER] Requester: %s(%d), Input %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, input));
		User update = Utils.fromJSON(input, User.class);
		User response = dbhandler.updateUser(update);
		String proResponse;
		if(response == null)
			proResponse = "ERROR Changes could not be applied.\n";
		else
			proResponse = String.format("UPDATEUSER %s\n", Utils.toJSON(response));
		loger.log(String.format("[%s][PROTOCOL][UPDATEUSER] Requester: %s(%d), Response: %s\n", Utils.getDateTime(), requester.ipaddr(), requester.sessionid, proResponse));
		requester.send(proResponse);
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
				loger.log(String.format("[%s][ERROR] IOException in Client(%d).Client: %s", Utils.getDateTime(), sessionid, e.getMessage()));
			}
		}

		public void send(String arg) {
			try {
				this.writer.write(arg);
				this.writer.flush();
			} catch (IOException e) {
				loger.log(String.format("[%s][ERROR] IOException in Client(%d).send: %s", Utils.getDateTime(), sessionid, e.toString()));
			}
		}

		public void close(){
			try {
				reader.close();
				writer.close();
				socket.close();
			} catch (IOException e) {
				loger.log(String.format("[%s][ERROR] IOException in Client(%d).close: %s", Utils.getDateTime(), sessionid, e.toString()));
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
				loger.log(String.format("[%s][ERROR] IOException in Client(%d).run: %s", Utils.getDateTime(), sessionid, e.getMessage()));
			}
		}
	}
}
