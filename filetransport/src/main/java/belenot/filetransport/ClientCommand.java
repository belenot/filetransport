package belenot.filetransport;

public enum ClientCommand {
	SAVE, MKDIR, LOAD, DELETTER, LISTTREE, STOP;
	
	@Deprecated
	public static int maxLength() {
		int maxLength = 0;
		for(ClientCommand value : ClientCommand.values()) {
			maxLength = value.toString().length() > maxLength ?
				value.toString().length() :
				maxLength;
		}
		return maxLength;
	}
}
