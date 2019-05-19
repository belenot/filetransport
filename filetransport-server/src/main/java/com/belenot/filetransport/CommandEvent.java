package com.belenot.filetransport;

import org.springframework.context.ApplicationEvent;

public class CommandEvent extends ApplicationEvent {
	public CommandEvent(Command command) {
		super(command);
	}
}
