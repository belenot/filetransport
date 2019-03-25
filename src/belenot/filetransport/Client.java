package belenot.filetransport;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
	public static void write(String strCommand,String[] args) {
		ClientQuery clientQuery = null;
		try (Socket socket = new Socket("localhost", 5678)){
			switch (strCommand) {
			case "LOAD": clientQuery = null; break;
			case "SAVE": clientQuery = newSaveQuery(args[0], args[1]);
			}
			if (clientQuery != null)			
				(new ObjectOutputStream(socket.getOutputStream())).
					writeObject(clientQuery);
		} catch (IOException exc) {
			System.out.println("Can't connect to server:\n" + exc);
		}
		System.out.println("wrote");
		
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
			
	
	public static void main(String[] args) {
		write(args[0], Arrays.copyOfRange(args, 1, args.length));
	}

}
