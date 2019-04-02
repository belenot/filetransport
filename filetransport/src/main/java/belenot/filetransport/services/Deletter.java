package belenot.filetransport.services;

import java.util.function.*;
import java.nio.file.*;
import java.io.IOException;
import belenot.filetransport.*;

public class Deletter implements Function<ClientQuery, ServerResponse> {
	@Override
	public ServerResponse apply(ClientQuery clientQuery) {
		ServerResponse serverResponse = null;
		try {
			String deleteFilename = clientQuery.getHeaders().get("filename");
			Path deletePath = Paths.get(deleteFilename);
			Files.delete(deletePath);
			serverResponse = new ServerResponse(ResponseCode.ALLOW);
			serverResponse.getHeaders().put("filename", deleteFilename);
		} catch (Exception exc) {
			serverResponse = new ServerResponse(ResponseCode.DENY);
			serverResponse.getHeaders().put("exception", exc.toString());
		}
		return serverResponse;
	}
}
	
