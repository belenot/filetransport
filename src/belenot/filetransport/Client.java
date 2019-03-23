package belenot.filetransport;

import java.net.*;
import java.io.*;
import java.util.regex.*;

public class Client {
	public static void write(String str) {
		Pattern pattern = Pattern.compile(" ");
		Matcher matcher = pattern.matcher(str);
		str = matcher.replaceAll("\0");
		try (Socket socket = new Socket("localhost", 5678)){
			socket.getOutputStream().write(str.getBytes());
		} catch (IOException exc) {
			System.out.println("Can't connect to server:\n" + exc);
		}
		System.out.println("write:\n" + str);
	}
		
	
	public static void main(String[] args) {
		write(args[0]);
	}

}
