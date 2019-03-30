package belenot.filetransport.services;

import belenot.filetransport.*;
import java.util.*;
import java.util.function.*;
import java.nio.file.*;
import java.io.*;

public class MkDir implements Function<ClientQuery, ServerResponse> {
	@Override
	public ServerResponse apply(ClientQuery clientQuery) {
		ServerResponse serverResponse = null;
		try {
			String dirname = clientQuery.getHeaders().get("dirname");
			mkdir(dirname);
			serverResponse = new ServerResponse(ResponseCode.ALLOW);
		} catch (Exception exc) {
			serverResponse = new ServerResponse(ResponseCode.DENY);
			serverResponse.getHeaders().put("exception", exc.toString());
		}
		return serverResponse;
	}

	private void mkdir(String dirname)
		throws FileAlreadyExistsException, IOException, SecurityException {
		System.out.println("Stud: try to mkdir");
		Path dirPath = Paths.get(dirname);
		Files.createDirectory(dirPath);
	}
}
	
