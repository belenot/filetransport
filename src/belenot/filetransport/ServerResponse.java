package belenot.filetransport;

import java.util.*;


public class ServerResponse {
	public ServerResponse(ResponseCode code) {
		responseCode = code;
	}
	public Map<String, String> getHeaders() { return headers; };
	public byte[] getData() { return data; };

	private ResponseCode responseCode;
	private Map<String, String> headers;
	private byte[] data;
}
