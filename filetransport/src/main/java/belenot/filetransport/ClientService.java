package belenot.filetransport;

import java.util.*;
import java.util.function.*;
import java.net.*;
import java.io.*;
import belenot.filetransport.services.*;

public class ClientService implements Runnable {
	private Socket socket;
	
	ClientService (Socket s) {
		socket = s;
	}

	public void serv(ClientQuery clientQuery) throws IllegalArgumentException {
		ClientCommand clientCommand = clientQuery.getClientCommand();
		ServerResponse serverResponse = null;
		System.out.println("Command is " + clientCommand);
		Function<ClientQuery, ServerResponse> function = null;
		switch (clientQuery.getClientCommand()) {
		case SAVE: function = new Saver(); break;
		case LOAD: function = new Loader(); break;
		case LISTTREE: function = new ListTree(); break;
		default:
			throw new IllegalArgumentException("Unsupported command: " + clientQuery.getClientCommand());
		}
		if (function != null)
			serverResponse = function.apply(clientQuery);
		try {
			(new ObjectOutputStream(socket.getOutputStream())).writeObject(serverResponse);
		} catch (IOException exc) {
			System.err.println("Can't response to client:\n" + exc);
		}
	}


	@Override
	public void run () {
		System.out.println("Run: " + socket.toString());
		try {
			ClientQuery query = (ClientQuery) new ObjectInputStream(socket.getInputStream()).readObject();
			serv(query);
		} catch (IOException | ClassNotFoundException exc) {
			System.err.println("Error while reading stream:\n" + exc);
		} catch (IllegalArgumentException exc) {
			System.err.println("Wrong argumment:\n" + exc);
		}
		finally {
			try {
				socket.close();
			} catch (IOException exc) {
				System.err.println("Error to close socket:\n" + exc);
			}
		}
	}

	
}

	
