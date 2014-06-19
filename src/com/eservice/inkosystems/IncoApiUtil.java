package com.eservice.inkosystems;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class IncoApiUtil {
	
	public static final int MESSAGE_LENGTH_IN_BYTES = 32;
	
	public static final char CHAR_CHECK_CR = '\r';
	public static final byte BYTE_CHECK_CR = 0x0D;
	
	public static final char CHAR_CHECK_NL = '\n';
	public static final byte BYTE_CHECK_NL = 0x0A;
	
	public static int[] getMessage(byte[] read) {
		int end = -1;
		for (int i = read.length - 1; i >= 0; i--) {
			if (read[i] != BYTE_CHECK_NL) {
				continue;
			}
			if (end == -1) {
				end = i;
				continue;
			}
			
			int start = i + 1;
			int length = end - start;
			if (length != MESSAGE_LENGTH_IN_BYTES * 2) {
				return null;
			}
			int[] message = new int[MESSAGE_LENGTH_IN_BYTES];
			byte[] buffer = new byte[length];
			System.arraycopy(read, i + 1, buffer, 0, length);
			message = convertHeximalToDecimal(buffer);
			return message;
		}
		
		return null;
	}
	
	static int[] convertHeximalToDecimal(byte[] hex) {
		String hexStr = new String(hex, Charset.defaultCharset());
		
		return hexToAsciiArray(hexStr);
	}
	
	public static int byteArrayToInt(byte[] b) {
		final ByteBuffer bb = ByteBuffer.wrap(b);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		
		return bb.getInt();
	}
	
	static String hexToAscii(String s) throws IllegalArgumentException
	{
		  int n = s.length();
		  StringBuilder sb = new StringBuilder(n / 2);
		  for (int i = 0; i < n; i += 2)
		  {
		    char a = s.charAt(i);
		    char b = s.charAt(i + 1);
		    sb.append((char) ((hexToInt(a) << 4) | hexToInt(b)));
		  }
		  return sb.toString();
	}
	
	public static int[] hexToAsciiArray(String s) throws IllegalArgumentException {

		  int n = s.length();
		  int[] ascii = new int[n / 2];
		  int pointer = 0;
		  for (int i = 0; i < n; i += 2) {
		    char a = s.charAt(i);
		    char b = s.charAt(i + 1);
		    int next = ((hexToInt(a) << 4) | hexToInt(b));
		    System.arraycopy(new int[] {next}, 0, ascii, pointer++, 1);
		  }
		  return ascii;
	}
	
	public static int hexToInt(char ch)
	{
		  if ('a' <= ch && ch <= 'f') { 
			  return ch - 'a' + 10; 
		  }
		  
		  if ('A' <= ch && ch <= 'F') { 
			  return ch - 'A' + 10; 
		  }
		  
		  if ('0' <= ch && ch <= '9') { 
			  return ch - '0'; 
		  }
		  
		  throw new IllegalArgumentException(String.valueOf(ch));
	}
	
	public static long intArrayToLong(int[] data) {
		long result = 0;
		for (int i = data.length - 1; i >= 0; i--) {
			result = result << 8;
			result |= data[i];
		}
		
		
		return result;
	}
}
