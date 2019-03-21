package belenot.filetransport;

import java.net.*;
import java.io.*;

public class Client {
	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 5678)){
			socket.getOutputStream().write(args.length > 0 ? args[0].getBytes() : "null arguments".getBytes());
		} catch (IOException exc) {
			System.out.println("Can't connect to server:\n" + exc);
		}
	}

}
