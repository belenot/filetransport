package belenot.filetransport;

import java.util.*;

public class CommandEvent extends EventObject {
	public CommandEvent(Command command) {
		super(command);
	}
}
