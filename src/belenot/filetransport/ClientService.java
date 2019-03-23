package belenot.filetransport;

import java.util.*;
import java.net.*;
import java.io.*;

public class ClientService implements Runnable {
	private Socket socket;
	
	ClientService (Socket s) {
		socket = s;
	}

	public void write(Map<String, String> data){
		System.out.println("Save: " + socket);
		System.out.println("\tData length: " + data.size());
		System.out.println("\tData:\n\t" + data);
		
	}
	public Map<String, String> load(Map<String, String> data) {
		System.out.println("Load: " + socket);
		System.out.println("\tData length: " + data.size());
		System.out.println("\tData:\n\t" + data);
		return data;
	}

	@Override
	public void run () {
		System.out.println("Run: " + socket.toString());
		try {
			ClientQuery query = new ClientQuery(socket.getInputStream());
			ClientCommand clientCommand = query.getClientCommand();
			System.out.println("Command is " + clientCommand);
			switch (query.getClientCommand()) {
			case SAVE: write(query.getData()); break;
			case LOAD: load(query.getData()); break;
			default: System.err.println("Unsupported command: " + query.getClientCommand());
			}
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

	
