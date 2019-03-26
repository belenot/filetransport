package belenot.filetransport;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
	public static ServerResponse write(String strCommand,String[] args) {
		ClientQuery clientQuery = null;
		try (Socket socket = new Socket("localhost", 5678)){
			switch (strCommand) {
			case "LOAD": clientQuery = newLoadQuery(args[0]); break;
			case "SAVE": clientQuery = newSaveQuery(args[0], args[1]); break;
			case "LISTTREE": clientQuery = newListTreeQuery(args[0]); break;
			default: System.err.println("UnknownCommand");
			}
			if (clientQuery == null) return null;
			(new ObjectOutputStream(socket.getOutputStream())).writeObject(clientQuery);
			ServerResponse serverResponse = (ServerResponse) (new ObjectInputStream(socket.getInputStream())).readObject();
			return serverResponse;
		} catch (ClassNotFoundException exc) {
			System.err.println("Can't resolve server response");
		} catch (IOException exc) {
			System.err.println("Can't connect to server:\n" + exc);
		}
		System.out.println("wrote");
		return null;
	}
		
	public static ClientQuery newSaveQuery (String filename, String newFilename)
		throws IOException, FileNotFoundException{
		FileInputStream in = new FileInputStream(filename);
		byte[] data = new byte[in.available()];
		in.read(data);
		ClientQuery clientQuery = new ClientQuery(ClientCommand.SAVE.toString());
		clientQuery.getHeaders().put("filename", newFilename);
		clientQuery.setData(data);
		return clientQuery;
	}

	public static ClientQuery newLoadQuery(String filename) {
		ClientQuery clientQuery = new ClientQuery(ClientCommand.LOAD);
		clientQuery.getHeaders().put("filename", filename);
		return clientQuery;
	}

	public static ClientQuery newListTreeQuery(String filename) {
		ClientQuery clientQuery = new ClientQuery(ClientCommand.LISTTREE);
		clientQuery.getHeaders().put("filename", filename);
		return clientQuery;
	}
	
	public static void main(String[] args) {
		ServerResponse serverResponse = write(args[0], Arrays.copyOfRange(args, 1, args.length));
		System.out.println(serverResponse.getResponseCode());
		if (serverResponse.getData() != null)
			System.out.println(new String(serverResponse.getData()));
	}

}
