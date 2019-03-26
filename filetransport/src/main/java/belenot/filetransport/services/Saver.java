package belenot.filetransport.services;

import belenot.filetransport.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;

public class Saver implements Function<ClientQuery, ServerResponse> {
	public ServerResponse apply(ClientQuery clientQuery) {
		ServerResponse serverResponse = null;
		try {
			String filename = clientQuery.getHeaders().get("filename");
			saveData(filename, clientQuery.getData());
			serverResponse = new ServerResponse(ResponseCode.ALLOW);
		} catch (IOException exc) {
			serverResponse = new ServerResponse(ResponseCode.DENY);
			serverResponse.getHeaders().put("exception", exc.toString());
		}
		return serverResponse;
	}

	private void saveData(String filename, byte[] data)
		throws FileNotFoundException, IOException{
		FileOutputStream out = new FileOutputStream(new File(filename));
		out.write(data);
		out.close();
	}
}
