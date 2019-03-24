package belenot.filetransport;

import java.io.*;
import java.util.*;
import java.util.function.*;

public class ClientQuery {
	
	public ClientQuery() {
		
	}
	

	public ClientCommand getClientCommand() { return clientCommand; }

	public Map<String, byte[]> getHeaders() { return headers; }
	public byte[] data getData() { return data; }

	private ClientCommand clientCommand;
	private Map<String, String> headers = new HashMap<>();
	private byte[] data;
}
		
