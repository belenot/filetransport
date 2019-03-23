package belenot.filetransport;

import java.io.*;
import java.nio.*;
import java.util.*;

public class ClientQuery {
	
	public ClientQuery(InputStreamReader in)
		throws IllegalArgumentException, IOException {
		int length = 0;
		int maxLength = ClientCommand.maxLength();
		int c;
		String strCommand = "";
		
		while ( (c = in.read()) != -1 && c != 0) {
			length++;
			strCommand += c;
			try {
				clientCommand = ClientCommand.valueOf(strCommand);
				break;
			} catch (IllegalArgumentException exc) { }
		}
		if (length == 0 || length == maxLength && strCommand.equals(""))
			throw new IllegalArgumentException("Incorrect comand: " + strCommand);
		while(c != -1) {
			c = in.read();
			String key = "";
			String value = "";
			while(c != -1 && c != 0)
				c = in.read();
				key += c;
			while(c != -1 && c != 0)
				c = in.read();				
				value += c;
			if (key.equals("") || value.equals(""))
				throw new IllegalArgumentException("Wrong data format");
			data.put(key, value);
		}
	}

	public ClientCommand getClientCommand() { return clientCommand; }

	public Map<String, String> getData() { return data; }

	private ClientCommand clientCommand;
	private Map<String, String> data = new HashMap<>();
}
		
