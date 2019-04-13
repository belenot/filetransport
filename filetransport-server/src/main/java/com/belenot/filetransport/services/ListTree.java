package com.belenot.filetransport.services;

import com.belenot.filetransport.*;
import com.belenot.filetransport.util.logging.*;
import java.util.stream.*;
import java.util.function.*;
import java.util.logging.*;
import java.io.*;
import java.nio.file.*;

public class ListTree implements Function<ClientQuery, ServerResponse> {
	ServerLogger logger = new ServerLogger();
	public ServerResponse apply(ClientQuery clientQuery) {
		ServerResponse serverResponse = null;
		String rootDirectory = clientQuery.getHeaders().get("filename");
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			Files.walk(Paths.get(rootDirectory))
				.map( (path) -> (path.toString() + "\n").getBytes())
				.forEach( (bytes) -> byteStream.write(bytes, 0, bytes.length));
			serverResponse = new ServerResponse(ResponseCode.ALLOW);
			serverResponse.getHeaders().put("filename", rootDirectory);
			serverResponse.setData(byteStream.toByteArray());
		} catch (IOException exc) {
			//System.err.println("Exception during walking root directory:\n" + exc);
			logger.log(Level.WARNING, "Exception during walking root directory:\n" + exc);
			serverResponse = new ServerResponse(ResponseCode.DENY);//Or better add ERROR?
		}
		return serverResponse;
	}
}
