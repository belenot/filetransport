package com.belenot.filetransport.services;

import java.util.function.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.io.IOException;
import com.belenot.filetransport.*;

public class Deletter implements Function<ClientQuery, ServerResponse> {
	@Override
	public ServerResponse apply(ClientQuery clientQuery) {
		ServerResponse serverResponse = null;
		try {
			String deleteFilename = clientQuery.getHeaders().get("filename");
			Boolean recursive = Boolean.valueOf(clientQuery.getHeaders().get("recursive"));
			Path deletePath = Paths.get(deleteFilename);
			if (!recursive) {
				Files.delete(deletePath);
			}
			else {
				Files.walkFileTree(deletePath, new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
							Files.delete(file);
							return FileVisitResult.CONTINUE;
						}
						@Override
						public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
							if (exc == null) {
								Files.delete(dir);
								return FileVisitResult.CONTINUE;
							} else {
								throw exc;
							}
						}
					});
			}
			serverResponse = new ServerResponse(ResponseCode.ALLOW);
			serverResponse.getHeaders().put("filename", deleteFilename);
		} catch (Exception exc) {
			serverResponse = new ServerResponse(ResponseCode.DENY);
			serverResponse.getHeaders().put("exception", exc.toString());
		}
		return serverResponse;
	}

}
	
