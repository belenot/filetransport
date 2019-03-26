package belenot.filetransport;

import java.util.*;
import java.io.*;

//Add ClientQuery
public class ServerResponse implements Serializable {
	public ServerResponse(ResponseCode code) {
		responseCode = code;
		headers = new HashMap<>();
	}
	public ResponseCode getResponseCode() { return responseCode; }
	public Map<String, String> getHeaders() { return headers; };
	public byte[] getData() { return data; };
	public void setData(byte[] bytes) { data = bytes; }

	private ResponseCode responseCode;
	private Map<String, String> headers;
	private byte[] data;
}
