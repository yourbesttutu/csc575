package com.example.testrssi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;

public class WifiExample extends Activity {

	private final String TAG = "WifiExample";

	private IntentFilter mWifiIntentFilter;
	private BroadcastReceiver mWifiIntentReceiver;

	private ImageButton mIconWifi;
	private TextView mLabelWifi;
	private Handler mHandler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_example);

		mLabelWifi = (TextView) findViewById(R.id.Label_WifiDetail);

		mWifiIntentFilter = new IntentFilter();
		mWifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

		mWifiIntentReceiver = new mWifiIntentReceiver();
		registerReceiver(mWifiIntentReceiver, mWifiIntentFilter);
		
		mHandler = new Handler();
		mHandler.post(new TimerProcess());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_wifi_example, menu);
		return true;
	}

	private class TimerProcess implements Runnable {
		public void run() {
			showWIFIDetail();
			mHandler.postDelayed(this, 500);
		}
	}

	public void showWIFIDetail() {
		WifiInfo info = ((WifiManager) getSystemService(WIFI_SERVICE))
				.getConnectionInfo();

		/*
		 * info.getBSSID()； 获取BSSID地址。 info.getSSID()； 获取SSID地址。 需要连接网络的ID
		 * info.getIpAddress()； 获取IP地址。4字节Int, XXX.XXX.XXX.XXX 每个XXX为一个字节
		 * info.getMacAddress()； 获取MAC地址。 info.getNetworkId()； 获取网络ID。
		 * info.getLinkSpeed()； 获取连接速度，可以让用户获知这一信息。 info.getRssi()；
		 * 获取RSSI，RSSI就是接受信号强度指示
		 */

		int Ip = info.getIpAddress();
		String strIp = "" + (Ip & 0xFF) + "." + ((Ip >> 8) & 0xFF) + "."
				+ ((Ip >> 16) & 0xFF) + "." + ((Ip >> 24) & 0xFF);
		mLabelWifi.setText("BSSID : " + info.getBSSID() + "\nSSID : "
				+ info.getSSID() + "\nIpAddress : " + strIp + "\nMacAddress : "
				+ info.getMacAddress() + "\nNetworkId : " + info.getNetworkId()
				+ "\nLinkSpeed : " + info.getLinkSpeed() + "Mbps" + "\nRssi : "
				+ info.getRssi());
		info.getIpAddress();
	}

	private class mWifiIntentReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			
			WifiInfo info = ((WifiManager) getSystemService(WIFI_SERVICE))
					.getConnectionInfo();
		

			/*
			 * WifiManager.WIFI_STATE_DISABLING 正在停止
			 * WifiManager.WIFI_STATE_DISABLED 已停止
			 * WifiManager.WIFI_STATE_ENABLING 正在打开
			 * WifiManager.WIFI_STATE_ENABLED 已开启 WifiManager.WIFI_STATE_UNKNOWN
			 * 未知
			 */

			switch (intent.getIntExtra("wifi_state", 0)) {
			case WifiManager.WIFI_STATE_DISABLING:
				Log.d(TAG, "WIFI STATUS : WIFI_STATE_DISABLING");
				break;
			case WifiManager.WIFI_STATE_DISABLED:
				Log.d(TAG, "WIFI STATUS : WIFI_STATE_DISABLED");
				break;
			case WifiManager.WIFI_STATE_ENABLING:
				Log.d(TAG, "WIFI STATUS : WIFI_STATE_ENABLING");
				break;
			case WifiManager.WIFI_STATE_ENABLED:
				Log.d(TAG, "WIFI STATUS : WIFI_STATE_ENABLED");
				break;
			case WifiManager.WIFI_STATE_UNKNOWN:
				Log.d(TAG, "WIFI STATUS : WIFI_STATE_UNKNOWN");
				break;
			}
		}
	}
}