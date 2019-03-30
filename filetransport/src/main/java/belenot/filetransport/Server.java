package belenot.filetransport;

import java.util.concurrent.*;
import java.net.*;
import java.io.*;
import java.time.*;


public class Server
	implements Runnable, CommandEventListener {
	private static int SERVER_SOCKET_PORT = 5678;

	private int serverSocketPort = SERVER_SOCKET_PORT;
	private int SO_TIMEOUT = 1000;
	
	private ExecutorService executorService;
	private ServerSocket serverSocket;
	
	private boolean isStart = false;
	private Command command;
	
	public Server() {
		//Config
	}

	public Thread start() {
		System.out.println("Start server");
		if (isStart) return null;
		try {
			init();
		} catch (IOException | IllegalStateException exc) {
			System.err.println("Error initialize:\n" + exc);
			return null;
		}
		Thread thread = new Thread(this);
		thread.start();
		CommandReader commandReader = new CommandReader(command);
		commandReader.addCommandEventListener(this);
		(new Thread(commandReader)).start();
		return thread;
	}

	@Override
	public void run() {
		System.out.println("Run server");
		try {
			while(!command.equals(Command.CLOSE)) {
				try {
					Socket clientSocket = serverSocket.accept();
					executorService.submit(new ClientService(clientSocket));
				} catch (SocketTimeoutException exc) { }
			}
			close();
		} catch (IOException exc) {
			System.out.println("Socket error while run server:\n" + exc);
		}
	}

	@Override
	public void occur(CommandEvent event) {
		command = (Command) event.getSource();
	}


	protected void init() throws IOException {
		executorService = Executors.newCachedThreadPool();
		serverSocket = new ServerSocket(serverSocketPort);
		serverSocket.setSoTimeout(SO_TIMEOUT);
		command = Command.NORMAL_RUN;
		if (executorService == null || serverSocket == null)
			throw new IllegalStateException();
	}

	protected void close() throws IOException {
		System.out.println("Close server...");
		try {
			executorService.shutdown();
			serverSocket.close();
		} catch (Exception exc) {
			System.out.println("Exception while closing server:\n");
		}
		System.out.println("Server was successfuly closed");
	}

	
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
		
}
	