package com.belenot.filetransport.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

import com.belenot.filetransport.ClientQuery;
import com.belenot.filetransport.ResponseCode;
import com.belenot.filetransport.ServerResponse;
import com.belenot.filetransport.util.logging.ServerLogger;

public class ListTree implements Function<ClientQuery, ServerResponse> {
    ServerLogger serverLogger;
    public void setServerLogger(ServerLogger serverLogger) { this.serverLogger = serverLogger; }
    
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
	    //serverLogger.log(Level.WARNING, "Exception during walking root directory:\n" + exc);
	    serverResponse = new ServerResponse(ResponseCode.DENY);//Or better add ERROR?
	}
	return serverResponse;
    }
}
