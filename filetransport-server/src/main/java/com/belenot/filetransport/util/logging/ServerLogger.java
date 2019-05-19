package com.belenot.filetransport.util.logging;

import java.util.logging.Logger;

public class ServerLogger extends Logger {
    public ServerLogger(String name, String resourceBundleName, ServerLoggerHandler handler) {
	super(name, resourceBundleName);
	addHandler(handler);
    }
}
