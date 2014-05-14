package com.eservice.inkosystems;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

public class IncoApiUtil {
	
	public static final int MESSAGE_LENGTH_IN_BYTES = 32;
	
	public static byte[] getMessage(byte[] read) throws Exception {
		int start = -1; 
		byte check = (byte)'\r';
		
		byte[] message = new byte[32];
		
		for(int i = 0 ; i < read.length; i++) {
			if (read[i] == check) {
				start = i;
				break;
			}
		}
		
		if (start >= 0) {
			System.arraycopy(read, start, message, 0, MESSAGE_LENGTH_IN_BYTES);
		} else {
			throw new Exception("Could not find termination sign in the message! \n" + new String(read, "US-ASCII"));
		}
		
		return message;
	}
	
	public static int byteArrayToInt(byte[] b) {
		final ByteBuffer bb = ByteBuffer.wrap(b);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		
		return bb.getInt();
	}
	
	public static int[] getTerrminationSignPositions(byte[] input) throws Exception {
		int[] result = new int[0];
		byte check = (char) '\n';
		ByteBuffer bb = ByteBuffer.wrap(input);
		CharBuffer cb = bb.asCharBuffer();
		//String s = cb.
		for (int i = 0; i < cb.capacity(); i++) {
			if (cb.charAt(i) == '\n' ) {
				//throw new Exception("Match found");
				System.arraycopy(new int[]{i}, 0, result,result.length, 1);
			}
		}
		
		return result;
	}
	
	public static String getTerminationPositionsAsString(byte[] input) {
		
		StringBuilder sb = new StringBuilder();
		try {
			
			int[] positions = getTerrminationSignPositions(input);
			if (positions.length < 1) {
				return "";
			}
			for (int i = 0; i < positions.length; i++) {
				sb.append(positions[i] + ", ");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			sb.append(e.getMessage());
		}
		return sb.toString();
	}
}
