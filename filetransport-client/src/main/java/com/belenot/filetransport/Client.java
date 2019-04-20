package com.belenot.filetransport;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
	static private String hostname = "";
	static private int port = -1;
	public static ServerResponse write(String strCommand,String[] args) {
	
		return null;
	}

	private static void config() throws IllegalStateException {
		try {
			System.out.print("<hostname>:<port>#:>");
			String inputBuffer = new BufferedReader(new InputStreamReader(System.in)).readLine();
		    hostname = inputBuffer.split(":")[0];
		    port = Integer.parseInt(inputBuffer.split(":")[1]);
		} catch (Exception exc) {
			throw new IllegalStateException("Config error", exc);
		}
	}
		
	
	public static void main(String[] args) {
		System.out.println("Start filetransport client:");
		try {
			config();
		} catch(IllegalStateException exc) {
			System.err.println(exc.toString());
			return;
		}
		ServerResponse serverResponse = null;
		ClientQuery clientQuery = null;
		boolean stop = false;
		try (Socket socket = new Socket(hostname, port)){
			do {
				serverResponse = null;
				clientQuery = null;
				System.out.print("#:>");
				args = (new BufferedReader(new InputStreamReader(System.in))).readLine().split(" ");
				stop = args[0].equals("STOP") ? true : false;
				switch (args[0]) {
				case "LOAD": clientQuery = ClientHelper.newLoadQuery(args[1]); break;
				case "SAVE": clientQuery = ClientHelper.newSaveQuery(args[1], args[2]); break;
				case "LISTTREE": clientQuery = ClientHelper.newListTreeQuery(args[1]); break;
				case "MKDIR": clientQuery = ClientHelper.newMkDirQuery(args[1]); break;
				case "DELETE": clientQuery = ClientHelper.newDeleteQuery(args[1], args.length < 3 ? null : args[2]); break;
				case "STOP": clientQuery = ClientHelper.newStopQuery(); break;
				default: //System.err.println("UnknownCommand");
					System.err.println("UnknownCommand");
				}
				if (clientQuery != null) {
					try {
						socket.getOutputStream().write(clientQuery.getBytes());
						//(new ObjectOutputStream(socket.getOutputStream())).writeObject(clientQuery);
					    byte firstByte = (byte) socket.getInputStream().read();
						byte[] bytes = new byte[socket.getInputStream().available() + 1];
						bytes[0] = firstByte;
						socket.getInputStream().read(bytes, 1, bytes.length - 1);
						serverResponse = (new ServerResponse()).fillObject(bytes);
																		   
					    //serverResponse = (ServerResponse) (new ObjectInputStream(socket.getInputStream())).readObject();
					}
					catch (IOException exc) {
						System.err.println("Can't connect to server:\n" + exc);
					}
					System.out.println(serverResponse.getResponseCode());
					if (serverResponse.getData() != null)
						System.out.println(new String(serverResponse.getData()));
				}
			} while(!stop && serverResponse != null && serverResponse.getResponseCode() == ResponseCode.ALLOW);
			if (serverResponse == null || serverResponse.getResponseCode() == ResponseCode.ERROR)
				System.err.println("Abnormal process termination");
		}
		catch (IOException exc) {
				System.err.println("Error read input command");				
		}
	}

}
