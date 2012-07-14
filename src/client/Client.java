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

import util.Utils;
import beans.User;

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

	public Client(User login) {
		user = login;
		try {
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

			// Login
			send(String.format("LOGIN %s\n", Utils.toJSON(user)));
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

}