package belenot.filetransport;

import java.util.concurrent.*;
import java.net.*;
import java.io.*;
import java.time.*;


public class Server
	implements Runnable {
	private static int SERVER_SOCKET_PORT = 5678;

	private int serverSocketPort = SERVER_SOCKET_PORT;
	
	private ExecutorService executorService;
	private ServerSocket serverSocket;
	private Exchanger<Command> exchangerCommand;
	
	private boolean isClose = false;
	private boolean isStart = false;
	
	public Server() {
		//Config
	}

	public Thread start() {
		if (isStart) return null;
		try {
			init();
		} catch (IOException exc) {
			System.err.println("Error initialize:\n" + exc);
		}
		Thread thread = new Thread(this);
		thread.start();
		(new Thread(new CommandReader(exchangerCommand))).start();
		return thread;
	}

	private void init() throws IOException {
		executorService = Executors.newCachedThreadPool();
		serverSocket = new ServerSocket(serverSocketPort);
		exchangerCommand = new Exchanger<>();
		if (executorService == null || serverSocket == null)
			throw new IllegalStateException();
	}

	@Override
	public void run() {
		
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
		
}
	
