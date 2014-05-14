package com.eservice.ftdi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.D2xxManager.D2xxException;
import com.ftdi.j2xx.FT_Device;

public class FtdiUtil {

	final byte XON = 0x11;    /* Resume transmission */
    final byte XOFF = 0x13;    /* Pause transmission */
    
    public final static int READ_BUFFER_SIZE = 10240;
    public final static int USB_DATA_BUFFER = 8192;
	D2xxManager ftdiManager;
	FT_Device ftdiDevice;
	
	Context ctx;
	
	int devicesCount;
	
	static FtdiUtil _instance;
	
	byte[] writeBuffer;
	byte[] readBuffer;
	
	private FtdiUtil() {
		devicesCount = -1;
	}
	
	public static FtdiUtil getInstance() {
		if (_instance == null) {
			_instance = new FtdiUtil();
		}
		
		return _instance;
	}
	
	public void setContext(Context context) {
		ctx = context;
	}
	
	public boolean hasContext() {
		return ctx != null;
	}
	
	public D2xxManager getFtdiManager() {
		if (ftdiManager == null) {
			try {
				ftdiManager = D2xxManager.getInstance(ctx);
				devicesCount = ftdiManager.createDeviceInfoList(ctx);
			} catch (D2xxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ftdiManager;
	}
	
	public int getDevicesCount() {
		if (ftdiManager == null) {
			getFtdiManager();
		}
		
		return devicesCount;
	}
	
	public boolean openDevice(int index) {
		if (getDevicesCount() - 1 >= index) {
			ftdiDevice = getFtdiManager().openByIndex(ctx, index);
			return ftdiDevice != null;
		}
		return false;
	}
	
	public boolean openDevice() {
		ftdiDevice = getFtdiManager().openByIndex(ctx, 0);
		return ftdiDevice != null;
	}
	
	public FT_Device getDevice() {
		if (ftdiDevice == null) {
			openDevice();
			configDevice();
		}
		
		return ftdiDevice;
	}
	
	private void configDevice() {
		if (ftdiDevice == null) {
			Log.w("FTDIUTIL", "Cant configure ftdi device, it is null");
			return;
		}
		
		ftdiDevice.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);
		ftdiDevice.setBaudRate(115200);
		ftdiDevice.setDataCharacteristics(D2xxManager.FT_DATA_BITS_8, D2xxManager.FT_STOP_BITS_1, D2xxManager.FT_PARITY_NONE);
		ftdiDevice.setFlowControl(D2xxManager.FT_FLOW_NONE, XON, XOFF);
	}
}
