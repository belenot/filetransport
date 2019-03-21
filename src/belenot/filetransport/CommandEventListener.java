package belenot.filetransport;

import java.util.*;

public interface CommandEventListener<CommandEvent> {
	public void inputCommand(CommandEvent event);
}
