package belenot.filetransport;

import java.net.*;
import java.io.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
		
	public static ClientQuery newSaveQuery (String filename, String newFilename)
		throws IOException, FileNotFoundException{
		FileInputStream in = new FileInputStream(filename);
		byte[] data = new byte[in.available()];
		in.read(data);
		ClientQuery clientQuery = new ClientQuery(ClientCommand.SAVE.toString());
		clientQuery.getHeaders().put("filename", newFilename);
		clientQuery.setData(data);
		return clientQuery;
	}

	public static ClientQuery newLoadQuery(String filename) {
		ClientQuery clientQuery = new ClientQuery(ClientCommand.LOAD);
		clientQuery.getHeaders().put("filename", filename);
		return clientQuery;
	}

	public static ClientQuery newListTreeQuery(String filename) {
		ClientQuery clientQuery = new ClientQuery(ClientCommand.LISTTREE);
		clientQuery.getHeaders().put("filename", filename);
		return clientQuery;
	}

	public static ClientQuery newStopQuery() {
		return new ClientQuery(ClientCommand.STOP);
	}
	
	public static void main(String[] args) {
		ServerResponse serverResponse = null;
		ClientQuery clientQuery = null;
		boolean stop = false;
		try (Socket socket = new Socket("localhost", 5678)){
			do {
				serverResponse = null;
				clientQuery = null;
				System.out.print("#:>");
				args = (new BufferedReader(new InputStreamReader(System.in))).readLine().split(" ");
				stop = args[0].equals("STOP") ? true : false;
				switch (args[0]) {
				case "LOAD": clientQuery = newLoadQuery(args[1]); break;
				case "SAVE": clientQuery = newSaveQuery(args[1], args[2]); break;
				case "LISTTREE": clientQuery = newListTreeQuery(args[1]); break;
				case "STOP": clientQuery = newStopQuery(); break;
				default: System.err.println("UnknownCommand");
				}
				if (clientQuery != null) {
					try {
						//(new ObjectOutputStream(socket.getOutputStream())).writeObject(clientQuery);
					    //serverResponse = (ServerResponse) (new ObjectInputStream(socket.getInputStream())).readObject();
						ObjectMapper objectMapper = new ObjectMapper();
						ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
						objectMapper.writeValue(byteStream, clientQuery);
						socket.getOutputStream().write(byteStream.toByteArray());
						serverResponse = (ServerResponse) objectMapper.readValue(socket.getInputStream(), ServerResponse.class);
					}
					catch (IOException exc) {
						System.err.println("Can't connect to server:\n" + exc);
						exc.printStackTrace();
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
