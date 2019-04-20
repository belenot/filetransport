package com.belenot.filetransport;

import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHelper {
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

	public static ClientQuery newMkDirQuery(String dirname) {
		ClientQuery clientQuery = new ClientQuery(ClientCommand.MKDIR);
		clientQuery.getHeaders().put("dirname", dirname);
		return clientQuery;
	}

	public static ClientQuery newDeleteQuery(String deleteFilename, String recursive) {
		recursive = recursive == null ? "false"  : recursive;
		ClientQuery clientQuery = new ClientQuery(ClientCommand.DELETTER);
		clientQuery.getHeaders().put("filename", deleteFilename);
		clientQuery.getHeaders().put("recursive", recursive.toString());
		return clientQuery;
	}
}
