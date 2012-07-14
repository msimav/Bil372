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

	// test function
	private void cmdECHO(Client requester, String input) {
		requester.send(String.format("INPUT: %s\n", input));
	}

	private void cmdDB(Client requester, String input) {
		requester.send(String.format("OUTPUT: %s\n", dbhandler.test(input)));
	}

	private void cmdLOGOUT(Client requester, String input) {
		clients.remove(requester.hashCode());
		requester.close();
		loger.log(String.format("[%s][INFO] Disconnection from IP Address: %s, Session ID: %d", Utils.getDate(), requester.ipaddr(), requester.sessionid));
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
