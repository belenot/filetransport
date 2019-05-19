package com.belenot.filetransport;

import java.net.Socket;

import com.belenot.filetransport.util.logging.ServerLogger;

public class ClientServiceFactory {
    
    private ServerLogger serverLogger;
    public void setServerLogger(ServerLogger serverLogger) { this.serverLogger = serverLogger; }
    
    public ClientService newClientService(Socket socket) {
	ClientService clientService = new ClientService(socket);
	clientService.setServerLogger(serverLogger);
	return clientService;
    }
}
