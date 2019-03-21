package belenot.filetransport;

import java.util.*;

public class CommandEvent extends EventObject {
	private Set<CommandEventListener> listeners = new HashSet<>();

	public CommandEvent(Command command) {
		super(command);
	}
	public void addListener(CommandEventListener listener) {
		listeners.add(listener);
	}
	public Command fire() {
		for(CommandEventListener listener : listeners) {
			listener.inputCommand(this);
		}
		return getCommand();
	}
	public void setCommand(Command command) {
		source = command;
	}
	public Command getCommand() {
		if(source instanceof Command) {
			return (Command) source;
		} else {
			throw new IllegalStateException();
		}
	}
		
}
