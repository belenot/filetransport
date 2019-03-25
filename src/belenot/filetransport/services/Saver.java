package belenot.filetransport.services;

import belenot.filetransport.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;

public class Saver implements Function<ClientQuery, ServerResponse> {
	public ServerResponse apply(ClientQuery clientQuery) {
		ServerResponse serverResponse = null;
		try {
			saveData(clientQuery);
			serverResponse = new ServerResponse(ResponseCode.ALLOW);
		} catch (IOException exc) {
			serverResponse = new ServerResponse(ResponseCode.DENY);
			serverResponse.getHeaders().put("exception", exc.toString());
		}
		return serverResponse;
	}

	private void saveData(ClientQuery clientQuery)
		throws FileNotFoundException, IOException{
		System.out.println("Save function:");
		System.out.println("\tcommand: " + clientQuery.getClientCommand());
		String filename = clientQuery.getHeaders().get("filename");
		System.out.println("Content to " + filename + ":\n" + new String(clientQuery.getData()));
		FileOutputStream out = new FileOutputStream(new File(filename));
		out.write(clientQuery.getData());
		out.close();
	}
}
