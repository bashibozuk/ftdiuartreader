package com.eservice.util;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

public class ExceptionUtil {
	public static String asString(Exception e) {
	    CharArrayWriter cw = new CharArrayWriter();
	    PrintWriter w = new PrintWriter(cw);
	    e.printStackTrace(w);
	    w.close();
	    
	    return cw.toString();
	}
}
