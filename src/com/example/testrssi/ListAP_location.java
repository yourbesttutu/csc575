package com.example.testrssi;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;




import com.example.testrssi.WifiListActivity.MyAdapter;

public class ListAP_location extends Activity {
	private WifiManager wifiManager;
	List<ScanResult> list;
	private static HashMap<Integer, Boolean> isSelected;
	Button save;
	private ProgressDialog dialog;
	private String[] MAC;
	TextView text;
	private int rssi_tmp[][] = new int[3][10];
	float []rssi=new float[3];
	float []r_min=new float[3];
	float []r_max=new float[3];
	private int[] X=new int[3];
	private int[] Y=new int[3];
	  private RssiAPTask mTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_checkbox);
		init();
	}

	private void init() {
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		 text = (TextView) findViewById(R.id.test);
		 isSelected=new HashMap<Integer, Boolean>();
		dialog = new ProgressDialog(this);
		dialog.setTitle("Progressing");
		dialog.setMessage("Getting Rssi Value.......");
		openWifi();
		list = wifiManager.getScanResults();
		for (int i = 0; i < list.size(); i++) {
			isSelected.put(i, false);
		}

		ListView listView = (ListView) findViewById(R.id.listView);
		save = (Button) findViewById(R.id.saveMAC);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MAC = new String[3];
				int j = 0;
				for (int i = 0; i < isSelected.size(); i++) {
					if (isSelected.get(i) == true) {
						MAC[j] = list.get(i).BSSID;
						j++;
					}
				}
				mTask = new RssiAPTask();
				mTask.execute();
            
			}
		});
		if (list == null) {
			Toast.makeText(this, "wifi未打开！", Toast.LENGTH_LONG).show();
		} else {
			listView.setAdapter(new MyAdapter(this, list));
		}

	}

	/**
	 * 打开WIFI
	 */
	private void openWifi() {
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}

	}

	public class MyAdapter extends BaseAdapter {

		LayoutInflater inflater;
		List<ScanResult> list;

		public MyAdapter(Context context, List<ScanResult> list) {
			// TODO Auto-generated constructor stub
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			/*
			 * getBSSID() 获取BSSID getDetailedStateOf() 获取客户端的连通性 getHiddenSSID()
			 * 获得SSID 是否被隐藏 getIpAddress() 获取IP 地址 getLinkSpeed() 获得连接的速度
			 * getMacAddress() 获得Mac 地址 getRssi() 获得802.11n 网络的信号 getSSID()
			 * 获得SSID getSupplicanState() 返回具体客户端状态的信息
			 */
			View view = null;
			view = inflater.inflate(R.layout.item_wifi_checkbox, null);
			ScanResult scanResult = list.get(position);
			TextView imformation = (TextView) view.findViewById(R.id.item_tv);
			// signalStrenth.setText(String.valueOf(Math.abs(scanResult.level)));

			StringBuffer mStringBuffer = null;
			mStringBuffer = new StringBuffer();
			// level 转换rssi 说明http://www.zhihu.com/question/21106590
			// RSSI = level - NoiceFloor
			// NoiceFloor一般取-96dBm
			// 这样如果 level 是 -60dBm, RSSI 就是 36
			mStringBuffer = mStringBuffer.append("NO.").append(position + 1)
					.append(" \nSSID:").append(scanResult.SSID)
					.append(" \nBSSID：").append(scanResult.BSSID)
					.append(" \ncapabilities：").append(scanResult.capabilities)
					.append(" \nfrequency：").append(scanResult.frequency)
					.append("\nRSSI：").append(scanResult.level)
					.append(" \ndescribeContents：")
					.append(scanResult.describeContents()).append("\n\n");
			imformation.setText(mStringBuffer.toString());
			// mLabelWifi.setText(mStringBuffer.toString());
			// System.out.println(mStringBuffer.toString());
			CheckBox ck = (CheckBox) view.findViewById(R.id.item_cb);
			ck.setChecked(isSelected.get(position));

			ck.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					isSelected.put(position, !isSelected.get(position));
					// Toast.makeText(Main.this, "CheckBox点击事件",
					// Toast.LENGTH_SHORT).show();
				}
			});

			return view;
		}

	}

	private class RssiAPTask extends AsyncTask<String, Integer, String> {
		protected void onPreExecute() {
			dialog.show();
			// Log.i(TAG, "onPreExecute() called");
			// textView.setText("loading...");
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			for (int i = 0; i < 10; i++) {
				wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				list = wifiManager.getScanResults();

				for (int j = 0; j < list.size(); j++) {
					if (list.get(j).BSSID.equals(MAC[0])) {
						rssi_tmp[0][i] = list.get(j).level;

					}
					if (list.get(j).BSSID.equals(MAC[1])) {
						rssi_tmp[1][i] = list.get(j).level;

					}
					if (list.get(j).BSSID.equals(MAC[2])) {
						rssi_tmp[2][i] = list.get(j).level;
					}
					if(rssi_tmp[0][i]!=0 &&rssi_tmp[1][i]!=0&&rssi_tmp[2][i]!=0){
						break;
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			float tmp1 = 0;
			float tmp2 = 0;
			float tmp3 = 0;
			for (int i = 0; i < 10; i++) {
				tmp1 = tmp1 + (float) rssi_tmp[0][i];
				tmp2 = tmp2 + (float) rssi_tmp[1][i];
				tmp3 = tmp3 + (float) rssi_tmp[2][i];
			}
			rssi[0] = tmp1 / 10;
			rssi[1] = tmp2 / 10;
			rssi[2] = tmp3 / 10;

				for (int i = 0; i < 3; i++) {
					r_min[i] = (float) Math.pow(10, -(rssi[i] + 10) / 35);
					r_max[i] = (float) Math.pow(10, -(rssi[i] - 30) / 50);
					if(MAC[i].equals("24:de:c6:e1:29:61")){
						X[i]=35;
						Y[i]=7;
					}
					if(MAC[i].equals("d8:c7:c8:38:22:61")){
						X[i]=147;
						Y[i]=13;
					}
					if(MAC[i].equals("d8:c7:c8:38:22:c1")){
						X[i]=53;
						Y[i]=-36;
					}
				}
				Intent in = new Intent(ListAP_location.this, DrawMapViewActivity.class);
				in.putExtra("r_min", r_min);
				in.putExtra("r_max", r_max);
				in.putExtra("X", X);
				in.putExtra("Y", Y);
				startActivity(in);
			

			return null;
		}

		protected void onPostExecute(String result) {
			dialog.dismiss();
		//text.setText("r: " +r_min[0]+" "+r_min[1]+" "+r_min[2]);

		}

	}
}
