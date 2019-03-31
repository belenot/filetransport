package belenot.filetransport.util;

public interface Bytesalizable<T> {
	byte[] getBytes();
	T fillObject(byte[] bytes);
}
