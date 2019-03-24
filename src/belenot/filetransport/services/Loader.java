package belenot.filetransport.services;

import belenot.filetransport.*;
import java.util.stream.*;
import java.util.function.*;

public class Loader implements Function<ClientQuery, ServerResponse> {
	public ServerResponse apply(ClientQuery clientQuery) {
		System.out.println("Load function:");
		System.out.println("\tcommand: " + clientQuery.getClientCommand());
		clientQuery.getHeaders().keySet().
			stream().
			forEach((k) -> System.out.println("\t" + k + ":" + clientQuery.getHeaders().get(k)));
		System.out.println("Content:\n" + new String(clientQuery.getData()));
		return null;
	}
}
