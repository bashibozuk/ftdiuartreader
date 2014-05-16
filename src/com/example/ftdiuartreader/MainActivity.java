package com.example.ftdiuartreader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.eservice.ftdi.FtdiUtil;
import com.eservice.inkosystems.DataObject;
import com.eservice.inkosystems.IncoApiUtil;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	boolean mReadingEnabled;
	
	FileOutputStream outputStream;
	
	public void setReadingEnabled(boolean value) {
		mReadingEnabled = value;
	}
	
	public boolean getReadingEnabled() {
		return mReadingEnabled;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		mReadingEnabled = false;
	}
	
	public boolean write(byte[] bytes) {
		if (outputStream == null) {
			File f = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "data.log");
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				outputStream = new FileOutputStream(f, true);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
		try {
			outputStream.write(bytes);
			outputStream.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		Button mFindDeviceBtn;
		Button mStartReadingBtn;
		Button mStopReadingBtn;
		Button mClearBtn;
		
		TextView mLog;
		TextView mData;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			mFindDeviceBtn = (Button) rootView.findViewById(R.id.btn_device);
			mStartReadingBtn = (Button) rootView.findViewById(R.id.btn_start_reading);
			mStopReadingBtn = (Button) rootView.findViewById(R.id.btn_stop_reading);
			mClearBtn = (Button) rootView.findViewById(R.id.btn_clear);
			mLog = (TextView) rootView.findViewById(R.id.log);
			mData = (TextView) rootView.findViewById(R.id.data);
			
			final MainActivity activity = (MainActivity)this.getActivity();
			
			mFindDeviceBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!FtdiUtil.getInstance().hasContext()) {
						FtdiUtil.getInstance().setContext(activity);
					}
					int devCount = FtdiUtil.getInstance().getDevicesCount();
					mLog.append("devices found: " + devCount + "\n");
					if (devCount > 0) {
						mStartReadingBtn.setVisibility(View.VISIBLE);
						mStopReadingBtn.setVisibility(View.VISIBLE);
						mClearBtn.setVisibility(View.VISIBLE);
					}
				}
			});
			
			mStartReadingBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					activity.setReadingEnabled(true);
					mLog.append("Reading enabled\n");
				}
			});
			
			mStopReadingBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					activity.setReadingEnabled(false);
					mLog.append("Reading disabled\n");
				}
			});
			
			Thread readThread = new Thread(new Runnable() {

				
				@Override
				public void run() {
					while (true) {
						
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if (activity.getReadingEnabled()) {
							try {
								final byte[] data = new byte[FtdiUtil.USB_DATA_BUFFER];
								final int result = FtdiUtil.getInstance().getDevice().read(data, FtdiUtil.USB_DATA_BUFFER);
								mData.post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										try {
											mData.setText(new DataObject(IncoApiUtil.getMessage(data)).toString());
											
										} catch (Exception e) {
											mData.setText("Exception : " + e.getMessage());
										}
									}
								});
								
								final boolean result1 = activity.write(data);
								
							} catch (Exception e) {
								final String msg = e.getMessage();
								mLog.post(new Runnable() {
									
									@Override
									public void run() {
										
										mLog.append("Exception : " + msg);
										mLog.append("\n");
										
									}
								});
								activity.setReadingEnabled(false);
							}
						}
						
					}
				}
			});
			
			readThread.start();
			return rootView;
		}
	}
}
