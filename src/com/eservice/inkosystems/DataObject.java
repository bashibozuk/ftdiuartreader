package com.eservice.inkosystems;

import java.util.Arrays;
import android.annotation.SuppressLint;
import android.text.format.DateFormat;

public class DataObject {
	int[] quarternions;
	
	int[] encoderData;
	
	int timestamp;
	
	int[] accelometer;
	
	public DataObject() {
		quarternions = new int[4];
		encoderData = new int[2];
		timestamp = 0;
		accelometer = new int[3];
	}
	
	public DataObject(byte[] data) {
		quarternions = new int[4];
		encoderData = new int[2];
		timestamp = 0;
		accelometer = new int[3];
		fromBytes(data);
	}
	
	public void fromBytes(byte[] data) {
		readQuarternions(data);
		readEncoderData(data);
		readTimestamp(data);
		readAccelometer(data);
	}
	
	public void readQuarternions(byte[] data) {
		byte[] qdata = Arrays.copyOfRange(data, 2, 9);
		quarternions[0] = IncoApiUtil.byteArrayToInt(new byte[]{qdata[1], qdata[0]});
		quarternions[1] = IncoApiUtil.byteArrayToInt(new byte[]{qdata[3], qdata[2]});
		quarternions[2] = IncoApiUtil.byteArrayToInt(new byte[]{qdata[5], qdata[4]});
		quarternions[3] = IncoApiUtil.byteArrayToInt(new byte[]{qdata[7], qdata[6]});
	}
	
	public void readEncoderData(byte[] data) {
		byte[] qdata = Arrays.copyOfRange(data, 10, 13);
		encoderData[0] = IncoApiUtil.byteArrayToInt(new byte[]{qdata[1], qdata[0]});
		encoderData[1] = IncoApiUtil.byteArrayToInt(new byte[]{qdata[3], qdata[2]});
	}
	
	public void readTimestamp(byte[] data) {
		byte[] qdata = Arrays.copyOfRange(data, 26, 29);
		timestamp = IncoApiUtil.byteArrayToInt(new byte[] {qdata[3], qdata[2], qdata[1], qdata[0]});
	}
	
	public void readAccelometer(byte[] data) {
		byte[] qdata = Arrays.copyOfRange(data, 14, 25);
		accelometer[0] = IncoApiUtil.byteArrayToInt(new byte[]{qdata[3 ], qdata[2 ], qdata[1], qdata[0]});
		accelometer[1] = IncoApiUtil.byteArrayToInt(new byte[]{qdata[7 ], qdata[6 ], qdata[5], qdata[4]});
		accelometer[2] = IncoApiUtil.byteArrayToInt(new byte[]{qdata[11], qdata[10], qdata[9], qdata[8]});
	}
	
	@SuppressLint("DefaultLocale")
	public String toString()
	{
		return String.format("Data: \r\nQuarternions: %d , %d, %d, %d \r\nEncoderData: %d, %d \r\nTimestamp %d(%s), \r\nAccelometer: %d, %d, %d",
			quarternions[0], quarternions[1], quarternions[2], quarternions[3],	
			encoderData[0], encoderData[1],	
			timestamp, DateFormat.format("Y-M-d H:m:s", timestamp * 1000),
			accelometer[0], accelometer[1], accelometer[2]
		);
	}
}
