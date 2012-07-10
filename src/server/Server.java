package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import util.Utils;

public class Server {

	private static final int port = 5001; // TODO: define the port number.

	private HashMap<Integer, Server.Client> clients;
	private Loger loger;
	private DBHandler dbhandler;
	private int sessionCounter = 1;

	public Server() {
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
		ServerSocket server;
		Socket socket;

		try {
			server = new ServerSocket(port);
			loger.log(String.format("[%s][INFO] Server is running on %s:%d.", Utils.getDate(), server
					.getInetAddress().getHostAddress(), port));
			while (true) {
				socket = server.accept();
				loger.log(String.format("[%s][INFO] New connection from %s", Utils.getDate(), socket.getInetAddress()));
				Client client = new Client(socket, sessionCounter);
				clients.put(sessionCounter, client);
				new Thread(client).run();
				loger.log(String.format("[%s][INFO] Session ID of %s is %d", Utils.getDate(), socket.getInetAddress(), sessionCounter));
				sessionCounter++;
			}
		} catch (IOException e) {
			loger.log(String.format("[%s][ERROR] IOException in Server.run: %s", Utils.getDate(), e.getMessage()));
		}
	}

	private void handleInput(String input, int sessionid) {
		Client requester;
		requester = clients.get(sessionid);
		if(input.equals("BYE")) {
			requester.send("Bye bye!\n");
			requester.close();
		} else {
			requester.send(input + '\n');
		}
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
				loger.log(String.format("[%s][ERROR] IOException in Client(%d).run: %s", Utils.getDate(), sessionid, e.getMessage()));
			}
		}
	}
}
