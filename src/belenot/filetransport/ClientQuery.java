package belenot.filetransport;

import java.io.*;
import java.util.*;
import java.util.function.*;

public class ClientQuery {
	
	public ClientQuery(InputStream in)
		throws IllegalArgumentException, IOException {
		byte[] buffer = new byte[256];
		int b = 0;
		int i = 0;
		while( (b = in.read()) != -1 && b != 0) {
			buffer[i++] = (byte) b;
		}
		try {
			clientCommand = ClientCommand.valueOf(new String(buffer, 0, i));
		} catch (IllegalArgumentException exc) {
			throw new IllegalArgumentException("Incorrect command: " + new String(buffer));
		}
		boolean stop = false;
		while(!stop) {
			buffer = new byte[256];
			i = 0;
			while( (b = in.read()) != -1 && b != 0) {
				buffer[i++] = (byte) b;
			}
			if (b == -1)break;
			String key = new String(buffer, 0, i);
			buffer = new byte[256];
			i = 0;
			while( (b = in.read()) != -1 && b != 0) {
				buffer[i++] = (byte) b;
			}
			byte[] value = Arrays.copyOf(buffer, i);
			data.put(key, value);
			if (b == -1)break;
		}
	}

	public ClientCommand getClientCommand() { return clientCommand; }

	public Map<String, byte[]> getData() { return data; }

	private ClientCommand clientCommand;
	private Map<String, byte[]> data = new HashMap<>();
}
		
