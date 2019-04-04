package belenot.filetransport;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import belenot.filetransport.util.logging.*;

class CommandReader implements Runnable {
	private ServerLogger logger = new ServerLogger();
	
	public CommandReader(Command command) {
		this.command = command;
	}

	public void addCommandEventListener(CommandEventListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void run() {
		while(!command.equals(Command.CLOSE)) {
			try {
				String input = (new BufferedReader(new InputStreamReader(System.in))).readLine();
				Command command = Command.valueOf(input);
				switch (command) {
				case CLOSE:
					this.command = command;
					fire(new CommandEvent(command));
					break;
				}
			} catch (IllegalArgumentException | NullPointerException exc) {
				//System.err.println("Incorrect command!");
				logger.log(Level.WARNING, "Incorrect command!");
			} catch (IOException exc) {
				//System.err.println("Error durring read command!");
				logger.log(Level.WARNING, "Error durring read command!");
			} 
		}
	}


	private Set<CommandEventListener> listeners = new HashSet<>();
	
	private boolean isClosed = false;
	private Command command;

	private void fire(CommandEvent event) {
		for (CommandEventListener listener : listeners) {
			listener.occur(event);
		}
	}
}
			
		
	
