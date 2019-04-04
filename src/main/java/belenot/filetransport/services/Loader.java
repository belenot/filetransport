package belenot.filetransport.services;

import belenot.filetransport.*;
import belenot.filetransport.util.logging.*;
import java.util.stream.*;
import java.util.function.*;
import java.util.logging.*;
import java.io.*;

public class Loader implements Function<ClientQuery, ServerResponse> {
	Logger logger = new ServerLogger();
	public ServerResponse apply(ClientQuery clientQuery) {
		ServerResponse serverResponse = null;
		try {
			String filename = clientQuery.getHeaders().get("filename");
			//System.out.println("Filename: " + filename);
			logger.log(Level.INFO, "Filename: " + filename);
			byte[] data = load(filename);
			//System.out.println("Data: " + data);
			logger.log(Level.INFO, "Data: " + data);
			serverResponse = new ServerResponse(ResponseCode.ALLOW);
			serverResponse.setData(data);
			return serverResponse;
		} catch (IOException | NullPointerException exc) {
			serverResponse = new ServerResponse(ResponseCode.DENY);
			serverResponse.getHeaders().put("exception", exc.toString());
			return serverResponse;
		}
			
	}

	private byte[] load(String filename) throws IOException, FileNotFoundException {
		FileInputStream fin = new FileInputStream(filename);
		byte[] data = new byte[fin.available()];
		fin.read(data);
		return data;
	}
}
