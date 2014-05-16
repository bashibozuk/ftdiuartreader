package com.eservice.inkosystems;

import android.annotation.SuppressLint;
import java.util.Arrays;

public class DataObject {
	
	short[] quarternions;
	
	short[] encoderData;
	
	long timestamp;
	
	int[] accelometer;
	
	public DataObject() {
		init();
	}
	
	public DataObject(int[] data) {
		init();
		fromInts(data);
	}
	
	void init() {
		quarternions = new short[4];
		encoderData = new short[2];
		timestamp = 0;
		accelometer = new int[3];
	}
	
	public void fromInts(int[] data) {
		readQuarternions(data);
		readEncoderData(data);
		readTimestamp(data);
		readAccelometer(data);
	}
	
	public void readQuarternions(int[] data) {
		int[] qdata = Arrays.copyOfRange(data, 2, 10);
		quarternions[0] = (short)IncoApiUtil.intArrayToLong(new int[]{qdata[1], qdata[0]});
		quarternions[1] = (short)IncoApiUtil.intArrayToLong(new int[]{qdata[3], qdata[2]});
		quarternions[2] = (short)IncoApiUtil.intArrayToLong(new int[]{qdata[5], qdata[4]});
		quarternions[3] = (short)IncoApiUtil.intArrayToLong(new int[]{qdata[7], qdata[6]});
	}
	
	public void readEncoderData(int[] data) {
		int[] qdata = Arrays.copyOfRange(data, 10, 14);
		encoderData[0] = (short)IncoApiUtil.intArrayToLong(new int[]{qdata[1], qdata[0]});
		encoderData[1] = (short)IncoApiUtil.intArrayToLong(new int[]{qdata[3], qdata[2]});
	}
	
	public void readTimestamp(int[] data) {
		int[] qdata = Arrays.copyOfRange(data, 26, 30);
		timestamp = IncoApiUtil.intArrayToLong(new int[] {qdata[3], qdata[2], qdata[1], qdata[0]});
	}
	
	public void readAccelometer(int[] data) {
		int[] qdata = Arrays.copyOfRange(data, 14, 26);
		accelometer[0] = (int)IncoApiUtil.intArrayToLong(new int[]{qdata[3 ], qdata[2 ], qdata[1], qdata[0]});
		accelometer[1] = (int)IncoApiUtil.intArrayToLong(new int[]{qdata[7 ], qdata[6 ], qdata[5], qdata[4]});
		accelometer[2] = (int)IncoApiUtil.intArrayToLong(new int[]{qdata[11], qdata[10], qdata[9], qdata[8]});
	}
	
	@SuppressLint("DefaultLocale")
	public String toString()
	{
		return String.format("Data: \r\nQuarternions: %d , %d, %d, %d \r\nEncoderData: %d, %d \r\nTimestamp %d, \r\nAccelometer: %d, %d, %d",
			quarternions[0], quarternions[1], quarternions[2], quarternions[3],	
			encoderData[0], encoderData[1],	
			timestamp, 
			accelometer[0], accelometer[1], accelometer[2]
		);
	}
}
