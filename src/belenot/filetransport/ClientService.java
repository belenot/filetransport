package belenot.filetransport;

import java.util.*;
import java.net.*;

public class ClientService implements Runnable {
	private Socket socket;
	
	ClientService (Socket s) {
		socket = s;
	}

	@Override
	public void run () {
		System.out.println("Run: " + socket.toString());
	}
}

	
