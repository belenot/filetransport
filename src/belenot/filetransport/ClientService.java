package belenot.filetransport;

import java.util.*;
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
		System.out.println("Command is " + clientCommand);
		switch (clientQuery.getClientCommand()) {
		case SAVE: (new Saver()).apply(clientQuery); break;
		case LOAD: (new Loader()).apply(clientQuery); break;
		default:
			throw new IllegalArgumentException("Unsupported command: " + clientQuery.getClientCommand());
		}
	}


	@Override
	public void run () {
		System.out.println("Run: " + socket.toString());
		try {
			ClientQuery query = new ClientQuery(socket.getInputStream());
			serv(query);
		} catch (IOException exc) {
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

	
