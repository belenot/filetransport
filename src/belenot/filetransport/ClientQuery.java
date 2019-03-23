package belenot.filetransport;

import java.io.*;
import java.nio.*;
import java.util.*;

public class ClientQuery {
	
	public ClientQuery(InputStream in)
		throws IllegalArgumentException, IOException {
		byte[] bytes = new byte[in.available()];
		in.read(bytes);
	    List<String> fields = new LinkedList<>();
		String field = "";
		for (byte b : bytes) {
			if(b == 0) {
				if(field.equals(""))
					throw new IllegalArgumentException("Wrong input data, expected field");
				fields.add(field);
				field = "";
			} else {
				field += (char) b;
			}
		}
		fields.add(field);
		System.out.println(fields);
		if(fields.size() == 0)
			throw new IllegalArgumentException("Null client query");
		if(fields.size() % 2 == 0)
			throw new IllegalArgumentException("Incorrect client query, expected value");
		try {
			clientCommand = ClientCommand.valueOf(fields.get(0));
		} catch (IllegalArgumentException exc) {
			throw new IllegalArgumentException("Incorrect client command");
		}
		for(int i = 1; i + 1 < fields.size(); i++) {
			data.put(fields.get(i), fields.get(i + 1));
		}	
			
	}

	public ClientCommand getClientCommand() { return clientCommand; }

	public Map<String, String> getData() { return data; }

	private ClientCommand clientCommand;
	private Map<String, String> data = new HashMap<>();
}
		
