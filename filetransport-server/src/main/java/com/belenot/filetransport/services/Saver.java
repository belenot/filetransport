package com.belenot.filetransport.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import com.belenot.filetransport.ClientQuery;
import com.belenot.filetransport.ResponseCode;
import com.belenot.filetransport.ServerResponse;

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
		Path file = Paths.get(filename).toAbsolutePath();
		Path parent = file.getParent().toAbsolutePath();
		if (parent != null && !Files.isDirectory(parent)) {
		    Files.createDirectories(parent);
		}
		FileOutputStream out = new FileOutputStream(new File(filename));
		out.write(data);
		out.close();
	}
}
