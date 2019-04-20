package com.belenot.filetransport;

import java.io.*;
import java.util.*;
import java.util.function.*;
import com.belenot.filetransport.util.Bytesalizable;

public class ClientQuery implements Bytesalizable<ClientQuery> {
	public ClientQuery() { }
	public ClientQuery(ClientCommand command) {
		clientCommand = command;
	}
	public ClientQuery(String strCommand) {
		clientCommand = ClientCommand.valueOf(strCommand);
	}

	public ClientCommand getClientCommand() { return clientCommand; }

	public Map<String, String> getHeaders() { return headers; }
	public byte[] getData() { return data; }
	public ClientQuery setData(byte[] bytes) {data = bytes; return this; }

	@Override
	public ClientQuery fillObject(byte[] bytes) throws IOException, IllegalArgumentException{
		String str = "";
		int b;
		int headerCount = 0;
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		while( (b = in.read()) != -1 && b != '\n') str += (char) b;
		try {
		    clientCommand = ClientCommand.valueOf(str);
		} catch (IllegalArgumentException exc) { throw new IOException(exc); }
		str = "";
		while( (b = in.read()) != -1 && b != '\n') str += (char) b;
		try {
			headerCount = Integer.parseInt(str);
		} catch (NumberFormatException exc) { throw new IOException(exc); }
		str = "";
		for(int i = 0; i < headerCount; i++) {
			str = "";
			while( (b = in.read()) != -1 && b != '\n') str += (char) b;
			String key = str.split(":")[0];
			String value = str.split(":")[1];
			headers.put(key, value);
		}	
		data = new byte[in.available()];
		in.read(data);
		return this;
	}

	@Override
	public byte[] getBytes() throws IOException, IllegalStateException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		String str = clientCommand.toString() + "\n" + headers.size() + "\n";
		for(Map.Entry<String, String> entry : headers.entrySet())
			str += entry.getKey() + ":" + entry.getValue() + "\n";
	    byteStream.write(str.getBytes(), 0, str.getBytes().length);
		if (data != null && data.length > 0)
			byteStream.write(data, 0, data.length);
		return byteStream.toByteArray();
	}

	private ClientCommand clientCommand;
	private Map<String, String> headers = new HashMap<>();
	private byte[] data;
}
		
