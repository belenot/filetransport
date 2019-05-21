package com.belenot.filetransport;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * Stub for standard streams for xml. Don't know how to specify property from static System class in xml configuration
 */
public class StandardStreamFactory {
    public static InputStream stdin() { return System.in; }
    public static OutputStream stdout() { return System.out; }
    public static OutputStream stderr() { return System.err; }
}
