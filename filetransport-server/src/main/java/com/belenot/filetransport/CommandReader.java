package com.belenot.filetransport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import com.belenot.filetransport.util.logging.ServerLogger;

class CommandReader implements Runnable {
    private ServerLogger serverLogger;
    public void setServerLogger(ServerLogger serverLogger) { this.serverLogger = serverLogger; }
    private Set<CommandEventListener> listeners = new HashSet<>();
    public void setListeners(Set<CommandEventListener> listeners) { this.listeners = listeners; }
    private InputStream inputStream;
    public void setInputStream(InputStream inputStream) { this.inputStream = inputStream; }
	
    private boolean isClosed = false;
    private Command command;

    public CommandReader() {
	this.command = Command.NORMAL_RUN;
    }
    
    public CommandReader(Command command) {
	this.command = command;
    }

    public void addCommandEventListener(CommandEventListener listener) {
	listeners.add(listener);
    }

    public void init() {
	(new Thread(this)).start();
    }
	
    @Override
    public void run() {
	while(!command.equals(Command.CLOSE)) {
	    try {
		String input = (new BufferedReader(new InputStreamReader(inputStream))).readLine();
		Command command = Command.valueOf(input);
		switch (command) {
		case CLOSE:
		    this.command = command;
		    fire(new CommandEvent(command));
		    break;
		}
	    } catch (IllegalArgumentException | NullPointerException exc) {
		//System.err.println("Incorrect command!");
		serverLogger.log(Level.WARNING, "Incorrect command!");
	    } catch (IOException exc) {
		//System.err.println("Error durring read command!");
		serverLogger.log(Level.WARNING, "Error durring read command!");
	    } 
	}
    }

    private void fire(CommandEvent event) {
	for (CommandEventListener listener : listeners) {
	    listener.occur(event);
	}
    }
}
			
		
	
