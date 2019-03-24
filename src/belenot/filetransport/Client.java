package belenot.filetransport;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
	public static void write(String strCommand,String[] headersLines) {
		try (Socket socket = new Socket("localhost", 5678)){
			ClientQuery query = new ClientQuery(strCommand);
			for (String headerLine : headersLines) {
				String key = headerLine.split(":")[0];
				String value = headerLine.split(":")[1];
				query.getHeaders().put(key, value);
			}
			(new ObjectOutputStream(socket.getOutputStream())).writeObject(query);
		} catch (IOException exc) {
			System.out.println("Can't connect to server:\n" + exc);
		}
		System.out.println("wrote");
		
	}
		
	
	public static void main(String[] args) {
		write(args[0], Arrays.copyOfRange(args, 1, args.length));
	}

}
