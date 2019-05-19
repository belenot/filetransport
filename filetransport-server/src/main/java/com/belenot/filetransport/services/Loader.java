package com.belenot.filetransport.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.Function;

import com.belenot.filetransport.ClientQuery;
import com.belenot.filetransport.ResponseCode;
import com.belenot.filetransport.ServerResponse;
import com.belenot.filetransport.util.logging.ServerLogger;

public class Loader implements Function<ClientQuery, ServerResponse> {
    ServerLogger serverLogger;
    public void setServerLogger(ServerLogger serverLogger) { this.serverLogger = serverLogger; }
    public ServerResponse apply(ClientQuery clientQuery) {
	ServerResponse serverResponse = null;
	try {
	    String filename = clientQuery.getHeaders().get("filename");
	    //serverLogger.log(Level.INFO, "Filename: " + filename);
	    byte[] data = load(filename);
	    //serverLogger.log(Level.INFO, "Data: " + data);
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
	fin.close();
	return data;
    }
}
