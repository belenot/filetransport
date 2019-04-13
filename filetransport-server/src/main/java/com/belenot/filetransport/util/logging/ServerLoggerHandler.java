package com.belenot.filetransport.util.logging;

import java.util.logging.*;
import java.io.*;

public class ServerLoggerHandler extends Handler {
	private OutputStream out = System.err;

    public ServerLoggerHandler() {
		super();
	}

	@Override
	public void close() throws SecurityException {
		if(out != System.err) {
			try {
				out.close();
			}
			catch (IOException exc) {
				/*What I shall do?*/
			}
		}
	}

	@Override
	public void flush() throws SecurityException {
		try {
		if(out instanceof Flushable) out.flush();
		}
		catch (IOException exc) {
			/*What I shall do?*/
		}
	}

	@Override
	public void publish(LogRecord record) {
		try {
			out.write( ("[" + record.getLevel() + "]:" + record.getMessage() + "\n").getBytes());
		}
		catch (IOException exc) {
			/*What I shall do?*/
		}
	}
}
