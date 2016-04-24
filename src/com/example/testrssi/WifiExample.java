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
		 * info.getBSSID()�� ��ȡBSSID��ַ�� info.getSSID()�� ��ȡSSID��ַ�� ��Ҫ���������ID
		 * info.getIpAddress()�� ��ȡIP��ַ��4�ֽ�Int, XXX.XXX.XXX.XXX ÿ��XXXΪһ���ֽ�
		 * info.getMacAddress()�� ��ȡMAC��ַ�� info.getNetworkId()�� ��ȡ����ID��
		 * info.getLinkSpeed()�� ��ȡ�����ٶȣ��������û���֪��һ��Ϣ�� info.getRssi()��
		 * ��ȡRSSI��RSSI���ǽ����ź�ǿ��ָʾ
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
			 * WifiManager.WIFI_STATE_DISABLING ����ֹͣ
			 * WifiManager.WIFI_STATE_DISABLED ��ֹͣ
			 * WifiManager.WIFI_STATE_ENABLING ���ڴ�
			 * WifiManager.WIFI_STATE_ENABLED �ѿ��� WifiManager.WIFI_STATE_UNKNOWN
			 * δ֪
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