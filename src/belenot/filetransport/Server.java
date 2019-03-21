package belenot.filetransport;

import java.util.concurrent.*;
import java.net.*;
import java.io.*;
import java.time.*;


public class Server
	implements Runnable, CommandEventListener<CommandEvent> {
	private static int SERVER_SOCKET_PORT = 5678;

	private int serverSocketPort = SERVER_SOCKET_PORT;
	
	private ExecutorService executorService;
	private ServerSocket serverSocket;
	private boolean isClosed = false;
	private CommandEvent commandEvent =  new CommandEvent(Command.CLOSE);


	private void init() {
		serverSocketPort = SERVER_SOCKET_PORT;
	}		
	
	private Server() {
		executorService = Executors.newCachedThreadPool();
		try {
			serverSocket = new ServerSocket(serverSocketPort);
			(new Thread(this)).start();
		} catch (Exception exc) {
			System.err.println("Error was occured, when start server:\n" + exc);
		}
	}

    public void addCommandEventListener(CommandEventListener listener) {
		commandEvent.addListener(listener);
	}
		
	@Override
    public void run() {
		System.out.println("Run server at " + LocalTime.now());
		while (!isClosed) {
			try {
				Socket socket = serverSocket.accept();
				executorService.submit(new ClientService(socket));
			} catch (IOException exc) {
				System.err.println("Error was occured, when run socket:\n" + exc);
			} finally {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException exc) {
					System.err.println(exc);
				}
			}
				
		}
	};

	@Override
	public void inputCommand(CommandEvent commandEvent) {
		switch (commandEvent.getCommand()) {
		case CLOSE: close(); System.exit(0);
		}
	}

	public void close() {
		isClosed = true;
		executorService.shutdown();
		try {
			if (!serverSocket.isClosed())
				serverSocket.close();
		} catch (IOException exc) {
			System.err.println("Error when closing server socket:\n" + exc);
		}
	}
	
	
	public static void main(String[] args) {
		Server server = new Server();
		server.addCommandEventListener(server);
		try {
			while (!server.isClosed) {
				String commandStr = (new BufferedReader(new InputStreamReader(System.in)))
				.readLine();
				try {
					Command command = Command.valueOf(commandStr.toUpperCase());
				    server.commandEvent.setCommand(command);
					server.commandEvent.fire();
				} catch (IllegalArgumentException exc) {
					System.err.println("Error: Incorrect command!");
				}
			}
		} catch (IOException exc) {
			System.err.println("Error during reading input command\n" + exc);
		} finally {
			if (!server.isClosed)
				server.close();
		}
			
		System.out.println("Closed");
	}
}
	
