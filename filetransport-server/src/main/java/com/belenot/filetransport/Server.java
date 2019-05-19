package com.belenot.filetransport;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

import com.belenot.filetransport.util.logging.ServerLogger;

public class Server
    implements Runnable, CommandEventListener {

    private ServerLogger logger;
    public void setLogger(ServerLogger logger) { this.logger = logger; }

    private int soTimeout;
    public void setSoTimeout(int soTimeout) { this.soTimeout = soTimeout; }

    
    private ExecutorService executorService;
    public void setExecutorService(ExecutorService executorService) { this.executorService = executorService; }
    private ServerSocket serverSocket;
    public void setServerSocket(ServerSocket serverSocket) { this.serverSocket = serverSocket; }
	
    private boolean isStart = false;
    private Command command = Command.NORMAL_RUN;
	
    public Server() {
	//Config
    }

    public void start() {
	logger.log(Level.INFO, "Start server");
	if (isStart) return;
	try {
	    serverSocket.setSoTimeout(soTimeout);
	} catch (IOException | IllegalStateException exc) {
	    logger.log(Level.WARNING, "Error initialize:\n" + exc);
	    return;
	}
	Thread thread = new Thread(this);
	thread.start();
    }

    @Override
    public void run() {
	logger.log(Level.INFO, "Run server");
	try {
	    while(!command.equals(Command.CLOSE)) {
		try {
		    Socket clientSocket = serverSocket.accept();
		    //Here need to add prototype scope with proxy, but i don't know how, yet :(
		    ClientService clientService = new ClientService(clientSocket);
		    clientService.setServerLogger(logger);
		    executorService.submit(clientService);
		} catch (SocketTimeoutException exc) { }
	    }
	    close();
	} catch (IOException exc) {
	    logger.log(Level.WARNING, "Socket error while run server:\n" + exc);
	}
    }

    @Override
    public void occur(CommandEvent event) {
	command = (Command) event.getSource();
    }

    protected void close() throws IOException{
	//System.out.println("Close server...");
	logger.log(Level.INFO, "Close server...");
	try {
	    executorService.shutdown();
	    serverSocket.close();
	} catch (Exception exc) {
	    //System.err.println("Exception while closing server:\n");
	    logger.log(Level.WARNING, "Exception while closing server:\n");
	}
	//System.out.println("Server was successfuly closed");
	logger.log(Level.INFO, "Server was successfuly closed");
    }

	
	
    public static void main(String[] args) {
	Server server = new Server();
	server.start();
    }
		
}
	
