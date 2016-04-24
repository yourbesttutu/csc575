package com.example.testrssi;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button button1, button2, button3, button4,button5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button4 = (Button) findViewById(R.id.button4);
		button5 = (Button) findViewById(R.id.button5);
		button1.setOnClickListener((android.view.View.OnClickListener) this);
		button2.setOnClickListener((android.view.View.OnClickListener) this);
		button3.setOnClickListener((android.view.View.OnClickListener) this);
		button4.setOnClickListener((android.view.View.OnClickListener) this);
		button5.setOnClickListener((android.view.View.OnClickListener) this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			Intent in = new Intent(MainActivity.this, WifiExample.class);
			startActivity(in);
			break;
		case R.id.button2:
			Intent in2 = new Intent(MainActivity.this, WifiListActivity.class);
			startActivity(in2);
			break;
		case R.id.button3:
			Intent in3 = new Intent(MainActivity.this, SaveFile.class);
			startActivity(in3);
			break;

		case R.id.button4:
			Intent in4 = new Intent(MainActivity.this, DrawMapViewActivity.class);
			startActivity(in4);
			break;
		case R.id.button5:
			Intent in5 = new Intent(MainActivity.this, ListAP_location.class);
			startActivity(in5);
			break;
		}
	}

}
