package com.belenot.filetransport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationEventPublisher;

import com.belenot.filetransport.util.logging.ServerLogger;

class CommandReader implements Runnable, ApplicationEventPublisherAware {
    private ServerLogger serverLogger;
    public void setServerLogger(ServerLogger serverLogger) { this.serverLogger = serverLogger; }
    private InputStream inputStream;
    public void setInputStream(InputStream inputStream) { this.inputStream = inputStream; }
    private ApplicationEventPublisher publisher;
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) { this.publisher = publisher; }
	
    private boolean isClosed = false;
    private Command command;

    public CommandReader() {
	this.command = Command.NORMAL_RUN;
    }
    
    public CommandReader(Command command) {
	this.command = command;
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
	publisher.publishEvent(event);
    }
}
			
		
	
