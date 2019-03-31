package belenot.filetransport;

import java.util.*;
import java.io.*;

public class ServerResponse implements Externalizable {
	public ServerResponse() { }
	public ServerResponse(ResponseCode code) {
		responseCode = code;
		headers = new HashMap<>();
	}
	public ResponseCode getResponseCode() { return responseCode; }
	public Map<String, String> getHeaders() { return headers; };
	public byte[] getData() { return data; };
	public void setData(byte[] bytes) { data = bytes; }

	@Override
	public void readExternal(ObjectInput in) throws IOException{
		String str = "";
		int b;
		int headerCount = 0;
		while( (b = in.read()) != -1 && b != '\n') str += (char) b;
		try {
			responseCode = ResponseCode.valueOf(str);
		} catch (IllegalArgumentException exc) { throw new IOException(exc); }
		str = "";
		while( (b = in.read()) != -1 && b != '\n') str += (char) b;
		try {
			headerCount = Integer.parseInt(str);
		} catch (NumberFormatException exc) { throw new IOException(exc); }
		str = "";
		for(int i = 0; i < headerCount; i++) {
			while( (b = in.read()) != -1 && b != '\n') str += (char) b;
			String key = str.split(":")[0];
			String value = str.split(":")[1];
			headers.put(key, value);
		}
		data = new byte[in.available()];
		in.read(data);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bytes.write((responseCode.toString() + "\n" + headers.size() + "\n").getBytes());
		//String str = responseCode.toString() + "\n" + headers.size() + "\n";
		for(Map.Entry<String, String> entry : headers.entrySet())
		    bytes.write((entry.getKey() + ":" + entry.getValue() + "\n").getBytes());
		if (data != null && data.length > 0)
			bytes.write(data, 0, data.length);
		out.write(bytes.toByteArray());
	}
		

	private ResponseCode responseCode;
	private Map<String, String> headers = new HashMap<>();
	private byte[] data;
}
