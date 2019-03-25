package belenot.filetransport;

import java.io.*;
import java.util.*;
import java.util.function.*;

public class ClientQuery implements Serializable {
	public ClientQuery() { }
	public ClientQuery(String strCommand) {
		clientCommand = ClientCommand.valueOf(strCommand);
	}

	public ClientCommand getClientCommand() { return clientCommand; }

	public Map<String, String> getHeaders() { return headers; }
	public byte[] getData() { return data; }
	public ClientQuery setData(byte[] bytes) {data = bytes; return this; }

	private ClientCommand clientCommand;
	private Map<String, String> headers = new HashMap<>();
	private byte[] data;
}
		
