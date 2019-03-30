package belenot.filetransport;

import java.util.*;
import java.util.function.*;
import java.net.*;
import java.io.*;
import belenot.filetransport.services.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientService implements Runnable {
	private static final int MAX_LENGTH = 2048;
	private Socket socket;
	
	ClientService (Socket s) {
		socket = s;
	}

    private ServerResponse serv(ClientQuery clientQuery) throws IllegalArgumentException {
		if (clientQuery == null) throw new NullPointerException("clientQuery is null");
		ClientCommand clientCommand = clientQuery.getClientCommand();
		ServerResponse serverResponse = null;
		System.out.println("Command is " + clientCommand);
		Function<ClientQuery, ServerResponse> function = null;
		switch (clientQuery.getClientCommand()) {
		case SAVE: function = new Saver(); break;
		case LOAD: function = new Loader(); break;
		case LISTTREE: function = new ListTree(); break;
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
		System.out.println("Run: " + socket.toString());
		ClientQuery query = null;
		ServerResponse response = null;
		try {
			do {
				query = null;
				try {
					//query = (ClientQuery) new ObjectInputStream(socket.getInputStream()).readObject();
					byte[] bytes = new byte[MAX_LENGTH];
					socket.getInputStream().read(bytes);
					ObjectMapper objectMapper = new ObjectMapper();
					query = (ClientQuery) objectMapper.readValue(bytes, ClientQuery.class);
					
				}
				catch (IOException exc) {
					System.err.println("Error while reading stream:\n" + exc);
					exc.printStackTrace();
				}
				catch (IllegalArgumentException exc) {
					System.err.println("Wrong argumment:\n" + exc);
				}
				try {
					System.out.println(query);
					ServerResponse serverResponse = serv(query);
					System.out.println(query + "\n" + serverResponse);
					//(new ObjectOutputStream(socket.getOutputStream())).writeObject(serverResponse);
					ObjectMapper objectMapper = new ObjectMapper();
					ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
					objectMapper.writeValue(byteStream, serverResponse);
					socket.getOutputStream().write(byteStream.toByteArray());
				}
				catch (IOException | NullPointerException exc) {
					System.err.println("Can't response to client:\n" + exc);
				}
			} while(query != null && query.getClientCommand() != ClientCommand.STOP);
		}
		finally {
			try {
				socket.close();
			}
			catch (IOException exc) {
				System.err.println("Error to close socket:\n" + exc);
			}
		}
	}

	
}

	
