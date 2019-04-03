package belenot.filetransport.util.logging;

import java.util.logging.*;

public class ServerLogger extends Logger {
	private static String NAME = "ServerLogger";
	private static String RESOURCE_BUNDLE_NAME = null;
	public ServerLogger() {
		super(NAME, RESOURCE_BUNDLE_NAME);
		//read conf file
		addHandler(new ServerLoggerHandler());
	}
}
