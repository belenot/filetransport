package com.belenot.filetransport.services;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import com.belenot.filetransport.ClientQuery;
import com.belenot.filetransport.ResponseCode;
import com.belenot.filetransport.ServerResponse;
import com.belenot.filetransport.util.logging.ServerLogger;



public class MkDir implements Function<ClientQuery, ServerResponse> {
    ServerLogger serverLogger;
    public void setServerLogger(ServerLogger serverLogger) { this.serverLogger = serverLogger; }
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
	//serverLogger.log(Level.INFO, "Stub: try to mkdir");
	Path dirPath = Paths.get(dirname);
	Files.createDirectory(dirPath);
    }
}
	
