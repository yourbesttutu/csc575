package com.example.testrssi;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Move extends Activity {

	private Button button;
	private int count = 0;
	float []rssi=new float[3];
	float []r_min=new float[3];
	float []r_max=new float[3];
	private int[] X=new int[3];
	private int[] Y=new int[3];
	private int[] rssi_tmp=new int[10];
	String result;
	private TextView MacAdd;
	private TextView Info;
	private ProgressDialog dialog;
    private RssiTask mTask;
    private EditText locationX;
    private EditText locationY;
    private WifiManager wifiManager;  
    List<ScanResult> list; 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.move);
		MacAdd = (TextView) findViewById(R.id.Mac);
		locationX=(EditText)findViewById(R.id.location_x);
		locationY=(EditText)findViewById(R.id.location_y);
		Info = (TextView) findViewById(R.id.Text);
		button = (Button) findViewById(R.id.saveButton);
		Intent intent = getIntent();
		result = intent.getStringExtra("result");
		MacAdd.setText("Mac addr for choosen AP :" + result);
		dialog = new ProgressDialog(this);
		dialog.setTitle("Progressing");
		dialog.setMessage("Getting Rssi Value.......");
		dialog.setCancelable(false);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 注意每次需new一个实例,新建的任务只能执行一次,否则会出现异常
				X[count]=Integer.parseInt(locationX.getText().toString());
				Y[count]=Integer.parseInt(locationY.getText().toString());
				mTask = new RssiTask();
				mTask.execute();

			}
		});

	}

	private class RssiTask extends AsyncTask<String, Integer, String> {
		protected void onPreExecute() {
			dialog.show();
			//Log.i(TAG, "onPreExecute() called");
			//textView.setText("loading...");
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
		for(int i=0;i<10;i++){
			wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
			list = wifiManager.getScanResults(); 
			for(int j=0;j<list.size();j++){
				if(list.get(j).BSSID.equals(result)){
					rssi_tmp[i]=list.get(j).level;
					System.out.printf("get value");
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
		
		float tmp = 0;
		for(int i=0;i<10;i++){
		
		tmp=tmp+(float)rssi_tmp[i];
		}
			rssi[count]=tmp/10;
			count++;
		if(count == 3){
			for(int i=0;i<3;i++){
				r_min[i]=(float) Math.pow(10,-(rssi[i]+10)/35);
				r_max[i]=(float) Math.pow(10,-(rssi[i]-30)/50);
			}
			Intent in = new Intent(Move.this, DrawMapViewActivity.class);
        	in.putExtra("r_min", r_min);
        	in.putExtra("r_max", r_max);
        	in.putExtra("X", X); 
        	in.putExtra("Y", Y); 
			startActivity(in);
		}
		
			return null;
		}
		protected void onPostExecute(String result) {
		dialog.dismiss();
	    Info.setText("already get "+ count +" rssi value");
	
		}

	}
}
