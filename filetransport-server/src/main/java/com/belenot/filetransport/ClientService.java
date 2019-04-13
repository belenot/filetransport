package com.belenot.filetransport;

import java.util.*;
import java.util.function.*;
import java.util.logging.*;
import java.net.*;
import java.io.*;
import com.belenot.filetransport.services.*;
import com.belenot.filetransport.util.logging.*;

public class ClientService implements Runnable {
	private Socket socket;
	private ServerLogger logger = new ServerLogger();
	
	ClientService (Socket s) {
		socket = s;
	}

    private ServerResponse serv(ClientQuery clientQuery) throws IllegalArgumentException {
		if (clientQuery == null) throw new NullPointerException("clientQuery is null");
		ClientCommand clientCommand = clientQuery.getClientCommand();
		ServerResponse serverResponse = null;
		//System.out.println("Command is " + clientCommand);
		logger.log(Level.INFO, "Command is " + clientCommand);
		Function<ClientQuery, ServerResponse> function = null;
		switch (clientQuery.getClientCommand()) {
		case SAVE: function = new Saver(); break;
		case LOAD: function = new Loader(); break;
		case LISTTREE: function = new ListTree(); break;
		case MKDIR: function = new MkDir(); break;
		case DELETTER: function = new Deletter(); break;
		case STOP: function = (query) -> new ServerResponse(ResponseCode.ALLOW); break;
		default:
			throw new IllegalArgumentException("Unsupported command: " + clientQuery.getClientCommand());
		}
		if (function != null)
			serverResponse = function.apply(clientQuery);
		return serverResponse;
	}


	@Override
	public void run () {
		//System.out.println("Run: " + socket.toString());
		logger.log(Level.INFO, "Run: " + socket.toString());
		ClientQuery query = null;
		ServerResponse response = null;
		try {
			do {
				query = null;
				try {
				    byte firstByte = (byte) socket.getInputStream().read();
					byte[] bytes = new byte[socket.getInputStream().available() + 1];
					bytes[0] = firstByte;
					socket.getInputStream().read(bytes, 1, bytes.length - 1);
					query = (new ClientQuery()).fillObject(bytes);
					//query = (ClientQuery) new ObjectInputStream(socket.getInputStream()).readObject();
				}
				catch (IOException exc) {
					//System.err.println("Error while reading stream:\n" + exc);
					logger.log(Level.WARNING, "Error while reading stream:\n" + exc);
				}
				catch (IllegalArgumentException exc) {
					//System.err.println("Wrong argumment:\n" + exc);
					logger.log(Level.WARNING, "Wrong argumment:\n" + exc);
				}
				try {
					ServerResponse serverResponse = serv(query);
					socket.getOutputStream().write(serverResponse.getBytes());
				}
				catch (IOException | NullPointerException exc) {
					//System.err.println("Can't response to client:\n" + exc);
					logger.log(Level.WARNING, "Can't response to client:\n" + exc);
				}
			} while(query != null && query.getClientCommand() != ClientCommand.STOP);
		}
		finally {
			try {
				socket.close();
			}
			catch (IOException exc) {
				//System.err.println("Error to close socket:\n" + exc);
				logger.log(Level.WARNING, "Error to close socket:\n" + exc);
			}
		}
	}

	
}

	