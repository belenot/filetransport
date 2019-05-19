package com.belenot.filetransport;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Function;
import java.util.logging.Level;

import com.belenot.filetransport.services.Deletter;
import com.belenot.filetransport.services.ListTree;
import com.belenot.filetransport.services.Loader;
import com.belenot.filetransport.services.MkDir;
import com.belenot.filetransport.services.Saver;
import com.belenot.filetransport.util.logging.ServerLogger;

public class ClientService implements Runnable {
    private Socket socket;
    private ServerLogger serverLogger;
    public void setServerLogger(ServerLogger serverLogger) { this.serverLogger = serverLogger; }
	
    ClientService (Socket s) {
	socket = s;
    }

    private ServerResponse serv(ClientQuery clientQuery) throws IllegalArgumentException {
	if (clientQuery == null) throw new NullPointerException("clientQuery is null");
	ClientCommand clientCommand = clientQuery.getClientCommand();
	ServerResponse serverResponse = null;
	serverLogger.log(Level.INFO, "Command is " + clientCommand);
	Function<ClientQuery, ServerResponse> function = null;
	switch (clientQuery.getClientCommand()) {
	case SAVE: function = new Saver(); break;
	case LOAD: function = new Loader(); break;
	case LISTTREE: function = new ListTree(); break;
	case MKDIR: function = new MkDir(); break;
	case DELETTER: function = new Deletter(); break;
	case STOP: function = (query) -> new ServerResponse(ResponseCode.ALLOW); break;
	default:
	    throw new IllegalArgumentException("Unsupported command: " + clientQuery.getClientCommand());
	}
	if (function != null)
	    serverResponse = function.apply(clientQuery);
	return serverResponse;
    }

    @Override
    public void run () {
	serverLogger.log(Level.INFO, "Run: " + socket.toString());
	ClientQuery query = null;
	try {
	    do {
		query = null;
		try {
		    byte firstByte = (byte) socket.getInputStream().read();
		    byte[] bytes = new byte[socket.getInputStream().available() + 1];
		    bytes[0] = firstByte;
		    socket.getInputStream().read(bytes, 1, bytes.length - 1);
		    query = (new ClientQuery()).fillObject(bytes);
		}
		catch (IOException exc) {
		    serverLogger.log(Level.WARNING, "Error while reading stream:\n" + exc);
		}
		catch (IllegalArgumentException exc) {
		    serverLogger.log(Level.WARNING, "Wrong argumment:\n" + exc);
		}
		try {
		    ServerResponse serverResponse = serv(query);
		    socket.getOutputStream().write(serverResponse.getBytes());
		}
		catch (IOException | NullPointerException exc) {
		    serverLogger.log(Level.WARNING, "Can't response to client:\n" + exc);
		}
	    } while(query != null && query.getClientCommand() != ClientCommand.STOP);
	}
	finally {
	    try {
		socket.close();
	    }
	    catch (IOException exc) {
		serverLogger.log(Level.WARNING, "Error to close socket:\n" + exc);
	    }
	}
    }

	
}

	
