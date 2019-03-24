package belenot.filetransport;

import java.io.*;
import java.nio.*;
import java.util.*;

public class ClientQuery {
	
	public ClientQuery(InputStream in)
		throws IllegalArgumentException, IOException {
		//Сделать парсинг потока
			
			
			
	}

	public ClientCommand getClientCommand() { return clientCommand; }

	public Map<String, String> getData() { return data; }

	private ClientCommand clientCommand;
	private Map<String, int[]> data = new HashMap<>();
}
		
