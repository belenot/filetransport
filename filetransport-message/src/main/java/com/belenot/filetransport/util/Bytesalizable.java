package com.belenot.filetransport.util;

import java.io.IOException;

public interface Bytesalizable<T> {
	byte[] getBytes() throws IOException, IllegalStateException;
	T fillObject(byte[] bytes) throws IOException, IllegalArgumentException;
}
